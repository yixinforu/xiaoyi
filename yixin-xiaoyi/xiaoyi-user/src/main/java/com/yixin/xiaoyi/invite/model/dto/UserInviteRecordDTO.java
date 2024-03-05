package com.yixin.xiaoyi.invite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/9/24 17:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInviteRecordDTO {

    /**
     * 被邀请人脱敏手机号
     */
    private String inviteeMaskPhone;

    /**
     * 注册时间
     */
    private Date registerTime;
}
