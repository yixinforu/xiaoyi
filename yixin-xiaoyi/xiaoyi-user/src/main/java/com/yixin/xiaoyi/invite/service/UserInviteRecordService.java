package com.yixin.xiaoyi.invite.service;

import com.yixin.xiaoyi.invite.model.dto.UserInviteRecordDTO;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:19
 */
public interface UserInviteRecordService {
    /**
     * 新增邀请记录
     * @param inviteeUserId 被邀请人用户id
     * @param inviteCode 邀请码
     * @return
     */
    public int insertInviteRecord(Long inviteeUserId,String inviteCode);


    /**
     * 获取当前用户的邀请记录
     * @return
     */
    public List<UserInviteRecordDTO> selectCurrentInviteRecords();


    /**
     * 通过邀请记录近授权VIP
     * @return
     */
    public int empowerByInviteRecords();


}
