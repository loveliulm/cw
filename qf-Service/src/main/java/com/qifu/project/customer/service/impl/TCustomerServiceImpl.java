package com.qifu.project.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.common.constant.UserConstants;
import com.qifu.common.exception.CustomException;
import com.qifu.common.exception.UtilException;
import com.qifu.common.utils.*;
import com.qifu.project.base.domain.BaseServiceType;
import com.qifu.project.base.service.IBaseServiceTypeService;
import com.qifu.project.common.enums.ServerStatusEnum;
import com.qifu.project.common.enums.ServiceTypeNameEnum;
import com.qifu.project.customer.domain.TAccountBook;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.TCustomerServiceType;
import com.qifu.project.customer.domain.vo.ReportData;
import com.qifu.project.customer.domain.vo.TCustomerVo;
import com.qifu.project.customer.mapper.TAccountBookMapper;
import com.qifu.project.customer.mapper.TCustomerMapper;
import com.qifu.project.customer.mapper.TCustomerServiceTypeMapper;
import com.qifu.project.customer.service.ITAccountBookService;
import com.qifu.project.customer.service.ITContractService;
import com.qifu.project.customer.service.ITCustomerService;
import com.qifu.project.customer.service.ITCustomerServiceTypeService;
import com.qifu.project.system.domain.SysNumberRule;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.mapper.SysRoleMapper;
import com.qifu.project.system.mapper.SysUserMapper;
import com.qifu.project.system.service.ISysNumberRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 客户Service业务层处理
 *
 *
 * 06-15
 */
@Service
public class TCustomerServiceImpl extends ServiceImpl<TCustomerMapper, TCustomer> implements ITCustomerService {
    @Autowired
    private TCustomerMapper customerMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private TCustomerServiceTypeMapper serviceTypeMapper;
    @Autowired
    private ITCustomerServiceTypeService serviceTypeService;
    @Autowired
    private TAccountBookMapper bookMapper;
    @Autowired
    private ITContractService itContractService;
    @Autowired
    private ISysNumberRuleService sysNumberRuleService;
    @Autowired
    private IBaseServiceTypeService baseServiceTypeService;
    @Autowired
    private ITAccountBookService itAccountBookService;

    public List<TCustomerVo> customerList(TCustomer customer) {
        customer.setDeptId(SecurityUtils.companyId());
        List<TCustomerVo> list = customerMapper.customerList(customer);
        StringBuilder sb = null;
        for (TCustomerVo customerVo : list) {
            TCustomerServiceType[] serviceTypes = serviceTypeMapper.getListByCustomerId(customerVo.getId());
            if (customer.getAccountingStatus() == null || customer.getAccountingStatus() == StringDateUtil.ACCOUNTING_STATUS_0) {
                customerVo.setIsCreateAccount(is(serviceTypes));
            }
            sb = new StringBuilder();
            for (int i = 0; i < serviceTypes.length; i++) {
                if (i == 0) {
                    sb.append(serviceTypes[i].getServiceTypeName());
                } else {
                    sb.append("，" + serviceTypes[i].getServiceTypeName());
                }
            }
            customerVo.setTCustomerServiceTypesName(sb.toString());
            customerVo.setTCustomerServiceTypes(serviceTypes);
        }
        return list;
    }

    private Boolean is(TCustomerServiceType[] serviceTypes) {
        for (TCustomerServiceType type : serviceTypes) {
            if (type.getServiceTypeName().contains(ServiceTypeNameEnum.代理记账.name())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public TCustomerVo customerById(long id) {
        TCustomerVo customerVo = customerMapper.customerById(id);
        customerVo.setTCustomerServiceTypes(serviceTypeMapper.getListByCustomerId(id));
        return customerVo;
    }


    @Transactional
    public int deleteCustomerByIds(Long[] customerIds) {
        //获取合同列表
        LambdaQueryWrapper<TContract> lqw = new LambdaQueryWrapper();
        lqw.in(TContract::getCustomerId, customerIds);
        List<TContract> tcList = itContractService.list(lqw);
        long[] tcIds = new long[tcList.size()];
        for (int i = 0; i < tcList.size(); i++) {
            tcIds[i] = tcList.get(i).getContractId();
        }
        //删除合同
        if (tcList.size() > 0) {
            itContractService.deleteContractByIds(tcIds);
        }

        //删除代账服务
        bookMapper.deleteByCustomerId(customerIds);

        return customerMapper.deleteCustomerByIds(customerIds);
    }

    public List<SysUser> selectUserList(String roleKey, Long deptId) {
        //判断该部门是否有该角色
        SysRole role = sysRoleMapper.roleByKeyDeptId(roleKey, deptId);
        if (role == null) {
            return null;
        }
        return sysUserMapper.selectUserListByRoleKey(roleKey, deptId);
    }

    public int createAccount(int customerId) {
        TCustomer customer = customerMapper.selectById(customerId);
        Asserts.isNull(customer, "该客户不存在或已删除，不能创建账套");
        Asserts.isTrue(customer.getAccountingStatus() == StringDateUtil.ACCOUNTING_STATUS_0, "该客户已创建账套，不能重复创建！");
        //判断该客户是否是代账服务
        TCustomerServiceType[] serviceTypes = serviceTypeMapper.getListByCustomerId(customer.getId());
        Asserts.isTrue(is(serviceTypes), "该客户不是代理记账类型，不能创建账套！");

        customer.setAccountingStatus(StringDateUtil.ACCOUNTING_STATUS_1);
        int i = customerMapper.updateById(customer);
        Asserts.isTrue(i == 1, "账套创建失败！");
        //创建代账服务的一条记录
        return i;
    }

    @Override
    public TCustomer findFirstByDeptId(Long deptId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date curTime = cal.getTime();
        LambdaQueryWrapper<TCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TCustomer::getDeptId, deptId)
                .ge(TCustomer::getCreateTime, curTime)
                .orderByDesc(TCustomer::getId)
                .last("limit 1");
        TCustomer customer = customerMapper.selectOne(wrapper);
        return customer;
    }

    @Transactional
    public void add(TCustomerVo tCustomer) {
        try {
            TCustomerServiceType[] customerServiceTypes = tCustomer.getTCustomerServiceTypes();
            tCustomer.setDeptId(SecurityUtils.companyId());
            tCustomer.setStatus(StringDateUtil.STATUS_0);
            tCustomer.setDelFlag(StringDateUtil.Del_FLAG_0);
            tCustomer.setCreateTime(new Date());
            tCustomer.setCreateBy(SecurityUtils.getUsername());
            int i = customerMapper.insert(tCustomer);
            Asserts.isTrue(i == 1, "客户添加失败");
            for (TCustomerServiceType serviceType : customerServiceTypes) {
                serviceType.setCustomerId(tCustomer.getId());
                serviceType.setCreateTime(new Date());
                serviceTypeService.save(serviceType);
            }

            //创建账套
            createAccountBook(tCustomer, customerServiceTypes);


        } catch (Exception e) {
//            throw new UtilException("新增客户失败");
            throw e;
        }
    }

    /**
     * 创建账套
     * 1:是否包含代理记账，2：已分配代账人员
     *
     * @param tCustomer
     */
    private void createAccountBook(TCustomer tCustomer, TCustomerServiceType[] serviceTypes) {

//        Long assisUserId = tCustomer.getAssistantAccountingUserId();
        Long bookkeepUserId = tCustomer.getBookkeepingAccountingUserId();
//        Long managerUserId = tCustomer.getManagerAccountingUserId();

        if (is(serviceTypes) &&
                bookkeepUserId != null) {
            TAccountBook accountBook = new TAccountBook(tCustomer);
            TAccountBook book = bookMapper.selectByCustomerId(tCustomer.getId());
            if (book != null) {//修改
                accountBook.setBookId(book.getBookId());
                accountBook.setStatus(0);
                int i = bookMapper.updateById(accountBook);
                Asserts.isTrue(i == 1, "创建账套失败");
            } else {
//                //验证公司账套数是否到达上限
//                boolean b = itAccountBookService.checkBookNumber(SecurityUtils.companyId());
//                Asserts.isTrue(b,"操作失败，账套数已达上限");
                //新增
                accountBook.setVoucherCount(0);
                accountBook.setServerStatus(ServerStatusEnum.正常服务);
                accountBook.setCreateTime(new Date());
                accountBook.setBookCount(0);
                accountBook.setStatus(0);
                accountBook.setIsDel(StringDateUtil.Del_FLAG_0);
                accountBook.setDeptId(SecurityUtils.companyId());
                int i = bookMapper.insert(accountBook);
                Asserts.isTrue(i == 1, "创建账套失败");
            }
        }
    }


    @Transactional
    public void edit(TCustomerVo tCustomer) {
        try {

            TCustomerServiceType[] serviceTypes = tCustomer.getTCustomerServiceTypes();

//            Long assisUserId = tCustomer.getAssistantAccountingUserId();
            Long bookkeepUserId = tCustomer.getBookkeepingAccountingUserId();
//            Long managerUserId = tCustomer.getManagerAccountingUserId();
            if (is(serviceTypes) && bookkeepUserId != null) {
                //创建账套
                createAccountBook(tCustomer, serviceTypes);
            }else{
                TAccountBook book = bookMapper.selectByCustomerId(tCustomer.getId());
                if (book != null) {//修改
                    TAccountBook accountBook = new TAccountBook(tCustomer);
                    accountBook.setBookId(book.getBookId());
                    accountBook.setStatus(1);
                    int i = bookMapper.updateById(accountBook);
                    Asserts.isTrue(i == 1, "创建账套失败");
                }
            }


            //删除原有服务类型，新增新服务类型
            serviceTypeService.removeByCustomerId(tCustomer.getId());
            for (TCustomerServiceType serviceType : serviceTypes) {
                serviceType.setCustomerId(tCustomer.getId());
                serviceType.setCreateTime(new Date());
                serviceTypeService.save(serviceType);
            }

            int i = customerMapper.updateById(tCustomer);
            Asserts.isTrue(i == 1, "修改客户失败");
        } catch (Exception e) {
//            throw new UtilException("修改客户失败");
            throw e;
        }
    }

    @Transactional
    public void dispatch(TCustomer tCustomer) {
        TCustomerServiceType[] serviceTypes = tCustomer.getTCustomerServiceTypes();
        //创建账套
        createAccountBook(tCustomer, serviceTypes);
        int i = customerMapper.updateById(tCustomer);
        Asserts.isTrue(i == 1, "派工失败");
    }

    @Override
    public ReportData selectByAccount(TCustomer customer) {
        return customerMapper.selectByAccount(customer);
    }

    public void add(TCustomer tCustomer) {
        try {
            TCustomerServiceType[] customerServiceTypes = tCustomer.getTCustomerServiceTypes();
            tCustomer.setDeptId(SecurityUtils.companyId());
            tCustomer.setStatus(StringDateUtil.STATUS_0);
            tCustomer.setDelFlag(StringDateUtil.Del_FLAG_0);
            tCustomer.setCreateTime(new Date());
            tCustomer.setCreateBy(SecurityUtils.getUsername());
            int i = customerMapper.insert(tCustomer);
            Asserts.isTrue(i == 1, "客户添加失败");
            for (TCustomerServiceType serviceType : customerServiceTypes) {
                serviceType.setCustomerId(tCustomer.getId());
                serviceType.setCreateTime(new Date());
                serviceTypeService.save(serviceType);
            }

            //创建账套
            createAccountBook(tCustomer, customerServiceTypes);

        } catch (Exception e) {
            throw new UtilException("新增客户失败");
        }
    }

    /**
     * 导入用户数据
     *
     * @param customerList    客户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    @Transactional
    public String importCustomer(List<TCustomer> customerList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(customerList) || customerList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        List<BaseServiceType> typeList = baseServiceTypeService.list();
        for (TCustomer customer : customerList) {
            Asserts.isNull(customer.getCustomerName(), "很抱歉，导入失败！客户姓名不能为空！");
            //判断系统中是否已有该客户
            TCustomer c = customerMapper.selectByName(customer.getCustomerName());
            Asserts.notNull(c, "很抱歉，导入失败！系统中已有'" + customer.getCustomerName() + "'该客户!");

            try {
                TCustomerServiceType[] customerServiceTypes = null;
                List<TCustomerServiceType> tCustomerServiceTypesList = new ArrayList<TCustomerServiceType>();
                for (BaseServiceType bst : typeList) {
                    if ("0".equals(customer.getVoucher()) && bst.getServiceTypeName().equals("代理记账")) {
                        tCustomerServiceTypesList.add(create(customer, bst));
                        //设置默认记账会计
                        List<SysUser> userList =  this.selectUserList("accounting", SecurityUtils.companyId());
                        if(userList.size() > 0){
                            customer.setBookkeepingAccountingUserId(userList.get(0).getUserId());
                        }
                    }
                    if ("0".equals(customer.getRegister()) && bst.getServiceTypeName().equals("工商注册")) {
                        tCustomerServiceTypesList.add(create(customer, bst));
                    }
                    if ("0".equals(customer.getChange()) && bst.getServiceTypeName().equals("工商变更")) {
                        tCustomerServiceTypesList.add(create(customer, bst));
                    }
                    if ("0".equals(customer.getLogoff()) && bst.getServiceTypeName().equals("工商注销")) {
                        tCustomerServiceTypesList.add(create(customer, bst));
                    }
                }
                //判断客户纳税性质是否填写
                if(customer.getVatType() == null){
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、客户 " + customer.getCustomerName() + " 纳税性质未填或填写错误";
                    failureMsg.append(msg);
                }
                //判断客户是否已选服务类型
                if(tCustomerServiceTypesList.size() == 0){
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、客户 " + customer.getCustomerName() + " 必选一个服务类型";
                    failureMsg.append(msg);
                }

                customerServiceTypes = new TCustomerServiceType[tCustomerServiceTypesList.size()];
                customerServiceTypes = tCustomerServiceTypesList.toArray(customerServiceTypes);
                customer.setTCustomerServiceTypes(customerServiceTypes);
                customer.setCustomerCode(getCustomerNo());
                customer.setIsOpenInvoice(0);
                this.add(customer);
                successNum++;
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、客户 " + customer.getCustomerName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }

        return successMsg.toString();
    }

    @Override
    public String checkCodeUnique(String code) {
        LambdaQueryWrapper<TCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TCustomer::getCustomerCode,code);
        lambdaQueryWrapper.eq(TCustomer::getDeptId,SecurityUtils.companyId());
        TCustomer customer = customerMapper.selectOne(lambdaQueryWrapper);
        if (customer != null) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkNameUnique(TCustomer tCustomer) {
        Long userId = StringUtils.isNull(tCustomer.getId()) ? -1L : tCustomer.getId();
        LambdaQueryWrapper<TCustomer> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TCustomer::getDeptId,SecurityUtils.companyId());
        lambdaQueryWrapper.eq(TCustomer::getDelFlag,StringDateUtil.Del_FLAG_0);
        lambdaQueryWrapper.eq(TCustomer::getCustomerName,tCustomer.getCustomerName());
        TCustomer info = customerMapper.selectOne(lambdaQueryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 生成客户编码
     */
    public synchronized String getCustomerNo() {
        String customerNo = null;
        Long deptId = SecurityUtils.getDeptId();
        //查找规则
        SysNumberRule sysNumberRules = sysNumberRuleService.findByRuleType(1);
        TCustomer customer = this.findFirstByDeptId(deptId);
        if (customer == null) {
            customerNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), null);
        } else {
            customerNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), customer.getCustomerCode());
        }
        return customerNo;
    }

    public TCustomerServiceType create(TCustomer customer, BaseServiceType baseServiceType) {
        TCustomerServiceType cst = new TCustomerServiceType();
        cst.setCustomerId(customer.getId());
        cst.setServiceTypeId(baseServiceType.getServiceTypeId());
        cst.setServiceTypeName(baseServiceType.getServiceTypeName());
        return cst;
    }
}
