package com.yixin.xiaoyi.web.controller.inner.user;

import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.entity.XyLogininfor;
import com.yixin.xiaoyi.user.service.XyLogininforAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统访问记录
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/user/logininfor")
public class XyLogininforController extends BaseController {

    private final XyLogininforAdminService logininforService;

    /**
     * 获取系统访问记录列表
     * @param xyLoginforJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyLogininfor> list(@RequestParam("xyLoginforJson") String xyLoginforJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return logininforService.selectPageLogininforList(JsonUtils.parseObject(xyLoginforJson,XyLogininfor.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }


    /**
     * 批量删除登录日志
     * @param infoIds 日志ids
     */
    @DeleteMapping("/{infoIds}")
    public R<Void> remove(@PathVariable("infoIds") Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    /**
     * 清理系统访问记录
     * @return
     */
    @DeleteMapping("/clean")
    public R<Void> clean() {
        logininforService.cleanLogininfor();
        return R.ok();
    }

    /**
     * 解锁用户
     * @param userName
     * @return
     */
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.SMS_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }

}
