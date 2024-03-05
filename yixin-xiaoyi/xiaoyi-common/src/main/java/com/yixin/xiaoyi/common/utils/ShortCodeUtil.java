package com.yixin.xiaoyi.common.utils;

import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

public class ShortCodeUtil {

    // hash算法所用参数
    private static final int c1 = 0xcc9e2d51;
    private static final int c2 = 0x1b873593;
    private static final int r1 = 15;
    private static final int r2 = 13;
    private static final int m = 5;
    private static final int n = 0xe6546b64;

    public static String getShortCode(String content) {
        if(StringUtils.isEmpty(content)){
            return "";
        }
        String code = to62HEX(hash32(content.getBytes(StandardCharsets.UTF_8),0));
        return code;
    }


    private static String to62HEX(int num) {
        num = Math.abs(num);
        String chars = "0123456789abcdefghijk_mnopqrstuvwxyzABCDEFGHIJKLMN-PQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder();
        int remainder;

        while (num > 62 - 1) {
            remainder = Long.valueOf(num % 62).intValue();
            sb.append(chars.charAt(remainder));
            num = num / 62;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        return sb.reverse().toString();
    }

    private static int hash32(byte[] key, int seed) {
        int hash = seed;
        final int len = key.length;
        int i = 0;
        int k = 0;
        for (; i + 4 <= len; i += 4) {
            k = ((key[i + 3] & 0xff) << 24)
                    | ((key[i + 2] & 0xff) << 16)
                    | ((key[i + 1] & 0xff) << 8)
                    | (key[i] & 0xff);
            k *= c1;
            k = Integer.rotateLeft(k, r1);
            k *= c2;

            hash ^= k;
            hash = Integer.rotateLeft(hash, r2);
            hash = hash * m + n;
        }

        int k1 = 0;
        switch (len - i) {
            case 3:
                k1 = (key[i + 2] & 0xff) << 16;
            case 2:
                k1 |= (key[i + 1] & 0xff) << 8;
            case 1:
                k1 |= key[i] & 0xff;
                k1 *= c1;
                k1 = Integer.rotateLeft(k1, r1);
                k1 *= c2;
                hash ^= k1;
        }

        hash ^= len;
        hash ^= hash >>> 16;
        hash *= 0x85ebca6b;
        hash ^= hash >>> 13;
        hash *= 0xc2b2ae35;
        hash ^= hash >>> 16;

        return hash;
    }
}
