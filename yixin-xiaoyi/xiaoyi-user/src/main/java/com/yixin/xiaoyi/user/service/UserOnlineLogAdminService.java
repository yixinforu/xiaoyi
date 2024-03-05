package com.yixin.xiaoyi.user.service;

import com.yixin.xiaoyi.user.entity.UserOnlineLog;

import java.util.Collection;

/**
 * @author: huangzexin
 * @date: 2023/8/19 18:08
 */
public interface UserOnlineLogAdminService {

    public boolean insertBatch(Collection<UserOnlineLog> userOnlineLogs);
}
