package com.qifu.project.system.domain;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.framework.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 配置管理-系统配置 sys_user_config
 */
@Data
public class SysUserConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Excel(name = "主键id", cellType = Excel.ColumnType.NUMERIC)
    private Long id;

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
    @Excel(name = "名称")
    private String name;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private String details;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String unit;

    /**
     * 是否属于成本
     */
    @Excel(name = "是否属于成本", readConverterExp = "0=否,1=是")
    private Integer isCost;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer age;

    /**
     * 性别
     */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private Integer sex;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String phone;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String idCard;

    /**
     * 关联公司id
     */
    private String deptIds;

    /**
     * 地址
     */
    @Excel(name = "地址")
    private String address;

    /**
     * 是否属于终止合同选项
     */
    @Excel(name = "是否属于终止合同选项", readConverterExp = "0=否,1=是")
    private Integer isContract;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private int sort;

}
