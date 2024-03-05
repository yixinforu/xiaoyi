package com.yixin.xiaoyi.web.controller.inner.user;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.module.user.dto.UserOnlineDTO;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.common.utils.StreamUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.entity.XyUserOnline;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author admin
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/user/online")
public class XyUserOnlineController extends BaseController {


    /**
     * 获取在线用户监控列表
     *
     * @param ipaddr   IP地址
     * @param phone 用户名
     */
    @GetMapping("/list")
    public TableDataInfo<XyUserOnline> list(@RequestParam(value = "ipaddr",required = false) String ipaddr, @RequestParam(value = "phone",required = false) String phone) {
        // 获取所有未过期的 token
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        List<UserOnlineDTO> userOnlineDTOList = new ArrayList<>();
        for (String key : keys) {
            String token = key.replace(CacheConstants.LOGIN_TOKEN_KEY, "");
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < -1) {
                continue;
            }
            userOnlineDTOList.add(RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token));
        }
        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(phone)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                StringUtils.equals(ipaddr, userOnline.getIpaddr()) &&
                    StringUtils.equals(phone, EncryptionUtil.decryptKey(userOnline.getEncryptedPhone(), UserConfig.getPhoneSecret()))
            );
        } else if (StringUtils.isNotEmpty(ipaddr)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                StringUtils.equals(ipaddr, userOnline.getIpaddr())
            );
        } else if (StringUtils.isNotEmpty(phone)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                StringUtils.equals(phone, EncryptionUtil.decryptKey(userOnline.getEncryptedPhone(),UserConfig.getPhoneSecret()))
            );
        }
        Collections.reverse(userOnlineDTOList);
        userOnlineDTOList.removeAll(Collections.singleton(null));
        List<XyUserOnline> userOnlineList = BeanUtil.copyToList(userOnlineDTOList, XyUserOnline.class);
        userOnlineList.stream().forEach(userOnline -> {userOnline.setPhone(EncryptionUtil.decryptKey(userOnline.getEncryptedPhone(),UserConfig.getPhoneSecret()));});
        return TableDataInfo.build(userOnlineList);
    }

    /**
     * 强退用户
     *
     * @param tokenId token值
     */
    @DeleteMapping("/{tokenId}")
    public R<Void> forceLogout(@PathVariable("tokenId") String tokenId) {
        try {
            StpUtil.kickoutByTokenValue(tokenId);
        } catch (NotLoginException ignored) {
        }
        return R.ok();
    }
}
