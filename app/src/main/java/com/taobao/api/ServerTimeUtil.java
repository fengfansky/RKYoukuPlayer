package com.taobao.api;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTimeUtil {
    private static long mTimeDiff = 0;
    private static int mTimeType = 0;

    public static void setTime(long serverTime) {
        if (serverTime == 0) {
            mTimeDiff = 0;
            mTimeType = 0;
        } else {
            mTimeDiff = serverTime - SystemClock.elapsedRealtime();
            mTimeType = 1;
        }
    }

    public static long getTime() {
        long time = 0;
        if (mTimeType == 0) {
            time = System.currentTimeMillis();
        } else {
            time = SystemClock.elapsedRealtime() + mTimeDiff;
        }
        return time;
    }

    public static String getFormatTimestamp() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getTime()));
        return timestamp;
    }
}
