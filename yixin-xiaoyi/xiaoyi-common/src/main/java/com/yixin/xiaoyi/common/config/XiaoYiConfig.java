package com.yixin.xiaoyi.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author admin
 */

@Data
@Component
@ConfigurationProperties(prefix = "xiaoyi")
public class XiaoYiConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 缓存懒加载
     */
    private boolean cacheLazy;

    /**
     * sm4 秘钥
     */
    private String sm4Key;

    /**
     * 签名密钥
     */
    private String signSecret;

    /**
     * 临时目录
     */
    @Getter
    private static String tempDir;

    /**
     * 资源路径
     */
    @Getter
    private static String libDir;

    /** 上传路径 */
    @Getter
    private static String materialPath;

    /**
     * 本项目访问地址
     */
    @Getter
    private static String materialUrl;

    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;


    /**
     * 地址
     */
    @Getter
    private static String link;

    public  void setLink(String link) {
        XiaoYiConfig.link = link;
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getMaterialPath();
    }


    public void setTempDir(String tempDir){
        XiaoYiConfig.tempDir = tempDir;
    }

    public void setLibDir(String libDir){
        XiaoYiConfig.libDir = libDir;
    }

    public void setMaterialPath(String profile) {
        XiaoYiConfig.materialPath = profile;
    }

    public void setMaterialUrl(String materialUrl) {
        XiaoYiConfig.materialUrl = materialUrl;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        XiaoYiConfig.addressEnabled = addressEnabled;
    }

}
