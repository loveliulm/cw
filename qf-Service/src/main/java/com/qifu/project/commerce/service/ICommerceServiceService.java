package com.qifu.project.commerce.service;

import com.qifu.project.commerce.domain.CommerceService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.commerce.domain.vo.CommerceServiceVo;
import com.qifu.project.commerce.domain.vo.StatisticalData;

import java.util.List;
import java.util.Map;

/**
 * 工商服务任务Service接口
 *
 *  YM
 * 08-14
 */
public interface ICommerceServiceService extends IService<CommerceService> {

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
     * @param ids 服务id数组
     * @return
     */
    List<CommerceService> getListByIds(Long[] ids);

    /**
     * 检查服务是否完成
     * @param id
     */
    void checkEnd(long id);

    /**
     * 获取首页统计数据
     * @param map
     * @return
     */
    StatisticalData getIndexData(Map<String,Object> map);

    /**
     * 检查服务状态
     * @return true：正常状态，false：停止状态
     */
    boolean checkServiceStatus(Long id);
}
