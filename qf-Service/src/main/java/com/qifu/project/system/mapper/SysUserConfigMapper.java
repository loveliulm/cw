package com.qifu.project.system.mapper;

import com.qifu.project.system.domain.SysUserConfig;

import java.util.List;

/**
 * 配置管理-系统配置 数据层
 */
public interface SysUserConfigMapper {
    /**
     * 获取系统配置列表
     *
     * @param info 信息
     * @return 系统配置列表
     */
    public List<SysUserConfig> selectList(SysUserConfig info);

    /**
     * 根据id获取详细信息
     *
     * @param id ID
     * @return 公详细信息
     */
    public SysUserConfig selectById(Long id);

    /**
     * 新增通系统配置
     *
     * @param info 信息
     * @return 结果
     */
    public int insertInfo(SysUserConfig info);

    /**
     * 修改通系统配置
     *
     * @param info 信息
     * @return 结果
     */
    public int updateInfo(SysUserConfig info);

    /**
     * 删除系统配置
     *
     * @param id ID
     * @return 结果
     */
    public int deleteById(Long id);

    /**
     * 批量删除统配置
     *
     * @param ids 需要删除的ID
     * @return 结果
     */
    public int deleteByIds(Long[] ids);
}
