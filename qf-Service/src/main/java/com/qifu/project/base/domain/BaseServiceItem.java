package com.qifu.project.base.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.util.List;

/**
 * 服务项目对象 base_service_item
 *
 *
 * 06-19
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("base_service_item")
public class BaseServiceItem implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "service_item_id", type = IdType.AUTO)
    private Long serviceItemId;

    /** 服务项目名称 */
    @Excel(name = "服务项目名称")
    private String serviceItemName;

    /** 父id */
    @Excel(name = "父id")
    private Long serviceItemParentId;

    /** 父名称 */
    @TableField(exist = false)
    private String serviceItemParentName;

    /** 服务类型id */
    @Excel(name = "服务类型id")
    private Long serviceTypeId;

    /** 工商局id */
    @Excel(name = "工商局id")
    private Long sysUserConfigId;

    /** 工商局名称 */
    @TableField(exist = false)
    private String sysUserConfigName;

    /** 预计完成天数 */
    @Excel(name = "预计完成天数")
    private Integer days;

    @TableField(exist = false)
    private List<BaseServiceItem> children;

}
