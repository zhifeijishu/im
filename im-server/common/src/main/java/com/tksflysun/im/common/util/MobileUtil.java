package com.tksflysun.im.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtil {
    /*  <br>　　　　　2019年1月16日已知
   中国电信号段
       133,149,153,173,174,177,180,181,189,199
   中国联通号段
       130,131,132,145,146,155,156,166,175,176,185,186
   中国移动号段
       134(0-8),135,136,137,138,139,147,148,150,151,152,157,158,159,165,178,182,183,184,187,188,198
   上网卡专属号段（用于上网和收发短信，不能打电话）
       如中国联通的是145
   虚拟运营商
       电信：1700,1701,1702
       移动：1703,1705,1706
       联通：1704,1707,1708,1709,171
   卫星通信： 1349 <br>　　　　　未知号段：141、142、143、144、154
   */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
        if(StringUtils.isNotBlank(str)){
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }
}
