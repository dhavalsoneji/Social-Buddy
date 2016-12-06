package com.kittyapplication.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dhaval Soneji Riontech on 16/11/16.
 */

public class DateTimeUtils {

    //2016-11-19T05:13:34+0000
    private static final String FACEBOOK_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+SSSS";
    //Sat, 19 Nov 2016 00:00:00 +0000
    private static final String UPCOMING_EVENT_DATE_FORMAT = "E, dd MMM yyyy HH:mm:ss +SSSS";
    //Nov 19
    private static final String APP_FB_DATE_FORMAT = "MMM dd";

    //Novermber 19, 2016
    private static final String APP_LIST_ROW_DATE_FORMAT = "MMMM dd, yyyy";

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    //
    private static final String DB_FORMAT = "yyyy-MM-dd hh:mm:ss";
    //
    private static final String DETAIL_DATE_FORMAT = "MMMM dd, yyyy hh 'Hour' mm 'Minutes'";
    //
    private static final String MEETING_DATE_FORMAT = "MMMM dd, yyyy";
    //
    private static final String MEETING_TIME_FORMAT = "hh:mm a";

    //Notice list row item date
    private static final String IP_NOTICE_LIST_ROW_DATE_FORMAT = "MMM dd yyyy, hh:mm a";
    private static final String OP_NOTICE_LIST_ROW_DATE_FORMAT = "MMM dd";

    public static SimpleDateFormat getDBFormat() {
        return new SimpleDateFormat(DB_FORMAT);
    }

    public static SimpleDateFormat getDetailDateFormat() {
        return new SimpleDateFormat(DETAIL_DATE_FORMAT);
    }

    public static SimpleDateFormat getMeetingDateFormat() {
        return new SimpleDateFormat(MEETING_DATE_FORMAT);
    }

    public static SimpleDateFormat getMeetingTimeFormat() {
        return new SimpleDateFormat(MEETING_TIME_FORMAT);
    }

    public static String getSocialPostDateFormat(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(FACEBOOK_DATE_FORMAT);
        SimpleDateFormat outputFormat = new SimpleDateFormat(APP_FB_DATE_FORMAT);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return str;
    }

    public static String getUpComingEventDate(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(UPCOMING_EVENT_DATE_FORMAT);
        SimpleDateFormat outputFormat = new SimpleDateFormat(APP_LIST_ROW_DATE_FORMAT);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return str;
    }

    public static String getNoticeListRowDateFormat(String dateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(IP_NOTICE_LIST_ROW_DATE_FORMAT);
        SimpleDateFormat outputFormat = new SimpleDateFormat(OP_NOTICE_LIST_ROW_DATE_FORMAT);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return str;
    }

}
