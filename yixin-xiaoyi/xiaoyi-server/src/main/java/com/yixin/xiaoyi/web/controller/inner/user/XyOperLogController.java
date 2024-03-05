package com.yixin.xiaoyi.web.controller.inner.user;

import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.user.entity.XyOperLog;
import com.yixin.xiaoyi.user.service.XyOperLogAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志记录
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/user/operlog")
public class XyOperLogController extends BaseController {

    private final XyOperLogAdminService operLogService;

    /**
     * 获取操作日志记录列表
     * @param xyOperLogJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyOperLog> list(@RequestParam("xyOperLogJson") String xyOperLogJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return operLogService.selectPageOperLogList(JsonUtils.parseObject(xyOperLogJson,XyOperLog.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }


    /**
     * 批量删除操作日志记录
     * @param operIds 日志ids
     */
    @DeleteMapping("/{operIds}")
    public R<Void> remove(@PathVariable("operIds") Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清理操作日志记录
     * @return
     */
    @DeleteMapping("/clean")
    public R<Void> clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }
}
