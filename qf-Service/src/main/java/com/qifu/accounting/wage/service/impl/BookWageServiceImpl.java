package com.qifu.accounting.wage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.common.factory.WageFactory;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.accounting.voucher.domain.BookVoucherModelRecord;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.accounting.wage.domain.BookWage;
import com.qifu.accounting.wage.mapper.BookWageMapper;
import com.qifu.accounting.wage.service.IBookWageService;
import com.qifu.common.exception.CustomException;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.bean.BeanUtils;
import com.qifu.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *  yangyang
 * @date 2020/7/20 15:32
 */
@Service
public class BookWageServiceImpl extends ServiceImpl<BookWageMapper, BookWage> implements IBookWageService {
    @Autowired
    private WageFactory wageFactory;

    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private BookWageMapper wageMapper;

    @Autowired
    private IBookVoucherService bookVoucherService;

    public Map<?,?>  wageTemplate(Long bookId,VoucherTypeEnum typeEnum){
        //获取当前账期
        BookPeriod period = periodService.getInfo(bookId);
        Date yearPeriod = period.getYearPeriod();
        Map<?,?> result = wageFactory.wageTemplate(bookId,yearPeriod,typeEnum);
        return result;
    }

    /**
     * 查询工资列表
     * @param bookWage
     * @return
     */
    public  List<BookWage> getList(BookWage bookWage){
        return wageMapper.getList(bookWage);
    }

    /**
     * 查询工资列表
     * @param id
     * @return
     */
    public  BookWage getWageById(Long id){
        return wageMapper.getWageById(id);
    }


    public String importList(List<BookWage> list){
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BookWage wage : list) {
           try{
               wage.setCreateTime(new Date());
               wage.setUpdateTime(new Date());
               int i = wageMapper.insert(wage);
               if(i == 1){
                   successNum++;
                   successMsg.append("<br/>" + successNum + "、人员：" + wage.getPersonName() + " 导入成功");
               }else {
                   failureNum++;
                   successMsg.append("<br/>" + successNum + "、人员：" + wage.getPersonName() + " 导入失败");
               }
           }catch (Exception e){
               failureNum++;
               String msg = "<br/>" + failureNum + "、人员：" + wage.getPersonName() + " 导入失败：";
               failureMsg.append(msg + e.getMessage());
               log.error(msg, e);
           }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();

    }

    /**
     * 判断计提，发放工资是否生成。
     * @param bookId
     * @return
     */
    public Map<Object, Object> checkWages(Long bookId) {
        BookPeriod period = periodService.getInfo(bookId);
        Date yearPeriod = period.getYearPeriod();
        LambdaQueryWrapper<BookVoucher> lqw = new LambdaQueryWrapper<BookVoucher>();
        if (bookId != null){
            lqw.eq(BookVoucher::getBookId ,bookId);
        }
        lqw.eq(BookVoucher::getVoucherType, VoucherTypeEnum.计提工资);
        lqw.eq(BookVoucher::getDelFlag, StringDateUtil.Del_FLAG_0);
        lqw.eq(BookVoucher::getYearPeriod,yearPeriod);
        List<BookVoucher> list1 = bookVoucherService.list(lqw);

        LambdaQueryWrapper<BookVoucher> lqw2 = new LambdaQueryWrapper<BookVoucher>();
        if (bookId != null){
            lqw2.eq(BookVoucher::getBookId ,bookId);
        }
        lqw2.eq(BookVoucher::getVoucherType, VoucherTypeEnum.发放工资);
        lqw2.eq(BookVoucher::getDelFlag, StringDateUtil.Del_FLAG_0);
        lqw2.eq(BookVoucher::getYearPeriod,yearPeriod);
        List<BookVoucher> list2 = bookVoucherService.list(lqw2);


        Map<Object,Object> result = new HashMap<>();
        if(list1.isEmpty()){
            result.put("provision",false);
        }else{
            result.put("provision",true);
        }
        if(list2.isEmpty()){
            result.put("grant",false);
        }else{
            result.put("grant",true);
        }
        return result;
    }
}
