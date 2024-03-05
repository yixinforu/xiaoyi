package com.yixin.xiaoyi.web.controller.api.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.yixin.xiaoyi.common.config.XiaoYiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页
 *
 * @author admin
 */
@RequiredArgsConstructor
@Controller
public class XyIndexInnerController {

    /**
     * 系统基础配置
     */
    private final XiaoYiConfig xiaoYiConfig;

    /**
     * 访问首页，提示语
     */
    @SaIgnore
    @GetMapping("/index")
    public String index() {
        return "redirect:/web/index.html";
    }
}
