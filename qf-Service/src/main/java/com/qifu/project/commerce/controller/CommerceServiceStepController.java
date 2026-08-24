package com.qifu.project.commerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qifu.common.constant.Constants;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.common.utils.file.FileUploadUtils;
import com.qifu.common.utils.file.FileUtils;
import com.qifu.framework.config.RuoYiConfig;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.domain.CommerceStepFile;
import com.qifu.project.commerce.domain.vo.CommerceServiceStepVo;
import com.qifu.project.commerce.domain.vo.CommerceServiceVo;
import com.qifu.project.commerce.service.ICommerceServiceService;
import com.qifu.project.commerce.service.ICommerceStepFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.qifu.project.commerce.service.ICommerceServiceStepService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 工商服务步骤Controller
 *
 *
 * 08-14
 */
@AllArgsConstructor
@RestController
@RequestMapping("/commerce/step")
@Api(tags = "工商服务流程")
public class CommerceServiceStepController extends BaseController {

    private final ICommerceServiceStepService iCommerceServiceStepService;
    @Autowired
    private ICommerceServiceService commerceServiceService;

    @Autowired
    private ICommerceStepFileService commerceStepFileService;

    /**
     * 查询工商服务步骤列表
     */
    @PreAuthorize("@ss.hasPermi('commerce:step:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommerceServiceStep commerceServiceStep) {
        startPage();
        LambdaQueryWrapper<CommerceServiceStep> lqw = new LambdaQueryWrapper<CommerceServiceStep>();
        if (commerceServiceStep.getCommerceServiceId() != null) {
            lqw.eq(CommerceServiceStep::getCommerceServiceId, commerceServiceStep.getCommerceServiceId());
        }
        if (StringUtils.isNotBlank(commerceServiceStep.getStepName())) {
            lqw.like(CommerceServiceStep::getStepName, commerceServiceStep.getStepName());
        }
        if (commerceServiceStep.getStepOrder() != null) {
            lqw.eq(CommerceServiceStep::getStepOrder, commerceServiceStep.getStepOrder());
        }
        if (StringUtils.isNotBlank(commerceServiceStep.getStepDescribe())) {
            lqw.eq(CommerceServiceStep::getStepDescribe, commerceServiceStep.getStepDescribe());
        }
        if (StringUtils.isNotBlank(commerceServiceStep.getStepData())) {
            lqw.eq(CommerceServiceStep::getStepData, commerceServiceStep.getStepData());
        }
        if (StringUtils.isNotBlank(commerceServiceStep.getStepOutput())) {
            lqw.eq(CommerceServiceStep::getStepOutput, commerceServiceStep.getStepOutput());
        }
        if (commerceServiceStep.getCompleteTime() != null) {
            lqw.eq(CommerceServiceStep::getCompleteTime, commerceServiceStep.getCompleteTime());
        }
        if (commerceServiceStep.getOperatorUserId() != null) {
            lqw.eq(CommerceServiceStep::getOperatorUserId, commerceServiceStep.getOperatorUserId());
        }
        if (commerceServiceStep.getStepStatus() != null) {
            lqw.eq(CommerceServiceStep::getStepStatus, commerceServiceStep.getStepStatus());
        }
        lqw.orderByAsc(CommerceServiceStep::getStepOrder);
        List<CommerceServiceStep> list = iCommerceServiceStepService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出工商服务步骤列表
     */
    @PreAuthorize("@ss.hasPermi('commerce:step:export')")
    @Log(title = "工商服务步骤", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CommerceServiceStep commerceServiceStep) {
        LambdaQueryWrapper<CommerceServiceStep> lqw = new LambdaQueryWrapper<CommerceServiceStep>(commerceServiceStep);
        List<CommerceServiceStep> list = iCommerceServiceStepService.list(lqw);
        ExcelUtil<CommerceServiceStep> util = new ExcelUtil<CommerceServiceStep>(CommerceServiceStep.class);
        return util.exportExcel(list, "step");
    }

    /**
     * 获取工商服务步骤详细信息
     */
    @PreAuthorize("@ss.hasPermi('commerce:step:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iCommerceServiceStepService.getById(id));
    }

    /**
     * 新增工商服务步骤
     */
    @PreAuthorize("@ss.hasPermi('commerce:step:add')")
    @Log(title = "工商服务步骤", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommerceServiceStep commerceServiceStep) {
        return toAjax(iCommerceServiceStepService.save(commerceServiceStep) ? 1 : 0);
    }

    /**
     * 修改工商服务步骤
     */
    @ApiOperation("完成服务流程")
    @PreAuthorize("@ss.hasPermi('commerce:step:edit')")
    @Log(title = "工商服务步骤", businessType = BusinessType.UPDATE)
    @PutMapping
    @Transactional
    public AjaxResult edit(@RequestBody CommerceServiceStep commerceServiceStep) {
        Asserts.isTrue(StringUtils.isNotNull(commerceServiceStep.getOperatorUserId()), "请选择完成人");
        Asserts.isTrue(StringUtils.isNotNull(commerceServiceStep.getCompleteTime()), "请选择日期");
        CommerceServiceStep css = iCommerceServiceStepService.getById(commerceServiceStep.getId());
        Asserts.isTrue(commerceServiceService.checkServiceStatus(css.getCommerceServiceId()),"所选任务已终止，无法操作");
        Asserts.isTrue(css.getStepStatus() == 0, "该流程已完成");
//        LambdaUpdateWrapper<CommerceService> lqw = new LambdaUpdateWrapper<CommerceService>();
//        lqw.set(CommerceService::getTaskStatus,css.getStepName()).eq(CommerceService::getId,css.getCommerceServiceId());
//        boolean b = commerceServiceService.update(lqw);
//        Asserts.isTrue(b,"操作失败");
        commerceServiceStep.setStepStatus(1);
        boolean b = iCommerceServiceStepService.updateById(commerceServiceStep);
        Asserts.isTrue(b, "操作失败");
        commerceServiceService.checkEnd(css.getCommerceServiceId());
        return AjaxResult.success();
    }

    /**
     * 删除工商服务步骤
     */
    @PreAuthorize("@ss.hasPermi('commerce:step:remove')")
    @Log(title = "工商服务步骤", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCommerceServiceStepService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @ApiOperation("开始服务")
    @PreAuthorize("@ss.hasPermi('commerce:step:begin')")
    @GetMapping("/serviceDo/{commerceServiceId}")
    @ApiParam(name = "commerceServiceId", required = true, value = "工商服务id")
    public AjaxResult serviceDo(@PathVariable("commerceServiceId") Long commerceServiceId) {
        Asserts.isTrue(commerceServiceService.checkServiceStatus(commerceServiceId),"所选任务已终止，无法开始服务");
        AjaxResult ajax = iCommerceServiceStepService.add(commerceServiceId);
        return ajax;
    }

    @ApiOperation("获取服务流程列表")
    @GetMapping("/list/{commerceServiceId}")
    public AjaxResult getStepList(@PathVariable("commerceServiceId") Long commerceServiceId) {
        Asserts.isTrue(StringUtils.isNotNull(commerceServiceId), "请选择要执行的服务");
        CommerceServiceStep commerceServiceStep = new CommerceServiceStep();
        commerceServiceStep.setCommerceServiceId(commerceServiceId);
        List<CommerceServiceStepVo> list = iCommerceServiceStepService.getList(commerceServiceStep);

        if (list != null && list.size() > 0) {
            for (CommerceServiceStepVo commerceServiceStepVo : list) {
                List<CommerceStepFile> files = commerceStepFileService.list(new LambdaQueryWrapper<CommerceStepFile>().eq(CommerceStepFile::getStepId, commerceServiceStepVo.getId()));
                commerceServiceStepVo.setCommerceStepFileList(files);
            }
        }

        CommerceServiceVo csv = commerceServiceService.selectById(commerceServiceId);
        AjaxResult ajax = AjaxResult.success(list);
        ajax.put("commerceService", csv);
        return ajax;
    }

    @ApiOperation("获取附件")
    @GetMapping("/list/getStepFile/{stepId}")
    public AjaxResult getStepFile(@PathVariable("stepId") Long stepId) {
        Asserts.isTrue(StringUtils.isNotNull(stepId), "请选择服务");

        List<CommerceStepFile> list = commerceStepFileService.list(new LambdaQueryWrapper<CommerceStepFile>().eq(CommerceStepFile::getStepId, stepId));
        return AjaxResult.success(list);
    }

    @ApiOperation("附件上传")
    @PostMapping("/list/file")
    public AjaxResult uploadStepFile(Long stepId, MultipartFile[] files) {
        Asserts.isTrue(StringUtils.isNotNull(stepId), "请选择要执行的服务");

        try {
            for (MultipartFile file : files) {
                // 上传文件路径
                String filePath = RuoYiConfig.getUploadPath();
                // 上传并返回新文件名称
                String fileName = file.getOriginalFilename();
                String fileUrl = FileUploadUtils.upload(filePath, file).substring(Constants.RESOURCE_PREFIX.length());
                CommerceStepFile commerceStepFile = new CommerceStepFile();
                commerceStepFile.setStepId(stepId);
                commerceStepFile.setFileName(fileName);
                commerceStepFile.setFileUrl(fileUrl);
                commerceStepFile.setCreateTime(new Date());
                commerceStepFileService.save(commerceStepFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return AjaxResult.success();
    }

    @ApiOperation("附件预览")
    @GetMapping("/list/file/preview")
    public void fileDownload(String fileUrl, HttpServletResponse response) {
        try {
            String filePath = RuoYiConfig.getProfile() + fileUrl;
            File file = new File(filePath);
            OutputStream out = null;
            try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(file))) {
                byte[] bs = new byte[1024];
                int len;
                response.reset();
                URL u = new URL("file:///" + filePath);
                String contentType = u.openConnection().getContentType();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "inline;filename=" + file.getName());
                out = response.getOutputStream();
                while ((len = br.read(bs)) > 0) {
                    out.write(bs, 0, len);
                }
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("附件删除")
    @GetMapping("/list/deleteStepFile/{id}")
    public AjaxResult deleteStepFile(@PathVariable("id") Long id) {
        CommerceStepFile commerceStepFile = commerceStepFileService.getById(id);

        FileUtils.deleteFile(RuoYiConfig.getProfile() + commerceStepFile.getFileUrl());

        commerceStepFileService.removeById(id);
        return AjaxResult.success();
    }

    @ApiOperation("流程撤销")
    @PreAuthorize("@ss.hasPermi('commerce:step:revocation')")
    @GetMapping("/revocation/{stepId}")
    @Transactional
    public AjaxResult revocation(@PathVariable("stepId") Long stepId) {
        CommerceServiceStep commerceServiceStep = iCommerceServiceStepService.getById(stepId);
        Asserts.isTrue(commerceServiceService.checkServiceStatus(commerceServiceStep.getCommerceServiceId()),"所选任务已终止，无法操作");
        Asserts.isTrue(commerceServiceStep.getStepStatus() == 1, "该流程未完成，不能撤销");
        LambdaUpdateWrapper<CommerceService> lqw = new LambdaUpdateWrapper<CommerceService>();
        lqw.set(CommerceService::getStatus, StringDateUtil.TASK_TYPE_1).eq(CommerceService::getId, commerceServiceStep.getCommerceServiceId());
        boolean b = commerceServiceService.update(lqw);
        Asserts.isTrue(b, "操作失败");
        commerceServiceStep.setOperatorUserId(null);
        commerceServiceStep.setStepStatus(0);
        commerceServiceStep.setRemark("");
        return toAjax(iCommerceServiceStepService.updateById(commerceServiceStep) ? 1 : 0);
    }
}
