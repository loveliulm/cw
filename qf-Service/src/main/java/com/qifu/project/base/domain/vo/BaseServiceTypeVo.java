package com.qifu.project.base.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.domain.BaseServiceType;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 *  yangyang
 * 06-23
 */
@Getter
@Setter
public class BaseServiceTypeVo extends BaseServiceType {
    private List<BaseServiceItem> serviceItems;
}
