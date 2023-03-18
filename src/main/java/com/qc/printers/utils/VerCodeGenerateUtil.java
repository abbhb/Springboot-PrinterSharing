package com.qc.printers.utils;

/**
 * TODO
 *
 * @author DB
 * <br>CreateDate 2021/9/13 2:07
 */
import java.security.SecureRandom;
import java.util.Random;

/**
 * @Classname VerCodeGenerateUtil
 * @Description 生成验证码工具类
 * @Date 2021/9/7 14:23
 * @Created by DB
 */
public class VerCodeGenerateUtil {
    //验证码包含的字段，可自己设置
    private static final String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    //    生成 6 位数的随机数字
    public static String generateVerCode() {
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
}

