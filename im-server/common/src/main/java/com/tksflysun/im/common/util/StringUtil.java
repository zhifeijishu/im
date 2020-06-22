package com.tksflysun.im.common.util;

import com.tksflysun.im.common.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.Random;

public class StringUtil {

    /**
     * 获取指定长度的随机字符串
     * 
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getSuffix(String origin, int length) {
        if (StringUtils.isEmpty(origin) || origin.length() < length) {
            throw new BusinessException("不合理操作");
        }
        return origin.substring(origin.length()-length);
    }
}
