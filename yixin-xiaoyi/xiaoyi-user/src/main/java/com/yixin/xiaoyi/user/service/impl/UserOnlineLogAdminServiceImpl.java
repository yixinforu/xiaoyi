package com.yixin.xiaoyi.user.service.impl;

import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import com.yixin.xiaoyi.user.dao.UserOnlineLogFinder;
import com.yixin.xiaoyi.user.service.UserOnlineLogAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author: huangzexin
 * @date: 2023/8/19 18:09
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserOnlineLogAdminServiceImpl implements UserOnlineLogAdminService {

    private final UserOnlineLogFinder userOnlineLogFinder;

    @Override
    public boolean insertBatch(Collection<UserOnlineLog> userOnlineLogs){
        return userOnlineLogFinder.insertBatch(userOnlineLogs);
    }
}
