package com.yixin.xiaoyi.invite.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:06
 */
@Data
@NoArgsConstructor
@TableName("user_invite_code")
public class UserInviteCode extends BaseEntity {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 邀请码
     */
    private String inviteCode;
}
