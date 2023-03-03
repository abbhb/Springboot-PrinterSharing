package com.qc.printers.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PWDMD5 {

    // 固定随机盐
    public static final String SECRET="z3g8r3h4bS8DFG0R";

    /**
     * md5+salt 加密算法 (推荐)
     * @author chenlirun
     * @date 2021/7/8 17:09
     */
    public static String getMD5Encryption(String password,String salt){
        //密码md5+随机盐加密
        return DigestUtils.md5DigestAsHex((password+salt).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * md5+salt 加密算法
     * @author chenlirun
     * @date 2021/7/9 8:56
     */
    public static String getMd5Encryption(String password){
        return DigestUtils.md5DigestAsHex((password+SECRET).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成随机盐
     * @author chenlirun
     * @date 2021/7/8 15:56
     */
    public static String getSalt(){
        String str="zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP1234567890,.<>:?";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        //循环16次，共取出16个随机字符
        for (int i = 0; i < 16; i++) {
            //每次生成一个67以内的随机数
            int number = random.nextInt(68);
            //生成的随机数作为 str 字符串的下标；从 str 中取出随机字符后追加到 stringBuffer
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }
}
