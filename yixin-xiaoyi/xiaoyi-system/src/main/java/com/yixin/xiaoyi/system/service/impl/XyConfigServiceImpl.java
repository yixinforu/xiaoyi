package com.yixin.xiaoyi.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.CacheNames;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.core.service.ConfigService;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.CacheUtils;
import com.yixin.xiaoyi.common.utils.spring.SpringUtils;
import com.yixin.xiaoyi.system.dao.XyConfigFinder;
import com.yixin.xiaoyi.system.entity.XyConfig;
import com.yixin.xiaoyi.system.service.IXyConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 参数配置 服务层实现
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class XyConfigServiceImpl implements IXyConfigService, ConfigService {

    private final XyConfigFinder xyConfigFinder;

    @Override
    public TableDataInfo<XyConfig> selectPageConfigList(XyConfig config, PageQuery pageQuery) {
        Page<XyConfig> page = xyConfigFinder.selectPageConfigList(config, pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DS("master")
    public XyConfig selectConfigById(Long configId) {
        return xyConfigFinder.selectById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#configKey")
    @Override
    public String selectConfigByKey(String configKey) {
        XyConfig retConfig = xyConfigFinder.selectConfigByKey(configKey);
        if (ObjectUtil.isNotNull(retConfig)) {
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = SpringUtils.getAopProxy(this).selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<XyConfig> selectConfigList(XyConfig config) {
        return xyConfigFinder.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.configKey")
    @Override
    public String insertConfig(XyConfig config) {
        int row = xyConfigFinder.insert(config);
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.configKey")
    @Override
    public String updateConfig(XyConfig config) {
        int row = 0;
        if (config.getConfigId() != null) {
            row = xyConfigFinder.updateById(config);
        } else {
            row = xyConfigFinder.updateByKey(config);
        }
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            XyConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getConfigKey());
        }
        xyConfigFinder.deleteBatchIds(Arrays.asList(configIds));
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<XyConfig> configsList = selectConfigList(new XyConfig());
        configsList.forEach(config ->
            CacheUtils.put(CacheNames.SYS_CONFIG, config.getConfigKey(), config.getConfigValue()));
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        CacheUtils.clear(CacheNames.SYS_CONFIG);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(XyConfig config) {
        Long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        XyConfig info = xyConfigFinder.selectConfigByKey(config.getConfigKey());
        if (ObjectUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    @Override
    public String getConfigValue(String configKey) {
        return SpringUtils.getAopProxy(this).selectConfigByKey(configKey);
    }

}
