package com.example.music.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @ClassName: Md5Util
 * @Description: md5加密 由于Oracle 没有md5()函数
 * @Author: JohnsonYSJ
 * @Date: 2019/9/8 4:06
 * @Version: 1.0
 */
public class Md5Util {
    private static final String[] HEX_DIG_ITS = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);

    /**
     * MD5加密
     * @param origin 字符
     * @param charsetName 编码
     */
    public static String md5Encode(String origin, String charsetName){
        String result = null;
        try{
            result = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(null == charsetName || "".equals(charsetName)){
                result = byteArrayToHexString(md.digest(result.getBytes()));
            }else{
                result = byteArrayToHexString(md.digest(result.getBytes(charsetName)));
            }
        }catch (Exception e){
            LOGGER.error("MD5加密失败，原因为{0}",e);
        }
        return result;
    }

    private static String byteArrayToHexString(byte[] bytes){
        StringBuilder resultSb = new StringBuilder();
        for (byte value : bytes) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIG_ITS[d1] + HEX_DIG_ITS[d2];
    }

}

