package com.qifu.accounting.carryover.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.CarryoverCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 结转模板对象 book_carryover_model
 *
 *  YM
 * 07-23
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_carryover_model")
public class BookCarryoverModel implements Serializable {

private static final long serialVersionUID=1L;


    @TableId(value = "carryover_model_id", type = IdType.AUTO)
    private Long carryoverModelId;

    private Long templateId;

    private String text;

    private CarryoverCodeEnum code;

    private String money;

    /** 显示，隐藏 */
    @Excel(name = "显示，隐藏")
    private Boolean status;

    /** 是否显示规则 */
    @Excel(name = "是否显示规则")
    private Boolean showRule;

    /** 结转损益字段：收入和损益是否分开，默认null */
    @Excel(name = "结转损益字段：收入和损益是否分开，默认null")
    private Boolean profitLossOpen;

}
