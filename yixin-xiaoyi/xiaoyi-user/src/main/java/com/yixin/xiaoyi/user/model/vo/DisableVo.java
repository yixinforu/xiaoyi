package com.yixin.xiaoyi.user.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: huangzexin
 * @date: 2023/8/29 18:14
 */
@Data
public class DisableVo  implements Serializable {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不允许为空")
    private Long userId;


    /**
     * 封禁时间 单位：天
     */
    @NotNull(message = "封禁时长不允许为空")
    private Integer disableDays;
}
