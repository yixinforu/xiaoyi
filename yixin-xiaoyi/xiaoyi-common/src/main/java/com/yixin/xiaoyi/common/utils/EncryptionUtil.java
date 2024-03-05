package com.yixin.xiaoyi.common.utils;


import com.yixin.xiaoyi.common.utils.sign.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**AES加解密工具类
 * @author: yixin
 * @date: 2023/3/1 16:32
 */
@Slf4j
public class EncryptionUtil {

    /**
     * AES密钥标识
     */
    public static final String SIGN_AES = "AES";

    /**
     * 字符串编码
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * 密码器AES模式
     */
    public static final String CIPHER_AES = "AES/ECB/PKCS5Padding";

    /**
     * 密钥长度128
     */
    public static final int KEY_SIZE_128_LENGTH = 128;

    /**
     * 密钥长度192
     */
    public static final int KEY_SIZE_192_LENGTH = 192;

    /**
     * 密钥长度256
     */
    public static final int KEY_SIZE_256_LENGTH = 256;

    /**
     * 生成密钥，请使用合适的长度128 192 256
     *
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String createKeyString(int keySize) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();

        return Base64.encode(keyBytes);
    }

    /**
     * 生成Key对象
     *
     * @param keyString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Key generateKey(String keyString) throws UnsupportedEncodingException {
        byte[] decodedKey = Base64.decode(keyString);
        Key key = new SecretKeySpec(decodedKey, SIGN_AES);
        return key;
    }

    /**
     * AES加密
     *
     * @param source
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encryptKey(String source,String key) {
        if(StringUtils.isBlank(source)){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_AES);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(key));
            byte[] encrypted = cipher.doFinal(source.getBytes(UTF_8));
            return Base64.encode(encrypted);
        }catch (Exception e){
            log.error("手机号:{} 加密异常",source);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param encrypted
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static String decryptKey(String encrypted,String key) {
        try{
        Cipher cipher = Cipher.getInstance(CIPHER_AES);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(key));
        byte[] decrypted = cipher.doFinal(Base64.decode(encrypted));
        return new String(decrypted, UTF_8);}
        catch(Exception e){
            log.error("手机号:{} 解密异常",encrypted);
            e.printStackTrace();
        }
        return null;
    }

    public  String genAesSecret(String key) {
        log.info("++++++++++++++++++++++++++++++:"+key);
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            //下面调用方法的参数决定了生成密钥的长度，可以修改为128, 192或256
            kg.init(KEY_SIZE_128_LENGTH, new SecureRandom(key.getBytes(StandardCharsets.UTF_8)));
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String secret = java.util.Base64.getEncoder().encodeToString(b);
            return secret;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("没有此算法");
        }
    }

}
