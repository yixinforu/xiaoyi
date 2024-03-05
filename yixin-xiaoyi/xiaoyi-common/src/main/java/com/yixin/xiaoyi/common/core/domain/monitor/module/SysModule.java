package com.yixin.xiaoyi.common.core.domain.monitor.module;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统相关信息
 *
 * @author ruoyi
 */
@Data
public class SysModule implements Serializable
{
    private static final long serialVersionUID = 1L;
    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

}
