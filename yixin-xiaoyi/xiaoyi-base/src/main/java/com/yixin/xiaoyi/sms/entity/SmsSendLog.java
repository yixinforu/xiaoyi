package com.yixin.xiaoyi.sms.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: huangzexin
 * @date: 2023/10/6 21:27
 */
@Data
@Accessors(chain = true)
@TableName("sms_send_log")
public class SmsSendLog extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 短信内容
     */
    private String smsContent;


    /**
     * 加密手机号
     */
    private String encryptedPhone;

    /**
     * 脱敏手机号
     */
    private String maskPhone;


    /**
     * 回执id
     */
    private String bizId;


    /**
     * 返回码
     */
    private String resultCode;


    /**
     * 描述
     */
    private String resultMsg;


}
