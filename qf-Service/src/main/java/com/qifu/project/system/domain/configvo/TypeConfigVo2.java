package com.qifu.project.system.domain.configvo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 配置管理-系统配置 sys_user_config
 */
@Data
public class TypeConfigVo2 implements Serializable {

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

   /**
     * 配置分类 1=客户来源管理,2=日记账分类管理,3=日记账收支方式管理,4=老板信息管理,5=税务局信息管理,6=工商局信息管理,7=公司/企业类型管理,8=公司客户状态管理,9=缴税税种信息管理,10=税务登记信息管理,11=登录身份信息管理,21=物品类型管理,22=物品名称管理,23=物品库位置管理,24=物品出库方式管理
     */
    @Excel(name = "配置分类", readConverterExp = "1=客户来源管理,2=日记账分类管理,3=日记账收支方式管理,4=老板信息管理,5=税务局信息管理,6=工商局信息管理,7=公司/企业类型管理,8=公司客户状态管理,9=缴税税种信息管理,10=税务登记信息管理,11=登录身份信息管理,21=物品类型管理,22=物品名称管理,23=物品库位置管理,24=物品出库方式管理")
    private Integer type;

    /**
     * 名称
     */
    @Excel(name = "日记账分类名称")
    private String name;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private int sort;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
