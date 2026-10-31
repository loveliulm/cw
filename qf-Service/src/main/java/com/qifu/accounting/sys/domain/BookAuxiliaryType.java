package com.qifu.accounting.sys.domain;

import com.qifu.accounting.common.enums.AuxiliaryTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;

/**
 * 辅组核算类型对象 book_auxiliary_type
 *
 *  YM
 * 07-09
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_auxiliary_type")
public class BookAuxiliaryType implements Serializable {

private static final long serialVersionUID=1L;


    /** 辅组核算类型id */
    @TableId(value = "auxiliary_type_id", type = IdType.AUTO)
    private Long auxiliaryTypeId;

    private AuxiliaryTypeEnum auxiliaryType;

    private AuxiliaryTypeEnum auxiliaryTypeText;
}
