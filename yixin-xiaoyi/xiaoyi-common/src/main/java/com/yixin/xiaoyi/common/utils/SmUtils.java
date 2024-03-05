package com.yixin.xiaoyi.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * 国密工具类
 *
 * @author Tait
 * @date 2023/03/18
 */
public class SmUtils {

    /***
     * SM4对称加密，
     */
    public static String sm4Encrypt(String encrypt, String key){
        return symmetricCrypto(key).encryptHex(encrypt);
    }

    /***
     * SM4对称解密，
     */
    public static String sm4Decrypt(String decrypt, String key){
        return symmetricCrypto(key).decryptStr(decrypt);
    }

    private static SymmetricCrypto symmetricCrypto(String key){
        return SmUtil.sm4(key.getBytes(CharsetUtil.CHARSET_UTF_8));
    }

}
