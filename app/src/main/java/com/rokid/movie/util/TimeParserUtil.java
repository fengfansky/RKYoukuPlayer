package com.rokid.movie.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

/**
 * Created by fanfeng on 2017/5/27.
 */

public class TimeParserUtil {

    private static final String TAG = "TimeParserUtil";

    private static final String NUM_ONE = "一";
    private static final String NUM_TWO = "二";
    private static final String NUM_THREE = "三";
    private static final String NUM_FOUR = "四";
    private static final String NUM_FIVE = "五";
    private static final String NUM_SIX = "六";
    private static final String NUM_SEVEN = "七";
    private static final String NUM_EIGHT = "八";
    private static final String NUM_NINE = "九";
    private static final String NUM_TEN = "十";
    private static final String NUM_HUNDRED = "百";

    private static final String TIME_HOUR = "hour";
    private static final String TIME_MINUTE = "min";
    private static final String TIME_SECOND = "second";


    public static int parseTime(Map<String, String> slots) {

        String hour = slots.get(TIME_HOUR);
        String minute = slots.get(TIME_MINUTE);
        String second = slots.get(TIME_SECOND);

        Log.d(TAG, "nlp slots hour: " + NumberParserUtil.parseChineseNumber(hour) + " minute: " + NumberParserUtil.parseChineseNumber(minute) + " second : " + NumberParserUtil.parseChineseNumber(second));
        int seekTime = NumberParserUtil.parseChineseNumber(hour) * 3600 + NumberParserUtil.parseChineseNumber(minute) * 60 + NumberParserUtil.parseChineseNumber(second);
        Log.d(TAG, "nlp seekTime : " + seekTime);
        return seekTime * 1000;
    }

    private static int translateToNum(String timeStr) {

        if (TextUtils.isEmpty(timeStr))
            return 0;

        if (NUM_ONE.length() == timeStr.length()) {
            return Integer.parseInt(transientText(timeStr));
        }

        StringBuilder sb = new StringBuilder();
        if (timeStr.contains(NUM_TEN) || timeStr.contains(NUM_HUNDRED)) {
            if (timeStr.endsWith(NUM_HUNDRED)) {
                sb.append(timeStr.substring(0));
                return Integer.parseInt(transientText(sb.toString())) * 100;
            } else if (timeStr.endsWith(NUM_TEN)) {
                sb.append(timeStr.substring(0));
                return Integer.parseInt(transientText(sb.toString())) * 10;
            } else {
                timeStr.replace(NUM_TEN, "").replace(NUM_HUNDRED, "")
                        .replace(NUM_ONE, "1").replace(NUM_TWO, "2").replace(NUM_THREE, "3")
                        .replace(NUM_FOUR, "4").replace(NUM_FIVE, "5").replace(NUM_SIX, "6")
                        .replace(NUM_SEVEN, "7").replace(NUM_EIGHT, "8").replace(NUM_NINE, "9")
                        .replace(NUM_NINE, "0");
                return Integer.parseInt(timeStr);
            }
        }

        return 0;
    }

    private static String transientText(String numStr) {

        if (TextUtils.isEmpty(numStr)) {
            return "";
        }
        switch (numStr) {
            case NUM_ONE:
                return "1";
            case NUM_TWO:
                return "2";
            case NUM_THREE:
                return "3";
            case NUM_FOUR:
                return "4";
            case NUM_FIVE:
                return "5";
            case NUM_SIX:
                return "6";
            case NUM_SEVEN:
                return "7";
            case NUM_EIGHT:
                return "8";
            case NUM_NINE:
                return "9";
            case NUM_TEN:
                return "10";
        }
        return "";
    }
}
