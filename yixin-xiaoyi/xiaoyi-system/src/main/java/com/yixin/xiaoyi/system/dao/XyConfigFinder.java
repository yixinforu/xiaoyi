package com.yixin.xiaoyi.system.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.system.entity.XyConfig;
import com.yixin.xiaoyi.system.dao.mapper.XyConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 19:05
 */
@Repository
@RequiredArgsConstructor
public class XyConfigFinder {

    private final XyConfigMapper xyConfigMapper;

    public Page<XyConfig> selectPageConfigList(XyConfig config, PageQuery pageQuery){
        Map<String, Object> params = config.getParams();
        LambdaQueryWrapper<XyConfig> lqw = new LambdaQueryWrapper<XyConfig>()
            .like(StringUtils.isNotBlank(config.getConfigName()), XyConfig::getConfigName, config.getConfigName())
            .eq(StringUtils.isNotBlank(config.getConfigType()), XyConfig::getConfigType, config.getConfigType())
            .like(StringUtils.isNotBlank(config.getConfigKey()), XyConfig::getConfigKey, config.getConfigKey())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
        return  xyConfigMapper.selectPage(pageQuery.build(), lqw);
    }


    public List<XyConfig> selectConfigList(XyConfig config){
        Map<String, Object> params = config.getParams();
        LambdaQueryWrapper<XyConfig> lqw = new LambdaQueryWrapper<XyConfig>()
            .like(StringUtils.isNotBlank(config.getConfigName()), XyConfig::getConfigName, config.getConfigName())
            .eq(StringUtils.isNotBlank(config.getConfigType()), XyConfig::getConfigType, config.getConfigType())
            .like(StringUtils.isNotBlank(config.getConfigKey()), XyConfig::getConfigKey, config.getConfigKey())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
        return xyConfigMapper.selectList(lqw);
    }

    public int updateById(XyConfig config){
         return xyConfigMapper.updateById(config);
    }

    public int updateByKey(XyConfig config){
        return xyConfigMapper.update(config, new LambdaQueryWrapper<XyConfig>()
            .eq(XyConfig::getConfigKey, config.getConfigKey()));
    }

    public int deleteBatchIds(Collection<Long> configIds){
        return  xyConfigMapper.deleteBatchIds(configIds);
    }


    public XyConfig selectById(Long configId){
        return xyConfigMapper.selectById(configId);
    }

    public XyConfig selectConfigByKey(String configKey){
        return xyConfigMapper.selectOne(new LambdaQueryWrapper<XyConfig>()
            .eq(XyConfig::getConfigKey, configKey));
    }

    public int insert(XyConfig config){
        return xyConfigMapper.insert(config);
    }
}
