package com.yixin.xiaoyi.web.controller.api.invite;

import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.invite.entity.UserInviteRecord;
import com.yixin.xiaoyi.invite.model.dto.UserInviteRecordDTO;
import com.yixin.xiaoyi.invite.service.UserInviteCodeService;
import com.yixin.xiaoyi.invite.service.UserInviteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: huangzexin
 * @date: 2023/9/24 14:05
 */
@RestController
@RequestMapping("/c/invite")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInviteApiController extends BaseController {

    private final UserInviteCodeService userInviteCodeService;
    private final UserInviteRecordService userInviteRecordService;


    /**
     * 获取邀请码
     * @return
     */
    @GetMapping("/code")
    public R<String> generateInviteCode(){
        return R.ok("操作成功",userInviteCodeService.generateInviteCode());
    }

    /**
     * 获取当前用户的邀请记录
     * @return
     */
    @GetMapping("/records")
    public TableDataInfo<UserInviteRecordDTO> selectCurrentUserInviteRecords(){
        return TableDataInfo.build(userInviteRecordService.selectCurrentInviteRecords());
    }

    /**
     * 兑换vip （在原来基础进行叠加）
     * @return
     */
    @PostMapping("/empower")
    public R<Void> empowerByInviteRecords(){
        return toAjax(userInviteRecordService.empowerByInviteRecords());
    }
}
