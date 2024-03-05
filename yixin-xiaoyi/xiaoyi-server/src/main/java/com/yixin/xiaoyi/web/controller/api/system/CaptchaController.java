package com.yixin.xiaoyi.web.controller.api.system;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.constant.Constants;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.enums.CaptchaType;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.common.utils.reflect.ReflectUtils;
import com.yixin.xiaoyi.common.utils.spring.SpringUtils;
import com.yixin.xiaoyi.framework.config.properties.CaptchaProperties;

import com.yixin.xiaoyi.system.service.IXyConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码操作处理
 *
 * @author admin
 */
@SaIgnore
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {

    private final CaptchaProperties captchaProperties;

    private final IXyConfigService configService;



    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public R<Map<String, Object>> getCode() {
        Map<String, Object> ajax = new HashMap<>();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return R.ok(ajax);
        }
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = isMath ? getCodeResult(captcha.getCode()) : captcha.getCode();
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        ajax.put("uuid", uuid);
        ajax.put("img", captcha.getImageBase64());
        return R.ok(ajax);
    }

    private String getCodeResult(String capStr) {
        int numberLength = captchaProperties.getNumberLength();
        int a = Convert.toInt(StringUtils.substring(capStr, 0, numberLength).trim());
        char operator = capStr.charAt(numberLength);
        int b = Convert.toInt(StringUtils.substring(capStr, numberLength + 1, numberLength + 1 + numberLength).trim());
        switch (operator) {
            case '*':
                return Convert.toStr(a * b);
            case '+':
                return Convert.toStr(a + b);
            case '-':
                return Convert.toStr(a - b);
            default:
                return StringUtils.EMPTY;
        }
    }

}
