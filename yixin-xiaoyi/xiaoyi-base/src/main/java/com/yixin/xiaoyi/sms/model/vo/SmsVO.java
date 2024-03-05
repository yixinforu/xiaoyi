package com.yixin.xiaoyi.sms.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: huangzexin
 * @date: 2023/10/7 15:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SmsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不允许为空")
    private String phone;

    /**
     * 签名
     */
    @NotBlank(message = "签名不允许为空")
    private String signature;


    /**
     * 时间戳
     */
    @NotNull(message = "时间戳不允许为空")
    private Long timestamp;
}
