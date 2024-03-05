package com.yixin.xiaoyi.client.user;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.user.entity.XyLogininfor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: huangzexin
 * @date: 2023/8/5 21:27
 */
@FeignClient(
    name = "xy-loginfor",
    contextId = "xy-loginfor-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyLogininforClient {

    /**
     * 获取系统访问记录列表
     * @param xyLoginforJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/user/logininfor/list")
    public TableDataInfo<XyLogininfor> list(@RequestParam("xyLoginforJson") String xyLoginforJson, @RequestParam("pageQueryJson") String pageQueryJson) ;

    /**
     *
     * 批量删除登录日志
     * @param infoIds 日志ids
     * @return
     */
    @DeleteMapping("/inner/user/logininfor/{infoIds}")
    public R<Void> remove(@PathVariable("infoIds") Long[] infoIds) ;

    /**
     *
     * 清理系统访问记录
     * @return
     */
    @DeleteMapping("/inner/user/logininfor/clean")
    public R<Void> clean() ;


    /**
     * 解锁
     * @param userName
     * @return
     */
    @GetMapping("/inner/user/logininfor/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) ;

}
