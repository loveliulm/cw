package com.qifu.project.park.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 园区企业对象 sys_park_enterprise
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_park_enterprise")
public class ParkEnterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务项目名称
     */
    private String serviceItemName;

    /**
     * 父id
     */
    private Long serviceItemParentId;

    /**
     * 父名称
     */
    @TableField(exist = false)
    private String serviceItemParentName;

    /**
     * 服务类型id
     */
    private Long serviceTypeId;

    /**
     * 工商局id
     */
    private Long sysUserConfigId;

    /**
     * 工商局名称
     */
    @TableField(exist = false)
    private String sysUserConfigName;

    /**
     * 是否属于成本
     */
//    @Excel(name = "是否属于成本", readConverterExp = "0=否,1=是")
    private Integer isCost;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
