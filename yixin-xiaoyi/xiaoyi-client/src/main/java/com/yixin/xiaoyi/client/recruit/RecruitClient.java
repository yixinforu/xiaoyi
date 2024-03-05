package com.yixin.xiaoyi.client.recruit;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.model.vo.RecruitAuditVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/9 22:36
 */
@FeignClient(
    name = "xy-recruit",
    contextId = "xy-recruit-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface RecruitClient {


    /**
     *
     * 招聘信息表
     * @param searchKey 信息编号 / 岗位
     * @param nature 性质
     * @param releaseTime 发布时间
     * @param location 为准
     * @param auditStatus 审核状态
     * @param pageQuery 分页
     * @return
     */
    @GetMapping("/inner/recruit/list")
    public TableDataInfo<Recruit> findJobInfoList(
        @RequestParam(value = "searchKey",required = false) String searchKey,
        @RequestParam(value="nature",required = false) String nature,
        @RequestParam(value = "releaseTime",required = false) Integer releaseTime,
        @RequestParam(value = "location",required = false) String location,
        @RequestParam(value = "audit") Boolean auditStatus,
        @RequestParam(value = "pageQuery") String pageQuery);


    /**
     * 增加招聘信息（手动，默认未审核）
     * @param recruit
     * @return
     */
    @PostMapping("/inner/recruit")
    public R<Void> addRecruit(@Validated @RequestBody Recruit recruit);


    /**
     * 编辑招聘信息
     * @param recruit
     * @return
     */
    @PutMapping("/inner/recruit")
    public R<Void> editJobInfo(@RequestBody @Valid Recruit recruit);


    /**
     * 删除招聘信息
     * @param recruitIds
     * @return
     */
    @DeleteMapping("/inner/recruit/{recruitIds}")
    public R<Void> deleteJobInfo(@PathVariable("recruitIds") Long[] recruitIds);


    @PutMapping("/inner/recruit/audit-ids")
    public R<Void> auditRecruitByIds(@RequestBody RecruitAuditVO recruitAuditVO);

    /**
     * 获取具体招聘信息
     * @param id
     * @return
     */
    @GetMapping("/inner/recruit/{id}")
    public R<Recruit> getJobInfo(@PathVariable(value = "id") Long id);



    /**
     * 导出
     * @param searchKey
     * @param nature
     * @param releaseTime
     * @param location
     * @param auditStatus
     * @return
     */
    @PostMapping("/inner/recruit/export")
    public List<Recruit> export(
                                @RequestParam(value = "search_key",required = false) String searchKey,
                                @RequestParam(value="nature",required = false) String nature,
                                @RequestParam(value = "release_time",required = false) Integer releaseTime,
                                @RequestParam(value = "location",required = false) String location,
                                @RequestParam(value = "audit") Boolean auditStatus);


    /**
     * 导入数据
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/inner/recruit/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData( @RequestPart("file") MultipartFile file) throws Exception ;

    /**
     * 更新计数
     * @param newCount
     * @return
     */
    @PutMapping("/inner/recruit/newCount")
    public R<Void> updateCount(@RequestBody NewCount newCount);

    /**
     * 获取计数
     * @return
     */
    @GetMapping("/inner/recruit/newCount")
    public R<NewCount> selectNewCount() ;
}
