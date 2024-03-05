package com.yixin.xiaoyi.invite.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.config.XiaoYiConfig;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.invite.dao.UserInviteCodeFinder;
import com.yixin.xiaoyi.invite.entity.UserInviteCode;
import com.yixin.xiaoyi.invite.service.UserInviteCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:19
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserInviteCodeServiceImpl implements UserInviteCodeService {

    private final UserInviteCodeFinder userInviteCodeFinder;

    @Override
    public String generateInviteCode() {
        Long userId = LoginHelper.getUserId();
        UserInviteCode userInviteCode = userInviteCodeFinder.selectUserInviteCodeByUserId(userId);
        if(ObjectUtil.isNull(userInviteCode)){
            log.error("用户编号：{},无对应邀请码",userId);
            throw new ServiceException(ErrorCode.USER_NOT_INVITE_CODE);
        }
        StringBuilder inviteLink = new StringBuilder();
        inviteLink.append(XiaoYiConfig.getLink())
            .append("?inviteCode=")
            .append(userInviteCode.getInviteCode());
        return inviteLink.toString();
    }

}
