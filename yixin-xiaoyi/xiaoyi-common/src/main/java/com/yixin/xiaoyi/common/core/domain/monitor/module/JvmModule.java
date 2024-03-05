package com.yixin.xiaoyi.common.core.domain.monitor.module;



import com.yixin.xiaoyi.common.utils.Arith;
import com.yixin.xiaoyi.common.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 *
 * @author ruoyi
 */
@Data
public class JvmModule implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    private double usage;

    private double used;

    private String name;

    private String startTime;

    private String runTime;

    private String inputArgs;

}
