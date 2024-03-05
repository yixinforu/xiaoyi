package com.yixin.xiaoyi.invite.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.invite.dao.UserInviteRecordFinder;
import com.yixin.xiaoyi.invite.dao.UserInviteCodeFinder;
import com.yixin.xiaoyi.invite.entity.UserInviteRecord;
import com.yixin.xiaoyi.invite.entity.UserInviteCode;
import com.yixin.xiaoyi.invite.model.dto.UserInviteRecordDTO;
import com.yixin.xiaoyi.invite.service.UserInviteRecordService;
import com.yixin.xiaoyi.user.dao.XyUserFinder;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.user.model.vo.EmpowerVO;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import com.yixin.xiaoyi.user.service.XyUserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: huangzexin
 * @date: 2023/9/24 11:19
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserInviteRecordServiceImpl implements UserInviteRecordService {

    private final UserInviteCodeFinder userInviteCodeFinder;
    private final UserInviteRecordFinder userInviteRecordFinder;
    private final XyUserAdminService xyUserAdminService;
    private final XyUserFinder xyUserFinder;
    @Value("${invite.inviteRecordsVip}")
    private Integer inviteRecordsVip ;
    @Value("${invite.inviteVipDays}")
    private Integer inviteVipDays ;

    @Override
    public int insertInviteRecord(Long inviteeUserId, String inviteCode) {
        UserInviteCode userInviteCode = userInviteCodeFinder.selectUserInviteCodeByInviteCode(inviteCode);
        if(ObjectUtil.isNull(userInviteCode)){
            log.error("邀请码无效：{} ",inviteCode);
            return -1;
        }
        UserInviteRecord userInviteRecord = new UserInviteRecord();
        userInviteRecord.setInviterUserId(userInviteCode.getUserId());
        userInviteRecord.setInviteeUserId(inviteeUserId);
        userInviteRecord.setInviteCode(inviteCode);
        return userInviteRecordFinder.insertInviteRecord(userInviteRecord);
    }

    @Override
    public List<UserInviteRecordDTO> selectCurrentInviteRecords() {
        List<UserInviteRecord> currentUserInviteRecords = userInviteRecordFinder.selectInviteRecordListByUserIdAndStatus(LoginHelper.getUserId(),"0");
        if(CollectionUtils.isEmpty(currentUserInviteRecords)){
            return new ArrayList<>();
        }
        List<Long> inverteeUserIds = currentUserInviteRecords.stream().map(UserInviteRecord::getInviteeUserId).collect(Collectors.toList());
        Map<Long,String> userIdAndMaskPhone = xyUserFinder.selectUserByIds(inverteeUserIds).stream().collect(Collectors.toMap(XyUser::getUserId,XyUser::getMaskPhone));
        List<UserInviteRecordDTO> userInviteRecordDTOS = new ArrayList<>();
        currentUserInviteRecords.stream().forEach(it->{
            userInviteRecordDTOS.add(UserInviteRecordDTO.builder()
                                    .inviteeMaskPhone(userIdAndMaskPhone.get(it.getInviteeUserId()))
                                    .registerTime(it.getCreateTime()).build());
        });
        return userInviteRecordDTOS;
    }

    @Override
    public int empowerByInviteRecords() {
        Long userId = LoginHelper.getUserId();
        //获取最近注册的N条邀请记录
        List<UserInviteRecord> currentUserInviteRecords = userInviteRecordFinder.selectInviteRecordListByUserIdAndStatus(userId,"0");
        List<UserInviteRecord> topInviteRecords = currentUserInviteRecords.stream().limit(inviteRecordsVip).collect(Collectors.toList());
        if(topInviteRecords.size()<inviteRecordsVip){
            log.error("用户：{} 邀请人数未达{} 人，授权VIP失败。",userId,inviteRecordsVip);
            throw new ServiceException(ErrorCode.EMPOWER_FAIL);
        }
        EmpowerVO empowerVO = new EmpowerVO();
        empowerVO.setUserId(userId);
        empowerVO.setVipDays(inviteVipDays);
        int empowerStatus = xyUserAdminService.increaseEmpower(empowerVO);
        if(empowerStatus<=0){
            log.error("用户:{} 授权失败。");
            throw new ServiceException(ErrorCode.EMPOWER_FAIL);
        }
        //将所N条邀请记录标记为已兑换状态
        for(UserInviteRecord userInviteRecord:topInviteRecords){
            userInviteRecord.setExchangedVip(1);
        }
        return userInviteRecordFinder.updateUserInviteRecords(topInviteRecords) ? topInviteRecords.size() : 0;
    }
}
