package com.taobao.api.internal.util;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

/***
 * 日志工具类
 *
 * @author hxp
 */
public class LogUtils {

    private static boolean DEBUG = true;

    private static final int Log_Level_error = 4;

    private static final int Log_Level_warn = 3;

    private static final int Log_Level_info = 2;

    private static final int Log_Level_debug = 1;

    private static final int Log_Level_verbose = 0;

    /**
     * Log信息级别>=logLevel的日志信息打印出来
     */
    private static int logLevel = 0;

    // 是否将日志写到文件中
    private static final boolean Log_IN_FILE = false;

    private static final boolean Log_WITH_POSTION = false;

    private static final String LOG_TAG = "alitvsdk";

    private static final String LOG_FILE = LOG_TAG + ".log";

    private static Context mContext = null;

    private static AtomicLong transactionId = new AtomicLong(1);

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void init(Context c) {
        mContext = c;
        // setDebug(getDebugMode(c));
    }

    public static long getTransactionId() {
        return transactionId.getAndIncrement();
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
        Log.d(LOG_TAG, "set debug = " + debug);
    }

    /**
     * 详细信息
     */
    public static void v(String msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (!DEBUG) {
            return;
        }

        if (Log_Level_verbose >= logLevel) {
            if (Log_WITH_POSTION) {
                msg = msg + "on"
                        + new Throwable().getStackTrace()[1].toString();
            }
            Log.v(tag, msg);

            if (Log_IN_FILE) {
                writeIntoFile(tag + ":v: " + msg);
            }
        }
    }

    /**
     * 调试日志
     */
    public static void d(String msg) {
        d(LOG_TAG, msg);
    }

    /**
     * 事务型日志
     *
     * @param tag
     * @param transaction
     * @param info
     */
    public static void d(String tag, String transaction, String info) {
        d(tag, transaction + " : " + info);
    }

    public static void d(String tag, String info) {
        if (!DEBUG) {
            return;
        }

        if (Log_Level_debug >= logLevel) {
            if (Log_WITH_POSTION) {
                info = info + "on"
                        + new Throwable().getStackTrace()[1].toString();
            }
            Log.d(tag, info);
            if (Log_IN_FILE) {
                writeIntoFile(tag + ":d:" + info);
            }
        }
    }

    /**
     * 信息日志
     */
    public static void i(String info) {
        i(LOG_TAG, info);
    }

    public static void i(String tag, String info) {
        if (!DEBUG) {
            return;
        }

        if (Log_Level_info >= logLevel) {
            if (Log_WITH_POSTION) {
                info = info + "on"
                        + new Throwable().getStackTrace()[1].toString();
            }
            Log.i(tag, info);
            if (Log_IN_FILE) {
                writeIntoFile(tag + ":i: " + info);
            }
        }
    }

    /**
     * 警告日志
     */
    public static void w(String msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, String info) {
        if (!DEBUG) {
            return;
        }

        if (Log_Level_warn >= logLevel) {
            if (Log_WITH_POSTION) {
                info = info + "on"
                        + new Throwable().getStackTrace()[1].toString();
            }
            Log.w(tag, info);
            if (Log_IN_FILE) {
                writeIntoFile(tag + ":w: " + info);
            }
        }
    }

    /**
     * 错误日志
     */
    public static void e(String msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, String info) {
        if (!DEBUG) {
            return;
        }

        if (Log_Level_error >= logLevel) {
            if (Log_WITH_POSTION) {
                info = info + " on "
                        + new Throwable().getStackTrace()[1].toString();
            }
            if (info != null)
                Log.e(tag, info);
            else {
                Log.e(LOG_TAG, "info null");
            }
            if (Log_IN_FILE) {
                writeIntoFile(tag + ":e: " + info);
            }
        }
    }

    public static void e(String title, Exception e) {
        e(LOG_TAG, title, e);
    }

    public static void e(String tag, String title, Throwable e) {
        if (!DEBUG) {
            return;
        }

        String msg = null;
        if (e == null) {
            msg = title + ": " + "null";
        } else {
            msg = title + ": " + e.toString();
        }
        e(tag, msg);
    }

    public static void logLongMsg(String msg) {
        logLongMsg(LOG_TAG, msg);
    }

    public static void logLongMsg(String tag, String msg) {
        if (!DEBUG || msg == null) {
            return;
        }

        int maxLogSize = 1000;
        int len = msg.length();
        boolean contineError = msg.contains("error");

        for (int i = 0; i <= len / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            if (contineError) {
                Log.e(tag, msg.substring(start, end));
            } else {
                Log.v(tag, msg.substring(start, end));
            }
        }
        if (Log_IN_FILE) {
            writeIntoFile(tag + ":v: " + msg);
        }
    }

    public static boolean writeIntoFile(String msg) {
        if (!DEBUG || mContext == null) {
            return false;
        }

        boolean res = false;
        try {
            FileOutputStream fOut = mContext.openFileOutput(LOG_FILE,
                    Context.MODE_APPEND);
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .format(new Date());
                fOut.write(date.getBytes());
                fOut.write("-->".getBytes());
                fOut.write(msg.getBytes());
                fOut.write("\r\n".getBytes());
                res = true;
            } catch (IOException e) {
                LogUtils.e(LOG_TAG, e.toString());
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(LOG_TAG, e.toString());
        }
        return res;
    }

}
