package com.qifu.project.customer.mapper;

import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.customer.domain.TAccountBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.customer.domain.vo.AccountBookVoTwo;
import com.qifu.project.customer.domain.vo.BookAccountTemplateVo;
import com.qifu.project.customer.domain.vo.TAccountBookVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 账套Mapper接口
 *
 *  YM
 * 06-30
 */
@Repository
public interface TAccountBookMapper extends BaseMapper<TAccountBook> {
    List<TAccountBookVo> selectListBook(TAccountBookVo bookVo);

    TAccountBook selectByCustomerId(@Param("customerId") Long customerId);

    List<BookAccountTemplateVo> getAccountTemplate();

    /**
     * 获取首页统计数据
     * @param map
     * @return
     */
    StatisticalData getIndexData(Map<String,Object> map);

    /**
     * 根据客户id删除
     */
    int deleteByCustomerId(Long[] customerIds);

    /**
     * 查询公司账套使用情况
     * @param deptId
     */
    AccountBookVoTwo selectAccountBookStatusByDeptId(Long deptId);
}
