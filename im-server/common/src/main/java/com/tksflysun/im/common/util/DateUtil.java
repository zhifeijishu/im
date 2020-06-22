package com.tksflysun.im.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String DEFAULT_FORMAT = "yyyyMMddHHmmss";

    public static String addDateWithHour(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);
    }

    public static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Integer getRemainTimeToTomorrow(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault()).plusDays(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int)seconds;
    }

    public static Integer getYearOfDate(Date date) {
        String year = String.format("%tY", date);
        return Integer.parseInt(year);
    }

    public static Integer getMonthOfDate(Date date) {
        String month = String.format("%tm", date);
        return Integer.parseInt(month);
    }

    public static Integer getDayOfDate(Date date) {
        String day = String.format("%td", date);
        return Integer.parseInt(day);
    }

    // 判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    // 判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    // 判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);// 参数时间
        String now = sdf.format(new Date());// 当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 获取在指定日期上加上或减少指定年数的日期
     * 
     * @param num
     * @return
     */
    public static Date addYear(Date targetDate, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        calendar.add(Calendar.YEAR, num);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取在指定日期上加上或减少指定月数的日期
     * 
     * @param num
     * @return
     */
    public static Date addMonth(Date targetDate, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        calendar.add(Calendar.MONTH, num);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取指定天数之前或之后的日期
     * 
     * @param num
     * @return
     */
    public static Date addDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        Date date = calendar.getTime();
        return date;
    }

    public static String getMonthAndDayStr(Date date) {
        String template = "%s月%s日";
        Integer month, day;
        month = getMonthOfDate(date);
        day = getDayOfDate(date);
        return String.format(template, month, day);
    }

    public static int getDiffMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * end比start多的天数
     * 
     * @param start
     * @param end
     * @return
     */
    public static int getDiffDays(Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) // 不同年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
                {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else // 同一年
        {
            return day2 - day1;
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(date.getTime());// 这个就是把时间戳经过处理得到期望格式的时间
        return time;
    }

    public static String getTimeStampStr() {
        return new Date().getTime() + "";
    }

    public static long getTimeStamp() {
        return new Date().getTime();
    }
}
