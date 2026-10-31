package com.qifu.project.commerce.domain.vo;

import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.qifu.project.commerce.domain.CommerceStepFile;
import lombok.Data;

import java.util.List;

@Data
public class CommerceServiceStepVo extends CommerceServiceStep {

    /**执行人姓名*/
    private String operatorUserName;

    // 附件列表
    private List<CommerceStepFile> commerceStepFileList;
}
