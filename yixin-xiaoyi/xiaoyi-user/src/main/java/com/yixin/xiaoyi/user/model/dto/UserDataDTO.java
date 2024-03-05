package com.yixin.xiaoyi.user.model.dto;

import com.yixin.xiaoyi.common.core.module.common.dto.DataCountDTO;
import lombok.Data;

/**
 * @author: huangzexin
 * @date: 2023/9/3 11:41
 */
@Data
public class UserDataDTO {

    /**
     * 今日IP访问数
     */
    private Integer todayIpCount;

    /**
     * 当前活跃IP数
     */
    private Integer currentActiveIpCount;


    /**
     * 用户数据
     */
    private DataCountDTO userDataCountDTO;


    /**
     * VIP用户数据
     */
    private DataCountDTO vipDataCountDTO;


}
