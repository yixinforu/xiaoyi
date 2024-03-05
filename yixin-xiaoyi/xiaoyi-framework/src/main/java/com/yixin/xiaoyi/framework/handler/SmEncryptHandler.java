package com.yixin.xiaoyi.framework.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;

import java.security.Security;

/**
 * 自定义国密加解密处理器
 *
 * @author nieHong
 * @date 2023/6/26
 */
public class SmEncryptHandler implements StringEncryptor {
    private final SymmetricCrypto smEncryptor;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SmEncryptHandler(String key) {
        smEncryptor = SmUtil.sm4(key.getBytes(CharsetUtil.CHARSET_UTF_8));
    }


    @Override
    public String encrypt(String value) {
        return smEncryptor.encryptHex(value);
    }

    @Override
    public String decrypt(String value) {
        return smEncryptor.decryptStr(value);
    }
}
