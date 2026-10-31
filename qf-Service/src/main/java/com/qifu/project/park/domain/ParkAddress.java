package com.qifu.project.park.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 园区地址对象 sys_park_address
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_park_address")
public class ParkAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 部门id */
    private Long deptId;

    /**
     * 编号
     */
    private String number;

    /**
     * 注册地址
     */
    private String address;

    /**
     * 地址状态1=闲置,2=再用
     *
     * @Excel(name = "地址状态", readConverterExp = "1=闲置,2=再用")
     */
    private Integer addressStatus;

    /**
     * 所在地区
     */
    private String regionname;

    /**
     * 同行联系人
     */
    private String contacts;

    /**
     * 同行联系人电话
     */
    private String phone;

    /**
     * 成本价
     */
    private BigDecimal costAmount;

    /**
     * 备注
     */
    private String remark;

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


}
