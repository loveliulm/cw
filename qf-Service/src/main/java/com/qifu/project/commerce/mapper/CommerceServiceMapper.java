package com.qifu.project.commerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.domain.vo.CommerceServiceVo;
import com.qifu.project.commerce.domain.vo.StatisticalData;

import java.util.List;
import java.util.Map;

/**
 * 工商服务任务Mapper接口
 *
 *  YM
 * 08-14
 */
public interface CommerceServiceMapper extends BaseMapper<CommerceService> {

    List<CommerceServiceVo> getList(Map<String,Object> map);

    /**
     * 根据合同ids删除工商服务
     * @param contractIds
     * @return
     */
    int deleteBycontractIds(long[] contractIds);

    /**
     * 根据工商服务id查询
     * @param id
     * @return
     */
    CommerceServiceVo selectById(Long id);

    /**
     * 根据服务ids查询列表
     * @param ids
     * @return
     */
    List<CommerceService> getListByIds(Long[] ids);

    /**
     * 获取首页统计数据
     * @param map
     * @return
     */
    StatisticalData getIndexData(Map<String,Object> map);
}
