package com.yixin.xiaoyi.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.config.SmsConfig;
import com.yixin.xiaoyi.common.config.XiaoYiConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.domain.model.LoginUser;
import com.yixin.xiaoyi.common.core.domain.model.UserPermission;
import com.yixin.xiaoyi.common.core.domain.model.UserRoleMenuPermission;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.ServletUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.common.utils.sign.Base64;
import com.yixin.xiaoyi.common.utils.sign.Md5Utils;
import com.yixin.xiaoyi.sms.model.vo.SmsVO;
import com.yixin.xiaoyi.sms.service.SmsService;
import com.yixin.xiaoyi.user.model.dto.UserInfoDTO;
import com.yixin.xiaoyi.user.service.SysPermissionService;
import com.yixin.xiaoyi.user.service.XyUserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author: huangzexin
 * @date: 2023/9/21 14:16
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class XyUserApiServiceImpl  implements XyUserApiService {

    private final SysPermissionService permissionService;
    private final SmsService smsService;
    private final XiaoYiConfig xiaoYiConfig;
    private final String vipKey = "vip";


    @Override
    public UserInfoDTO getUserInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        //获取未过期的权限
        List<UserRoleMenuPermission> userRoleMenuPermissions = getNotExpirePermission();
        List<String> menuPermissions = new ArrayList<>();
        List<String> rolePermissions = new ArrayList<>();
        for(UserRoleMenuPermission userRoleMenuPermission:userRoleMenuPermissions){
            menuPermissions.addAll(userRoleMenuPermission.getMenuPermission());
            rolePermissions.add(userRoleMenuPermission.getRoleKey());
        }
        Date expireTime = rolePermissions.contains(vipKey) ?
            userRoleMenuPermissions.stream().filter(it->vipKey.equals(it.getRoleKey())).findFirst().get().getExpireTime() : null;
        return UserInfoDTO.builder()
            .userId(loginUser.getUserId())
            .maskedPhone(loginUser.getMaskPhone())
            .permissions(menuPermissions)
            .vipStatus(rolePermissions.contains(vipKey)?"1":"0")
            .expireTime(expireTime).build();
    }

    /**
     * 发送短信
     * @param smsVO
     * @return
     */
    @Override
    public void sendSmsCode(SmsVO smsVO) {
        //签名和重发校验
        if(!generateSignature(smsVO.getPhone(),smsVO.getTimestamp()).equals(smsVO.getSignature()) || Math.abs(System.currentTimeMillis() - smsVO.getTimestamp()) > 10000 ){
            throw new ServiceException(ErrorCode.VIOLATION_CALL);
        }
        //短信调用次数限制校验
        Integer smsCount = RedisUtils.getCacheObject(CacheConstants.SMS_PHONE_LIMIT_KEY +smsVO.getPhone());
        if(ObjectUtil.isNotNull(smsCount) && SmsConfig.getMaxSmsCount().equals(smsCount)){
            throw new ServiceException(ErrorCode.SMS_PHONE_REPEAT_MAX_COUNT);
        }
        //IP调用次数限制（防刷手机号）
        final String ip = ServletUtils.getClientIP(ServletUtils.getRequest());
        Integer smsIpCount = RedisUtils.getCacheObject(CacheConstants.SMS_IP_LIMIT_KEY+ip);
        if(ObjectUtil.isNotNull(smsCount) && SmsConfig.getMaxIpSmsCount().equals(smsIpCount)){
            throw new ServiceException(ErrorCode.SMS_IP_REPEAT_MAX_COUNT);
        }
        String phone = smsVO.getPhone();
        //验证码
        String code = generateCode();
        //将验证码进行缓存，有效期10分钟
        RedisUtils.setCacheObject(CacheConstants.SMS_LOGIN_CODE_KEY +phone, code, Duration.ofMinutes(10));
        //调用次数计算
        limitSmsCount(smsCount,smsIpCount,phone,ip);
        //短信发送(异步）
        smsService.asyncSendSmsMessage(phone,code);
    }


    /**
     *  短信调用次数计算
     * @param smsCount
     * @param smsIpCount
     * @param phone
     * @param ip
     */
    public void limitSmsCount(Integer smsCount,Integer smsIpCount,String phone,String ip){
        // 限制1小时内手机最大调用次数
        if (ObjectUtil.isNull(smsCount)) {
            RedisUtils.setCacheObject(CacheConstants.SMS_PHONE_LIMIT_KEY + phone, 1, Duration.ofHours(1));
        } else {
            RedisUtils.setCacheObject(CacheConstants.SMS_PHONE_LIMIT_KEY + phone, smsCount + 1, true);
        }
        // 限制1小时内IP最大调用次数
        if (ObjectUtil.isNull(smsIpCount)) {
            RedisUtils.setCacheObject(CacheConstants.SMS_IP_LIMIT_KEY + ip, 1, Duration.ofHours(1));
        } else {
            RedisUtils.setCacheObject(CacheConstants.SMS_IP_LIMIT_KEY + ip, smsIpCount + 1, true);
        }
    }

    /**
     * 生成验证码
     * @return
     */
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    /**
     * 生成签名
     * @param phone
     * @param timestamp
     * @return
     */
    public String generateSignature(String phone,Long timestamp){
        StringBuilder content = new StringBuilder();
        content.append("phone=").append(phone).append("&")
            .append("timestamp=").append(timestamp).append("&")
            .append("secret=").append(xiaoYiConfig.getSignSecret());
        //先MD5加密 再进行Base64加密
        String signature = Base64.encode(Md5Utils.hash(content.toString()).getBytes(StandardCharsets.UTF_8));
        return signature;
    }


    private List<UserRoleMenuPermission> getNotExpirePermission(){
        UserPermission userPermission = permissionService.getUserPermission(StpUtil.getLoginIdAsLong());
        //获取未过期的权限
        List<UserRoleMenuPermission> userRoleMenuPermissions = userPermission.getUserRoleMenuPermissions()
            .stream().filter(permission -> ObjectUtil.isNull(permission.getExpireTime()) || permission.getExpireTime().compareTo(new Date())>0).collect(Collectors.toList());
        return userRoleMenuPermissions;
    }
}
