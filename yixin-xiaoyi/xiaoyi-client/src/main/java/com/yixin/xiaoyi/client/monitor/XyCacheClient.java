package com.yixin.xiaoyi.client.monitor;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.system.entity.XyCache;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/5 21:21
 */
@FeignClient(
    name = "xy-cache",
    contextId = "xy-cache-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyCacheClient {

    /**
     *
     *获取缓存监控列表
     * @return
     */
    @GetMapping("/inner/monitor/cache")
    public R<Map<String, Object>> getInfo();

    /**
     *
     * 获取缓存监控缓存名列表
     * @return
     */
    @GetMapping("/inner/monitor/cache/getNames")
    public R<List<XyCache>> cache();

    /**
     * 获取缓存监控Key列表
     *
     * @param cacheName 缓存名
     */
    @GetMapping("/inner/monitor/cache/getKeys/{cacheName}")
    public R<Collection<String>> getCacheKeys(@PathVariable("cacheName") String cacheName);

    /**
     * 获取缓存监控缓存值详情
     * @param cacheName 缓存名
     * @param cacheKey  缓存key
     */
    @GetMapping("/inner/monitor/cache/getValue/{cacheName}/{cacheKey}")
    public R<XyCache> getCacheValue(@PathVariable("cacheName") String cacheName, @PathVariable("cacheKey") String cacheKey);

    /**
     * 清理缓存监控缓存名
     *
     * @param cacheName 缓存名
     */
    @DeleteMapping("/inner/monitor/cache/clearCacheName/{cacheName}")
    public R<Void> clearCacheName(@PathVariable("cacheName") String cacheName);

    /**
     * 清理缓存监控Key
     * @param cacheKey key名
     */
    @DeleteMapping("/inner/monitor/cache/clearCacheKey/{cacheName}/{cacheKey}")
    public R<Void> clearCacheKey(@PathVariable("cacheName") String cacheName, @PathVariable("cacheKey") String cacheKey);

    /**
     * 清理全部缓存监控
     */
    @DeleteMapping("/clearCacheAll")
    public R<Void> clearCacheAll();
}
