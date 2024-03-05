package com.yixin.xiaoyi.common.core.domain.monitor.module;


import com.yixin.xiaoyi.common.core.domain.monitor.server.Cpu;
import com.yixin.xiaoyi.common.core.domain.monitor.server.Jvm;
import com.yixin.xiaoyi.common.core.domain.monitor.server.Mem;
import com.yixin.xiaoyi.common.core.domain.monitor.server.Sys;
import com.yixin.xiaoyi.common.core.domain.monitor.server.SysFile;
import com.yixin.xiaoyi.common.utils.Arith;
import com.yixin.xiaoyi.common.utils.ip.IpUtils;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 服务器相关信息
 *
 * @author ruoyi
 */
@Data
public class ServerModule implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private CpuModule cpu = new CpuModule();

    /**
     * 內存相关信息
     */
    private MemModule mem = new MemModule();

    /**
     * JVM相关信息
     */
    private JvmModule jvm = new JvmModule();

    /**
     * 服务器相关信息
     */
    private SysModule sys = new SysModule();

    /**
     * 磁盘相关信息
     */
    private List<SysFileModule> sysFiles = new LinkedList<SysFileModule>();

    public ServerModule(CpuModule cpu, MemModule mem, JvmModule jvm, SysModule sys, List<SysFileModule> sysFiles) {
        this.cpu = cpu;
        this.mem = mem;
        this.jvm = jvm;
        this.sys = sys;
        this.sysFiles = sysFiles;
    }

    public ServerModule(){}
}
