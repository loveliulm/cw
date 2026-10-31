package com.qifu.project.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.domain.BaseServiceProcess;
import com.qifu.project.base.domain.vo.BaseServiceTypeVo;
import com.qifu.project.base.domain.vo.ServiceClassVo;
import com.qifu.project.base.domain.vo.ServiceTypeVo;
import com.qifu.project.base.mapper.BaseServiceItemMapper;
import com.qifu.project.system.mapper.SysUserConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.base.mapper.BaseServiceTypeMapper;
import com.qifu.project.base.domain.BaseServiceType;
import com.qifu.project.base.service.IBaseServiceTypeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service业务层处理
 *
 *
 * 06-17
 */
@Service
public class BaseServiceTypeServiceImpl extends ServiceImpl<BaseServiceTypeMapper, BaseServiceType> implements IBaseServiceTypeService {
    @Autowired
    private BaseServiceTypeMapper baseServiceTypeMapper;

    @Autowired
    private BaseServiceItemMapper baseServiceItemMapper;

    @Autowired
    private SysUserConfigMapper sysUserConfigMapper;

    public List<BaseServiceTypeVo> selectServiceTypelist(){
        List<BaseServiceTypeVo> list = baseServiceTypeMapper.selectServiceTypelist();
        list.forEach(typeVo ->{
            LambdaQueryWrapper<BaseServiceItem> lqw = new LambdaQueryWrapper<BaseServiceItem>();
            lqw.eq(BaseServiceItem::getServiceTypeId,typeVo.getServiceTypeId());
            List<BaseServiceItem> items = baseServiceItemMapper.selectList(lqw);
            items = buildTree(items);
            if (items != null && items.size() > 0) {
                items.forEach(info -> {
                    if (info.getChildren() != null && info.getChildren().size() > 0) {
                        for (BaseServiceItem child : info.getChildren()) {
                            String name = sysUserConfigMapper.selectById(child.getSysUserConfigId()).getName();
                            child.setSysUserConfigName(name);
                            child.setServiceItemName(name + "（" + child.getServiceItemName() + "）");
                        }
                    }
                });
            }
            typeVo.setServiceItems(items);
        });
        return list;
    }

    public List<ServiceClassVo> selectServiceTypelistName(){
        List<ServiceClassVo> list = baseServiceTypeMapper.serviceClassList();
        list.forEach(classVo ->{
            List<ServiceTypeVo> list2 = baseServiceTypeMapper.selectServiceTypelistName(classVo.getId());
            classVo.setServiceTypeVos(list2);
        });
        return list;
    }

    public  Long getServiceClassId(Long serviceTypeId){
        return baseServiceTypeMapper.getServiceClassId(serviceTypeId);
    }

    /**
     * 使用递归方法建树
     *
     * @param list 子节点集合
     * @return List
     */
    public static List<BaseServiceItem> buildTree(List<BaseServiceItem> list) {
        List<BaseServiceItem> trees = new ArrayList<>();
        for (BaseServiceItem entity : list) {
            Long parentId = entity.getServiceItemParentId();
            if (0 == parentId) {
                trees.add(findChildren(entity, list));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param entity   菜单数对象
     * @param list 子节点
     * @return MenuTree
     */
    private static BaseServiceItem findChildren(BaseServiceItem entity, List<BaseServiceItem> list) {
        for (BaseServiceItem info : list) {
            if (entity.getServiceItemId().equals(info.getServiceItemParentId())) {
                if (entity.getChildren() == null) {
                    entity.setChildren(new ArrayList<>());
                }
                entity.getChildren().add(findChildren(info, list));
            }
        }
        return entity;
    }

}
