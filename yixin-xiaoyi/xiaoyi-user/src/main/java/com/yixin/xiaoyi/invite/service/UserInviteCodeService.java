package com.yixin.xiaoyi.invite.service;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:19
 */
public interface UserInviteCodeService {


    /**
     * 获取当前用户的专属邀请链接
     * @return
     */
    public String generateInviteCode();



}
