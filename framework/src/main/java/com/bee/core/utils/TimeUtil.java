package com.bee.core.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 */
@SuppressWarnings("ALL")
public class TimeUtil {
    //format格式,有缺少的参照样式增加即可
    public static final String FORMAT_M_D = "M月d日";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_DD = "MM月dd日";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_2 = "yyyyMMdd";
    public static final String FORMAT_MM_DD_HH_MM = "MM月dd日  HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_2 = "yyyy_MM_dd_HH_mm_ss";
    public static final String FORMAT_MM_SS = "mm:ss";

    //时区，有缺少的参照样式增加即可
    public static final String TIME_CHINA = "Asia/Shanghai";

    public static Date longToDate(long time) {
        return new Date(time);
    }

    public static String longToString(long time, String formatType) {
        Date date = longToDate(time);
        return dateToString(date, formatType);
    }

    public static String longToString(long time, String formatType, Locale locale) {
        Date date = longToDate(time);
        return dateToString(date, formatType, locale);
    }

    public static String longToString(long time, String formatType, String timeZone) {
        Date date = longToDate(time);
        return dateToString(date, formatType, timeZone);
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String dateToString(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }

    public static String dateToString(Date date, String formatType, Locale locale) {
        return new SimpleDateFormat(formatType, locale).format(date);
    }

    public static String dateToString(Date date, String formatType, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        return format.format(date);
    }

    public static Date stringToDate(String timeStr, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long stringToLong(String timeStr, String formatType) {
        Date date = stringToDate(timeStr, formatType);
        return date == null ? 0L : dateToLong(date);
    }

    public static String stringToString(String timeStr, String beforeFormat, String afterFormat) {
        long time = stringToLong(timeStr, beforeFormat);
        return longToString(time, afterFormat);
    }

    /**
     * 判断目标时间跟系统时间是否同一天
     */
    public static boolean isLocalSameDay(long time) {
        return isSameDay(time, System.currentTimeMillis());
    }

    public static boolean isSameDay(long time1, long time2) {
        return isSameDay(longToDate(time1), longToDate(time2));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        String ds1 = sdf.format(date1);
        String ds2 = sdf.format(date2);
        return TextUtils.equals(ds1, ds2);
    }

    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是昨天
     */
    public static boolean isYesterday(long curTime, long tarTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        int curDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(tarTime);
        int tarDay = calendar.get(Calendar.DAY_OF_YEAR);
        return tarDay - curDay == 1;
    }

    /**
     * 判断两个时间的天数 相差是否大于等于一天（过了0点也算大）（time2 比 time 大一天）
     *
     * @return {@code true} 大于等于1天， {@code false} 小于1天
     */
    public static boolean isLargeOneDay(long time1, long time2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(time2);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1 >= 1;
    }

    public static boolean isSameWeek(long time1, long time2) {
        return getWeekInYear(time1) == getWeekInYear(time2);
    }

    public static String getCurrentTime(String formatType) {
        return dateToString(new Date(), formatType);
    }

    public static int getWeekInYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getWeekZh(long time) {
        String weekDay = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        switch (index) {
            case 1:
                weekDay = "周日";
                break;
            case 2:
                weekDay = "周一";
                break;
            case 3:
                weekDay = "周二";
                break;
            case 4:
                weekDay = "周三";
                break;
            case 5:
                weekDay = "周四";
                break;
            case 6:
                weekDay = "周五";
                break;
            case 7:
                weekDay = "周六";
                break;
            default:
                break;
        }
        return weekDay;
    }

    /**
     * 获取指定天数差值的日期
     *
     * @param srcTime 传入初始时间
     * @param day     差值,以天为单位
     * @return 指定差值后的日期
     */
    public static long getDateAfter(long srcTime, int day) {
        Date date = getDateAfter(longToDate(srcTime), day);
        return date.getTime();
    }

    /**
     * 同上
     */
    public static Date getDateAfter(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        return calendar.getTime();
    }

    /**
     * 获取当天00:00:00对应时刻long值;举例如下：
     * 时间戳：1587830447000 北京时间：2020-04-26 00:00:47 ; 世界标准时间：2020-04-25 16:00:47
     * 归零之后(中国时区需要的逻辑),北京时间少了47s，但世界标准时间少了（16小时+47s)，所以一般
     * 默认转换为中国地区时间，计算后再转换为世界标准时间(ms)。
     * 注：Calendar和Data会根据时区自动计算时间，但最终获取的long型值也自动都转换为世界标准时的时间
     *
     * @param srcTime  初始时间
     * @param timeZone 时区,传空默认中国时区
     * @return 计算后的值
     */
    public static long getStartTimeOfDay(long srcTime, String timeZone) {
        String zone = TextUtils.isEmpty(timeZone) ? TIME_CHINA : timeZone;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(zone));
        calendar.setTimeInMillis(srcTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String getZoneTime(long startTime, long sysTime) {
        String result = "";

        long currentStartTime = System.currentTimeMillis() - sysTime + startTime;

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());
        Calendar otherDayTime = Calendar.getInstance();
        otherDayTime.setTimeInMillis(currentStartTime);

        Date sysDate = new Date(System.currentTimeMillis());
        Date otherDay = new Date(currentStartTime);
        int temp = Integer.parseInt(sdf.format(otherDay)) - Integer.parseInt(sdf.format(sysDate));
        if (currentTime.get(Calendar.YEAR) == otherDayTime.get(Calendar.YEAR)) {
            int current = currentTime.get(Calendar.DAY_OF_YEAR);
            int other = otherDayTime.get(Calendar.DAY_OF_YEAR);
            switch (other - current) {
                case 0:
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    result = "今天 " + format.format(new Date(currentStartTime));
                    break;
                case 1:
                    SimpleDateFormat tow = new SimpleDateFormat("HH:mm");
                    result = "明天 " + tow.format(new Date(currentStartTime));
                    break;
                default:
                    SimpleDateFormat format1 = new SimpleDateFormat(FORMAT_MM_DD_HH_MM);
                    format1.setTimeZone(Calendar.getInstance().getTimeZone());
                    result = format1.format(new Date(currentStartTime));
            }
        } else {
            SimpleDateFormat format1 = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM);
            format1.setTimeZone(Calendar.getInstance().getTimeZone());
            result = format1.format(new Date(currentStartTime));
        }
        return result;
    }

}