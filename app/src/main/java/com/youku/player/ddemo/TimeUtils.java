package com.youku.player.ddemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.mma.mobile.tracking.util.Logger;

/**
 * Created by fanfeng on 2017/5/13.
 */

public class TimeUtils {

    /**
     * 获取当前的时间戳
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Logger.d("timeStamp is " + simpleDateFormat.format(currentDate));
        return simpleDateFormat.format(currentDate);
    }
}
