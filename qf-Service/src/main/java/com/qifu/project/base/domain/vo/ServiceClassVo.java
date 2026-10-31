package com.qifu.project.base.domain.vo;

import lombok.Data;

import java.util.List;

/**
 *  yangyang
 * @date 2020/6/28 16:54
 */
@Data
public class ServiceClassVo {
    private Long id;
    private String name;
    private Long fathId;
    private List<ServiceTypeVo> serviceTypeVos;
}
