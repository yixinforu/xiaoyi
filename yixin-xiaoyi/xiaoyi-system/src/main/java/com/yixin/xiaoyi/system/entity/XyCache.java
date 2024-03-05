package com.yixin.xiaoyi.system.entity;

import com.yixin.xiaoyi.common.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存信息
 *
 * @author admin
 */
@Data
@NoArgsConstructor
public class XyCache {

    /**
     * 缓存名称
     */
    private String cacheName = "";

    /**
     * 缓存键名
     */
    private String cacheKey = "";

    /**
     * 缓存内容
     */
    private String cacheValue = "";

    /**
     * 备注
     */
    private String remark = "";

    public XyCache(String cacheName, String remark) {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public XyCache(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

}
