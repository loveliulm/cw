package com.qifu.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.crypto.Data;

/**
 * 时间工具类
 *
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


    /**
     * 计算两个时间差30天
     */
    public static Boolean is30Days(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day > 30;
    }

    /**
     * 计算两个时间差7天
     */
    public static Boolean is7Days(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day > 7;
    }

    //根据账期获取上一年12月账期
    public static Date getLastYearPeriod(Date date){
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = Integer.valueOf(gc.get(1))-1+"-12"+"-01";
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    //根据账期获取上个账期
    public static Date getPreviousMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    //根据账期获取上个账期
    public static Date nextPeriod(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    //根据账期获取最后一天
    public static Date getLastDayByYearPeriod(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }

    // 获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取时间的月份
    public static int getMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    // 获取时间的年份
    public static int getYear(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.YEAR);
    }


    /**
     * 获取两个日期之间的所有月(date格式, 按月计算)
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getBetweenMonths(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        // 加了一个月
        tempStart.add(Calendar.MONTH, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        result.add(start);
        while (!tempStart.after(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 日期往后多一天
     * @param time
     * @return
     */
    public static Date timeAddOne(Date time){
        if(time == null){
            return null;
        }
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(calendar.DATE,1);
        return calendar.getTime();
    }

    /**
     *将字符串日期
     *举例:日期格式yyyyMMdd的字符串转为日期格式"yyyy-MM-dd"
     * @param date 需要转换日期字符串  formatDateStr:需要转换日期字符串格式 converDateStr:需要转换为哪种格式的日期
     * @return 返回格式化的日期
     * @throws ParseException 分析时意外地出现了错误异常
     */
    public static String strToDateFormat(String date,String formatDateStr,String converDateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDateStr);
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(converDateStr);
            return simpleDateFormat1.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据月份获取季度
    public static int getSeason(int month){
        int season = 0;
        switch (month) {
            case 1:
            case 2:
            case 3:
                season = 1;
                break;
            case 4:
            case 5:
            case 6:
                season = 2;
                break;
            case 7:
            case 8:
            case 9:
                season = 3;
                break;
            case 10:
            case 11:
            case 12:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }
}
