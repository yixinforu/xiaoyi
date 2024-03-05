package com.yixin.xiaoyi.invite.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yixin.xiaoyi.invite.entity.UserInviteRecord;
import com.yixin.xiaoyi.invite.dao.mapper.UserInviteRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:26
 */
@Repository
@RequiredArgsConstructor
public class UserInviteRecordFinder {

    private final UserInviteRecordMapper userInviteRecordMapper;

    /**
     * 新增邀请记录
     * @param userInviteRecord
     * @return
     */
    public int insertInviteRecord(UserInviteRecord userInviteRecord){
        return userInviteRecordMapper.insert(userInviteRecord);
    }


    /**
     * 通过邀请人和兑换状态获取数据
     * @param inviterUserId
     * @param exchangeStatus
     * @return
     */
    public List<UserInviteRecord> selectInviteRecordListByUserIdAndStatus(Long inviterUserId,String exchangeStatus){
        return userInviteRecordMapper.selectList(new LambdaQueryWrapper<UserInviteRecord>()
            .eq(UserInviteRecord::getInviterUserId, inviterUserId)
            .eq(UserInviteRecord::getExchangedVip, exchangeStatus).orderByDesc(UserInviteRecord::getCreateTime));
    }

    public boolean updateUserInviteRecords(List<UserInviteRecord> userInviteRecords){
        return userInviteRecordMapper.updateBatchById(userInviteRecords);
    }


}
