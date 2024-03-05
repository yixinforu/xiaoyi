package com.yixin.xiaoyi.job.service;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.service.UserOnlineLogAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/** 用户在线日志任务
 * @author: huangzexin
 * @date: 2023/8/17 23:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOnlineLogService {


    private final LockTemplate lockTemplate;
    private final UserOnlineLogAdminService userOnlineLogAdminService;


    /**
     * 持久化存储缓存的在线日志
     */
    @XxlJob("persistenceLogTask")
    public void persistenceLogTask(){
        // 分布式锁
        final LockInfo lockInfo = lockTemplate.lock("OnlineLogLock", 10000L, 5000L, RedissonLockExecutor.class);
        // 获取锁，锁超时，说明线程正在执行，直接结束本次任务
        if (lockInfo == null) {
            log.error("持久化在线日志-获取锁超时，结束本次任务");
            XxlJobHelper.handleFail("持久化在线日志-获取锁超时，结束本次任务");
            return;
        }
        try{
            //获取缓存在线日志
           Collection<String> cacheKeys = RedisUtils.keys(CacheConstants.CLIENT_ONLINE_LOG+ "*");
           List<UserOnlineLog> userOnlineLogs = new ArrayList<>();
           for(String key:cacheKeys){
                UserOnlineLog userOnlineLog = RedisUtils.getCacheObject(key);
                userOnlineLog.setLastRequestTime(new Date(userOnlineLog.getCurrentRequestTime()));
                userOnlineLogs.add(userOnlineLog);
           }
           //持久化存储
            userOnlineLogAdminService.insertBatch(userOnlineLogs);
           //清空缓存所对应的日志
            RedisUtils.deleteKeys(CacheConstants.CLIENT_ONLINE_LOG+ "*");
            XxlJobHelper.handleSuccess("持久化在线日志任务执行成功");
        }catch (Exception e){
            log.error("持久化在线日志-----详细情况请查看日志：{}", e.getMessage());
            XxlJobHelper.handleFail("持久化在线日志线程异常-------详细情况请查看日志");
            return;
        }finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }

    }
}
