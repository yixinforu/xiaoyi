package com.yixin.xiaoyi.user.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.user.entity.XyLogininfor;
import com.yixin.xiaoyi.user.dao.mapper.XyLogininforMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 12:42
 */
@Repository
@RequiredArgsConstructor
public class XyLogininforFinder {

    private final XyLogininforMapper xyLogininforMapper;


    public Page<XyLogininfor> selectPageLogininforList(XyLogininfor logininfor,PageQuery pageQuery){
        Map<String, Object> params = logininfor.getParams();
        LambdaQueryWrapper<XyLogininfor> lqw = new LambdaQueryWrapper<XyLogininfor>()
            .like(StringUtils.isNotBlank(logininfor.getIpaddr()), XyLogininfor::getIpaddr, logininfor.getIpaddr())
            .eq(StringUtils.isNotBlank(logininfor.getStatus()), XyLogininfor::getStatus, logininfor.getStatus())
            .eq(StringUtils.isNotBlank(logininfor.getPhone()),XyLogininfor::getEncryptedPhone,EncryptionUtil.encryptKey(logininfor.getPhone(), UserConfig.getPhoneSecret()))
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyLogininfor::getLoginTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("info_id");
            pageQuery.setIsAsc("desc");
        }
        return xyLogininforMapper.selectPage(pageQuery.build(), lqw);
    }

    public List<XyLogininfor> selectLogininforList(XyLogininfor logininfor){
        Map<String, Object> params = logininfor.getParams();
        return xyLogininforMapper.selectList(new LambdaQueryWrapper<XyLogininfor>()
            .like(StringUtils.isNotBlank(logininfor.getIpaddr()), XyLogininfor::getIpaddr, logininfor.getIpaddr())
            .eq(StringUtils.isNotBlank(logininfor.getStatus()), XyLogininfor::getStatus, logininfor.getStatus())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyLogininfor::getLoginTime, params.get("beginTime"), params.get("endTime"))
            .orderByDesc(XyLogininfor::getInfoId));
    }

    /**
     * 新增
     * @param logininfor
     * @return
     */
    public int insert(XyLogininfor logininfor){
       return xyLogininforMapper.insert(logininfor);
    }

    public int deleteBatchIds(Collection<Long> ids){
        return xyLogininforMapper.deleteBatchIds(Arrays.asList(ids));
    }

    public int cleanLogininfor(){
       return xyLogininforMapper.delete(new LambdaQueryWrapper<>());
    }
}
