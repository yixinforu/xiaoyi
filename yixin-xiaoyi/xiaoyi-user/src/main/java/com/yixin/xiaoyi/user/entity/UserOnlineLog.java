package com.yixin.xiaoyi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/8/17 0:28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_online_log")
public class UserOnlineLog extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id（空则表示该ip没有登录）
     */
    private Long userId;


    /**
     * 客户端Ip
     */
    private String clientIp;

    /**
     * 最后请求时间
     */
    private Date lastRequestTime;


    /**
     * 当天请求数量
     */
    private Integer requestCount;

    @TableField(exist = false)

    /**
     * 当前请求时间
     */
    private long currentRequestTime;

}
