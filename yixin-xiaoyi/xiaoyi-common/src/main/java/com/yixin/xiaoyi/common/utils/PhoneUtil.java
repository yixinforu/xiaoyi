package com.yixin.xiaoyi.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: huangzexin
 * @date: 2023/8/29 11:08
 */
public class PhoneUtil {


    /**
     * @param phone 字符串类型的手机号
     * 传入手机号,判断后返回
     * true为手机号,false相反
     * */
    public static boolean verifyPhone(String phone) {
        String regex = "^1[3456789]\\d{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
