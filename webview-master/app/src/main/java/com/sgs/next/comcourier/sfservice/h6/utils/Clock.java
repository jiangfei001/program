package com.sgs.next.comcourier.sfservice.h6.utils;

import com.sgs.next.comcourier.sfservice.fourlevel.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

public class Clock {
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    private static long offsetInMillis = 0;

    private Clock() {
    }




    public static DateTime now() {
        return adjustTime(internalNow());
    }

    public static DateTime adjustTime(DateTime time) {
        return minusMillis(time, offsetInMillis);
    }

    // An fix to DateTime's API, allow pass argument as long
    private static DateTime minusMillis(DateTime dateTime, long millis) {
        if (millis == 0)
            return dateTime;

        long instant = dateTime.getChronology().millis().subtract(dateTime.getMillis(), millis);
        return dateTime.withMillis(instant);
    }

    private static DateTime internalNow() {
        return new DateTime();
    }

    public static String formatToYMD(DateTime dateTime) {
        return dateTime.toString("YYYYMMdd");
    }

    public static DateTime today() {
        if (now().getHourOfDay() <= 4) {
            return now().minusDays(1).withTimeAtStartOfDay().plusHours(4);
        }
        return now().withTimeAtStartOfDay().plusHours(4);
    }

    public static DateTime yesterday() {
        return today().minusDays(1);
    }

    public static DateTime tomorrow() {
        return today().plusDays(1);
    }

    public static String getYmdOfDate(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd");
    }

    public static String getYmdOfDate(DateTime date) {
        return date.toString("yyyy-MM-dd");
    }

    public static String getHmOfDate(Date date) {
        return getHmOfDate(new DateTime(date));
    }

    public static String getHmOfDate(DateTime date) {
        return date.toString("HH:mm");
    }

    public static String getHm2OfDate(DateTime date) {
        return date.toString("HHmm");
    }

    public static DateTime getTodayDateTimeByHm(String hhmm) {
        DateTime now = now();
        if (StringUtils.isEmpty(hhmm) || hhmm.length() != 4) {
            return now;
        }
        String hourString = hhmm.substring(0, 2);
        String minuteString = hhmm.substring(2, 4);
        now = now.withHourOfDay(Integer.parseInt(hourString));
        now = now.withMinuteOfHour(Integer.parseInt(minuteString));
        return now;
    }

    public static String getYMdHmsOfDate(DateTime referenceTime) {
        return referenceTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public static DateTime getThreeMonthAgoOfDate() {
        return Clock.today().minusMonths(3);
    }

    public static String getHmsOfDate(DateTime dateTime) {
        return dateTime.toString("HH:mm:ss");
    }

    public static String getFormatDateTime(Date date) {
        return new DateTime(date).toString("MM-dd HH:mm");
    }

    public static String getFormatMonthAndDay(String dateTime, String format) {
        DateTime yyyyMMdd = DateTime.parse(dateTime, DateTimeFormat.forPattern("yyyyMMdd"));
        return yyyyMMdd.toString(format);
    }

    public static String getMdHmOfCurrentTime() {
        DateTime dateTime = DateTime.now();
        return dateTime.toString("MMddHHmm");
    }

    public static boolean isZoneCodeConfirmDate() {
        boolean isConfirmDate = false;
        DateTime dateTime = DateTime.now();
        int dayOfMonth = dateTime.getDayOfMonth();
        if ((dayOfMonth >= 1 && dayOfMonth <= 2) || (dayOfMonth >= 15 && dayOfMonth <= 16)) {
            isConfirmDate = true;
        }
        return isConfirmDate;
    }

    public static boolean isToday(long time) {
        String today = formatToYMD(now());
        String timeDay = formatToYMD(new DateTime(time));
        return today.equals(timeDay);
    }

    public static DateTime getDateTimeOfCurrentMoth(int dayOfMoth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMoth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new DateTime(calendar.getTime());
    }

    public static long getMinutesLeftTheDay(String time) {
        DateTime theDay = DateTime.parse(time, DateTimeFormat.forPattern("yyyyMMddHHmm"));
        DateTime tomorrow = theDay.plusDays(1).withTimeAtStartOfDay();
        return (tomorrow.getMillis() - theDay.getMillis()) / 60000;
    }

    public static String getHmOfTime(String time) {
        DateTime date = DateTime.parse(time, DateTimeFormat.forPattern("yyyyMMddHHmm"));
        return date.toString("HH:mm");
    }

    public static String getYYYYMMddHmsOfDate(DateTime referenceTime) {
        return referenceTime.toString("yyyyMMddHHmmss");
    }

    public static DateTime get60DaysAgoOfDate() {
        return Clock.today().minusDays(60);
    }

    public static DateTime dateTimeAddTwoHours(DateTime dateTime) {
        DateTime today = now().withTimeAtStartOfDay();
        today = today.plusDays(1);
        today = today.minusSeconds(1);
        dateTime = dateTime.plusHours(2);
        if (dateTime.isAfter(today)) {
            return today;
        }
        return dateTime;
    }
}
