package com.muelpatmore.sanfranciscoparking.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Samuel on 03/12/2017.
 */

public class DateUtils {

    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static Date stringToDate(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException exception) {
            exception.printStackTrace();
            return null;
        }
        return date;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String dateString;
        dateString = dateFormat.format(date);
        return dateString;
    }


    public static Date millisecondsToDate(long dateMilliseconds) {
        Date date=new Date(dateMilliseconds);
        return date;
    }

    public static Date addTimeToDate(Date startTime, int millisecondsToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MILLISECOND, millisecondsToAdd);
        return cal.getTime();
    }

    public static String addTimeToDateString(String dateString, int millisecondsToAdd) {
        Date dateNow = stringToDate(dateString);
        Date dateThen = addTimeToDate(dateNow, millisecondsToAdd);
        String dateStringThen = dateToString(dateThen);
        return dateStringThen;
    }

    public static String dateStringFromNow(int minutes) {
        long timeNow = System.currentTimeMillis();
        timeNow += minutes * 60 * 1000;
        Date dateNow = new Date(timeNow);
        return dateToString(dateNow);
    }
}
