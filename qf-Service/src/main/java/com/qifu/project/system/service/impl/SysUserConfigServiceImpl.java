package com.qifu.project.system.service.impl;

import com.qifu.common.utils.SecurityUtils;
import com.qifu.project.system.domain.SysUserConfig;
import com.qifu.project.system.mapper.SysUserConfigMapper;
import com.qifu.project.system.service.ISysUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置管理-系统配置 服务层实现
 */
@Service
public class SysUserConfigServiceImpl implements ISysUserConfigService {
    @Autowired
    private SysUserConfigMapper sysUserConfigMapper;

    /**
     * 获取系统配置列表
     *
     * @param info 信息
     * @return 系统配置列表
     */
    @Override
    public List<SysUserConfig> selectList(SysUserConfig info) {
        info.setUserId(SecurityUtils.userId());
        List<SysUserConfig> list = sysUserConfigMapper.selectList(info);
        if (info.getType() == 22) {
            if (list != null && list.size() > 0) {
                for (SysUserConfig sysUserConfig : list) {
                    sysUserConfig.setDetails(sysUserConfigMapper.selectById(Long.valueOf(sysUserConfig.getDetails())).getName());
                }
            }
        }
        return list;
    }

    /**
     * 根据id获取详细信息
     *
     * @param id ID
     * @return 公详细信息
     */
    @Override
    public SysUserConfig selectById(Long id) {
        return sysUserConfigMapper.selectById(id);
    }

    /**
     * 新增通系统配置
     *
     * @param info 信息
     * @return 结果
     */
    @Override
    public int insertInfo(SysUserConfig info) {
        return sysUserConfigMapper.insertInfo(info);
    }

    /**
     * 修改通系统配置
     *
     * @param info 信息
     * @return 结果
     */
    @Override
    public int updateInfo(SysUserConfig info) {
        return sysUserConfigMapper.updateInfo(info);
    }

    /**
     * 删除系统配置
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return sysUserConfigMapper.deleteById(id);
    }

    /**
     * 批量删除统配置
     *
     * @param ids 需要删除的ID
     * @return 结果
     */
    public int deleteByIds(Long[] ids) {
        return sysUserConfigMapper.deleteByIds(ids);
    }
}
