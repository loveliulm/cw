package com.qifu.project.customer.service;

import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.customer.domain.TAccountBook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.vo.AccountBookVoTwo;
import com.qifu.project.customer.domain.vo.BookAccountTemplateVo;
import com.qifu.project.customer.domain.vo.TAccountBookVo;

import java.util.List;
import java.util.Map;

/**
 * 账套Service接口
 *
 *  YM
 * 06-30
 */
public interface ITAccountBookService extends IService<TAccountBook> {
    List<TAccountBookVo> selectListBook(TAccountBookVo bookVo);

    List<BookAccountTemplateVo> getAccountTemplate();

    /**
     * 获取首页统计数据
     * @param map
     * @return
     */
    StatisticalData getIndexData(Map<String,Object> map);

    /**
     * 验证公司账套数
     * @param deptId
     * @return
     */
    boolean checkBookNumber(Long deptId);

    /**
     * 查询公司账套使用情况
     * @param deptId
     */
    AccountBookVoTwo selectAccountBookStatusByDeptId(Long deptId);

    /**
     * 验证代账服务服务状态
     * @param id
     * @return true:正常服务，false:停止服务
     */
    boolean checkServiceStatus(Long id);
}
