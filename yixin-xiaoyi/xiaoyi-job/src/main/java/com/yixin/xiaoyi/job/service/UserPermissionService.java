package com.yixin.xiaoyi.job.service;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.role.dao.XyUserRoleFinder;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.user.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/** 用户权限任务
 * @author: huangzexin
 * @date: 2023/9/2 17:08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final LockTemplate lockTemplate;
    private final XyUserRoleFinder xyUserRoleFinder;
    private final SysPermissionService permissionService;

    /**
     * 定期清除用户过期权限
     */
    @XxlJob("removeExpirePermissionsTask")
    public void removeExpirePermissionsTask(){
        // 分布式锁
        final LockInfo lockInfo = lockTemplate.lock("ExpirePermissionLock", 10000L, 5000L, RedissonLockExecutor.class);
        // 获取锁，锁超时，说明线程正在执行，直接结束本次任务
        if (lockInfo == null) {
            log.error("清除用户过期权限-获取锁超时，结束本次任务");
            XxlJobHelper.handleFail("清除用户过期权限-获取锁超时，结束本次任务");
            return;
        }
        try{
            //获取当前时间
            Date currentTime = new Date();
            //获取过期数据
            List<XyUserRole> expireUserRoles = xyUserRoleFinder.findExpireUserRoles(currentTime);
            if(CollectionUtils.isEmpty(expireUserRoles)){
                return;
            }
            List<Long> userIds = expireUserRoles.stream().map(XyUserRole::getUserId).collect(Collectors.toList());
            //删除数据库过期数据
            xyUserRoleFinder.deleteExpireUserRoles(currentTime);
            //删除缓存过期数据
            permissionService.deleteUserPermissionBatch(userIds);
            XxlJobHelper.handleSuccess("清除用户过期权限任务执行成功");
        }catch (Exception e){
            log.error("清除用户过期权限-----详细情况请查看日志：{}", e.getMessage());
            XxlJobHelper.handleFail("清除用户过期权限线程异常-------详细情况请查看日志");
            return;
        }finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }

    }
}
