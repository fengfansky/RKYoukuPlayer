package com.rokid.movie.taobao.api.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

/**
 * Created by lizhi.ywp on 2017-05-17.
 */

public class CommUtils {

    public static String getDeviceID() {
        try {
            Class<?> cloudUuid = Class.forName("com.yunos.baseservice.clouduuid.CloudUUID");
            Method m = cloudUuid.getMethod("getCloudUUID");
            String result = (String) m.invoke(null);
            if ("false".equalsIgnoreCase(result)) {
                return "unknow_tv_imei";
            }
            return result;
        } catch (Exception e) {
//            e.printStackTrace();
            return "unknow_tv_imei";
        }
    }

    public static int strLengthOrigin(String value) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            // 获取一个字符
            String temp = value.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        // 进位取整
        return (int) Math.ceil(valueLength) * 2;
    }

    public static String getSubStr(String s, int length, Boolean ellipsis) throws Exception {

        byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 != 0) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 != 0)

        {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }
        String result = new String(bytes, 0, i, "Unicode");
        if (strLengthOrigin(s) > length && ellipsis == true) {
            result += "...";
        }
        return result;
    }

    public static String getSystemProperties(String key) {
        try {
            Class<?> SystemProperties = Class
                    .forName("android.os.SystemProperties");
            Method m = SystemProperties.getMethod("get", String.class,
                    String.class);
            String result = (String) m.invoke(null, key, "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSWValue(Context context) {// 以后传给服务端
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // dm.densityDpi: The screen density expressed as dots-per-inch
        // 屏幕密度（每寸像素：120/160/240/320）
        // dm.density;//屏幕密度（像素比例：0.75/1.0/1.5/2.0） perDip
        String ret = null;
        if (dm.heightPixels <= 720) {
            ret = "sw720";
        } else {
            ret = "sw1080";
        }
        return ret;
    }

    public static String getMediaParams() {
        String strProp = getSystemProperties("ro.media.ability");
        if (strProp != null && !TextUtils.isEmpty(strProp)) {
            return strProp;
        }
        return "";
    }

    public static String getChargeType(String def) {
        String result = def;

        if (TextUtils.isEmpty(result)) {
            result = "2,3,5";
        }
        return result;
    }

    /**
     * ro.yunos.domain.aliyingshi.contents 视频来源0淘TV,6优酷，4搜狐,5爱奇艺 给魔盒用，默认不配置。一体机才会配置。
     * 2.7魔盒参数0,3,4 ;
     * 2.8一体机参数:0,3,4,5
     * 代表客户端的能力，只升不降
     *
     * @return
     */
    public static String getContents(String def) {
        String result = def;

        try {
            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
            Method m = SystemProperties.getMethod("get", String.class, String.class);
            String tmp = (String) m.invoke(null, "ro.yunos.domain.aliyingshi.cts", "falsenull");

            if (!TextUtils.isEmpty(tmp)) {
                if ("falsenull".equals(tmp)) {
                } else {
                    result = tmp;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    // 牌照TODO
    public static String getLicense(String def) {
        String license = def;

        try {
            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
            Method m = SystemProperties.getMethod("get", String.class, String.class);
            String result = (String) m.invoke(null, "ro.yunos.domain.license", "falsenull");
            if (!TextUtils.isEmpty(result)) {
                if ("falsenull".equals(result)) {
                    Log.w("System", "domain yingshi mtop is unknow!!!");
                } else {
                    license = result.trim();
                }
            }
        } catch (Exception e) {
            Log.w("System", "getLicense: error");
        }
        return license;
    }

    public static JSONObject getLocalInfo(Context context) {
        JSONObject result = new JSONObject();
        if (context != null) {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            String country = locale.getCountry();
            try {
                result.put("language", language != null ? language : "");
                result.put("country", country != null ? country : "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            if (!TextUtils.isEmpty(packageName)) {
                PackageManager pm = context.getPackageManager();

                return pm.getPackageInfo(packageName, 0);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getSystemInfo(Context context) {
        JSONObject systemInfo = new JSONObject();

        //通用数据
        try {
            String uuid = getDeviceID();

            systemInfo.put("device_sn", getSubStr(uuid, 32, false));
            systemInfo.put("device_model", Build.MODEL);
            systemInfo.put("device_system_version", Build.VERSION.RELEASE);
            systemInfo.put("device_firmware_version", Build.VERSION.RELEASE);
            systemInfo.put("uuid", uuid);
            systemInfo.put("sw", getSWValue(context));
            systemInfo.put("from", 7);
            systemInfo.put("bcp", 7);
            systemInfo.put("device_media", "dts,dolby,drm,h265_720p");
//            systemInfo.put("device_media", getMediaParams());
            String areaCode = getSystemProperties("persist.sys.asr.areacode");
            if (null != areaCode) {
                systemInfo.put("area_code", areaCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int versionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo yingshiPackageInfo = pm.getPackageInfo(
                        "com.yunos.tv.yingshi.boutique",
                        PackageManager.GET_UNINSTALLED_PACKAGES);
                versionCode = yingshiPackageInfo.versionCode;
            } catch (Exception e) {
                PackageInfo homeshellPackageInfo = pm.getPackageInfo(
                        "com.yunos.tv.homeshell",
                        PackageManager.GET_UNINSTALLED_PACKAGES);
                versionCode = homeshellPackageInfo.versionCode;
            }
            systemInfo.put("version_code", versionCode);  //影视或桌面的版本

          /*  if (versionCode >= 2100208000 && versionCode < 2100209000) {
                // 2.8版本
                systemInfo.put("bcp", getLicense("1"));// 牌照方，1华数，2银河
                systemInfo.put("charge_type", getChargeType("2,3,5"));
                systemInfo.put("from", getContents("0,3,4,5"));// 视频来源0淘TV,3优酷，4搜狐,5爱奇艺;"0,3,4,5"
            } else if (versionCode >= 2100205000) {
                // 2.5，2.6，2.7版本或者3.0
                systemInfo.put("bcp", getLicense("1"));// 牌照方，1华数，2银河
                systemInfo.put("charge_type", getChargeType("2,3,5"));
                systemInfo.put("from", getContents("0,3,4"));// 视频来源0淘TV,3优酷，4搜狐,5爱奇艺;"0,3,4,5"
            }*/

            systemInfo.put("bcp", getLicense("1"));// 牌照方，1华数，2银河
            systemInfo.put("charge_type", getChargeType("3"));
            systemInfo.put("from", getContents("3"));// 视频来源0淘TV,3优酷，4搜狐,5爱奇艺;"0,3,4,5"
        } catch (Exception e) {

        }
        return systemInfo;
    }


    public static JSONObject getSupportPackageInfo(Context context) {
        JSONObject packageInfo = new JSONObject();

        List<SupportApp.LocalAppInfo> localSupportAppList = SupportApp.getSupportApp(context);
        try {
            if (localSupportAppList != null && !localSupportAppList.isEmpty()) {
                for (SupportApp.LocalAppInfo localAppInfo : localSupportAppList) {
                    packageInfo.put(localAppInfo.packageName, String.valueOf(localAppInfo.versionCode));
                }
            } else {
                //默认影视
                packageInfo.put("com.yunos.tv.yingshi.boutique", 2100501118);
            }
        } catch (Exception e) {

        }
        return packageInfo;
    }
}
