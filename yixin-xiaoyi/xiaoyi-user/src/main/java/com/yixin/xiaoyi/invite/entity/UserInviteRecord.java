package com.yixin.xiaoyi.invite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:13
 */
@Data
@NoArgsConstructor
@TableName("user_invite_record")
public class UserInviteRecord extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;


    /**
     * 邀请人
     */
    private Long inviterUserId;

    /**
     * 被邀请人
     */
    private Long inviteeUserId;


    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 是否兑换过VIP 0-未兑换 1-已兑换
     */
    private Integer exchangedVip;
}
