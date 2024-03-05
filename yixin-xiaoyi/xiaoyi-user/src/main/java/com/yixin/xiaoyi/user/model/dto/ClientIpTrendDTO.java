package com.yixin.xiaoyi.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/9/3 16:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientIpTrendDTO {

    /**
     * ip数量
     */
    private Integer clientIpCount;


    /**
     * 时间
     */
    private Date dateTime;
}
