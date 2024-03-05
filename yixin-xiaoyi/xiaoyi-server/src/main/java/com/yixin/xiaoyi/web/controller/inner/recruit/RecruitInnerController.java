package com.yixin.xiaoyi.web.controller.inner.recruit;


import cn.dev33.satoken.annotation.SaIgnore;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.excel.ExcelResult;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.common.utils.poi.ExcelUtil;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.listener.RecruitImportListener;
import com.yixin.xiaoyi.recruit.model.vo.RecruitAuditVO;
import com.yixin.xiaoyi.recruit.service.RecruitAdminService;
import com.yixin.xiaoyi.recruit.service.impl.RecruitAdminServiceImpl;
import com.yixin.xiaoyi.recruit.model.vo.RecruitImportVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: yixin
 * @date: 2023/3/9 17:12
 */
@RestController
@RequestMapping("/inner/recruit")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecruitInnerController extends BaseController {

    private final RecruitAdminService recruitAdminService;

    /**分页
     * 招聘信息表
     * @param searchKey 信息编号 / 岗位
     * @param nature 性质
     * @param releaseTime 发布时间
     * @param location 为准
     * @param auditStatus 审核状态
     * @return
     */
    @SaIgnore
    @GetMapping("/list")
    public TableDataInfo<Recruit> findJobInfoList(
        @RequestParam(value = "searchKey",required = false) String searchKey,
        @RequestParam(value="nature",required = false) String nature,
        @RequestParam(value = "releaseTime",required = false) Integer releaseTime,
        @RequestParam(value = "location",required = false) String location,
        @RequestParam(value = "audit") Boolean auditStatus,
        @RequestParam(value = "pageQuery") String pageQuery){
        return TableDataInfo.build(recruitAdminService.selectRecruitList(searchKey, nature, releaseTime, location,auditStatus, JsonUtils.parseObject(pageQuery,PageQuery.class)));
    }

    /**
     * 增加招聘信息（手动，默认未审核）
     * @param recruit
     * @return
     */
    @PostMapping
    @SaIgnore
    public R<Void> addRecruit(@Validated @RequestBody Recruit recruit){
        return toAjax(recruitAdminService.addRecruit(recruit));
    }

    /**
     * 编辑招聘信息
     * @param recruit
     * @return
     */
    @PutMapping
    public R<Void> editJobInfo(@RequestBody @Valid Recruit recruit){
        return toAjax(recruitAdminService.editRecruit(recruit));
    }

    /**
     * 删除招聘信息
     * @param recruitIds
     * @return
     */
    @DeleteMapping("/{recruitIds}")
    public R<Void> deleteJobInfo(@PathVariable("recruitIds") Long[] recruitIds){
        return toAjax(recruitAdminService.deleteRecruitByIds(recruitIds));
    }

    @PutMapping("/audit-ids")
    public R<Void> auditRecruitByIds(@RequestBody RecruitAuditVO recruitAuditVO){
        return toAjax(recruitAdminService.auditRecruitByIds(recruitAuditVO));
    }

    /**
     * 获取具体招聘信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Recruit> getJobInfo(@PathVariable(value = "id") Long id){
        return R.ok(recruitAdminService.getRecruit(id));
    }


    /**
     * 导出招聘信息
     * @param searchKey
     * @param nature
     * @param releaseTime
     * @param location
     * @param auditStatus
     * @return
     */
    @PostMapping("/export")
    public List<Recruit> export(
                       @RequestParam(value = "search_key",required = false) String searchKey,
                       @RequestParam(value="nature",required = false) String nature,
                       @RequestParam(value = "release_time",required = false) Integer releaseTime,
                       @RequestParam(value = "location",required = false) String location,
                       @RequestParam(value = "audit") Boolean auditStatus)
    {
        return recruitAdminService.selectRecruitList(searchKey, nature, releaseTime, location,auditStatus,new PageQuery()).getRecords();
    }

    /**
     * 导入数据
     *
     * @param file   导入文件
     */
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<RecruitImportVo> result = ExcelUtil.importExcel(file.getInputStream(), RecruitImportVo.class, new RecruitImportListener());
        return R.ok(result.getAnalysis());
    }


    @PutMapping("/newCount")
    public R<Void> updateCount(@RequestBody NewCount newCount) {
        return toAjax(recruitAdminService.updateCount(newCount));
    }

    @GetMapping("/newCount")
    public R<NewCount> selectNewCount() {
        return R.ok(recruitAdminService.selectNewCount());
    }



}
