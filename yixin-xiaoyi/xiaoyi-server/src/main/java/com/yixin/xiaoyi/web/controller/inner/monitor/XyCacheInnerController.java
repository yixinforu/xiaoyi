package com.yixin.xiaoyi.web.controller.inner.monitor;

import cn.hutool.core.collection.CollUtil;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.constant.CacheNames;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.CacheUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.system.entity.XyCache;
import lombok.RequiredArgsConstructor;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存监控
 *
 * @author admin
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/monitor/cache")
public class XyCacheInnerController {

    private final RedissonConnectionFactory connectionFactory;

    private final static List<XyCache> CACHES = new ArrayList<>();

    static {
        CACHES.add(new XyCache(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        CACHES.add(new XyCache(CacheConstants.PERMISSION,"用户权限信息"));
        CACHES.add(new XyCache(CacheConstants.ONLINE_TOKEN_KEY, "在线用户"));
        CACHES.add(new XyCache(CacheNames.SYS_CONFIG, "配置信息"));
        CACHES.add(new XyCache(CacheNames.SYS_DICT, "数据字典"));
        CACHES.add(new XyCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        CACHES.add(new XyCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        CACHES.add(new XyCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        CACHES.add(new XyCache(CacheConstants.SMS_ERR_CNT_KEY, "验证码错误次数"));
        CACHES.add(new XyCache(CacheNames.SESSION_CONFIG_ID, "会话配置ID"));
        CACHES.add(new XyCache(CacheNames.SESSION_CONFIG_CODE, "会话配置CODE"));
        CACHES.add(new XyCache(CacheNames.RES_LIB_TEMP, "资源缓存列表"));
    }

    /**
     * 获取缓存监控列表
     * @return
     * @throws Exception
     */
    @GetMapping()
    public R<Map<String, Object>> getInfo() throws Exception {
        RedisConnection connection = connectionFactory.getConnection();
        Properties info = connection.info();
        Properties commandStats = connection.info("commandstats");
        Long dbSize = connection.dbSize();

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return R.ok(result);
    }

    /**
     * 获取缓存监控缓存名列表
     * @return
     */
    @GetMapping("/getNames")
    public R<List<XyCache>> cache() {
        return R.ok(CACHES);
    }

    /**
     * 获取缓存监控Key列表
     *
     * @param cacheName 缓存名
     */
    @GetMapping("/getKeys/{cacheName}")
    public R<Collection<String>> getCacheKeys(@PathVariable String cacheName) {
        Collection<String> cacheKeys = new HashSet<>(0);
        if (isCacheNames(cacheName)) {
            Set<Object> keys = CacheUtils.keys(cacheName);
            if (CollUtil.isNotEmpty(keys)) {
                cacheKeys = keys.stream().map(Object::toString).collect(Collectors.toList());
            }
        } else {
            cacheKeys = RedisUtils.keys(cacheName + "*");
        }
        return R.ok(cacheKeys);
    }

    /**
     * 获取缓存监控缓存值详情
     *
     * @param cacheName 缓存名
     * @param cacheKey  缓存key
     */
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public R<XyCache> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        Object cacheValue;
        if (isCacheNames(cacheName)) {
            cacheValue = CacheUtils.get(cacheName, cacheKey);
        } else {
            cacheValue = RedisUtils.getCacheObject(cacheKey);
        }
        XyCache xyCache = new XyCache(cacheName, cacheKey, JsonUtils.toJsonString(cacheValue));
        return R.ok(xyCache);
    }

    /**
     * 清理缓存监控缓存名
     *
     * @param cacheName 缓存名
     */
    @DeleteMapping("/clearCacheName/{cacheName}")
    public R<Void> clearCacheName(@PathVariable String cacheName) {
        if (isCacheNames(cacheName)) {
            CacheUtils.clear(cacheName);
        } else {
            RedisUtils.deleteKeys(cacheName + "*");
        }
        return R.ok();
    }

    /**
     * 清理缓存监控Key
     *
     * @param cacheKey key名
     */
    @DeleteMapping("/clearCacheKey/{cacheName}/{cacheKey}")
    public R<Void> clearCacheKey(@PathVariable String cacheName, @PathVariable String cacheKey) {
        if (isCacheNames(cacheName)) {
            CacheUtils.evict(cacheName, cacheKey);
        } else {
            RedisUtils.deleteObject(cacheKey);
        }
        return R.ok();
    }

    /**
     * 清理全部缓存监控
     * @return
     */
    @DeleteMapping("/clearCacheAll")
    public R<Void> clearCacheAll() {
        RedisUtils.deleteKeys("*");
        return R.ok();
    }

    private boolean isCacheNames(String cacheName) {
        return !StringUtils.contains(cacheName, ":");
    }
}
