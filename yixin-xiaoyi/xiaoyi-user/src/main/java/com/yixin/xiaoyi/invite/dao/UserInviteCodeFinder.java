package com.yixin.xiaoyi.invite.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yixin.xiaoyi.invite.entity.UserInviteCode;
import com.yixin.xiaoyi.invite.dao.mapper.UserInviteCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:26
 */
@Repository
@RequiredArgsConstructor
public class UserInviteCodeFinder {

    private final UserInviteCodeMapper userInviteCodeMapper;



    public int insert(UserInviteCode userInviteCode){
        return userInviteCodeMapper.insert(userInviteCode);
    }


    /**
     * 通过用户编号获取记录
     * @param userId
     * @return
     */
    public UserInviteCode selectUserInviteCodeByUserId(Long userId){
        return userInviteCodeMapper.selectOne(new LambdaQueryWrapper<UserInviteCode>().eq(UserInviteCode::getUserId,userId));
    }

    /**
     * 通过邀请码获取记录
     * @param inviteCode
     * @return
     */
    public UserInviteCode selectUserInviteCodeByInviteCode(String inviteCode){
        return userInviteCodeMapper.selectOne(new LambdaQueryWrapper<UserInviteCode>().eq(UserInviteCode::getInviteCode,inviteCode));
    }
}
