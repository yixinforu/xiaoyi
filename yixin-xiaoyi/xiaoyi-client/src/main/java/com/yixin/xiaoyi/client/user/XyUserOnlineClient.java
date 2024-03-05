package com.yixin.xiaoyi.client.user;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.user.entity.XyUserOnline;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: huangzexin
 * @date: 2023/8/5 22:06
 */
@FeignClient(
    name = "xy-user-online",
    contextId = "xy-user-online-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyUserOnlineClient {

    /**
     *
     * 获取在线用户监控列表
     * @param ipaddr   IP地址
     * @param phone  手机号
     * @return
     */
    @GetMapping("/inner/user/online/list")
    public TableDataInfo<XyUserOnline> list(@RequestParam(value = "ipaddr",required = false)String ipaddr, @RequestParam(value = "phone",required = false) String phone) ;

    /**
     * 强退用户
     * @param tokenId token值
     * @return
     */
    @DeleteMapping("/inner/user/online/{tokenId}")
    public R<Void> forceLogout(@PathVariable("tokenId") String tokenId) ;
}
