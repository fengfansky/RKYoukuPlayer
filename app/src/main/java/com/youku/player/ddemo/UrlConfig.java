package com.youku.player.ddemo;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

/**
 * Created by fanfeng on 2017/5/14.
 */

public class UrlConfig {

    public static final String TAG = "yunos";

    public static final String YUNOS_SEARCH_URL = "http://gw.api.taobao.com/router/rest";

    public static String getUrl(String params) {
        if (TextUtils.isEmpty(params))
            return YUNOS_SEARCH_URL;

        String baseUrl = YUNOS_SEARCH_URL + "?";

//        String paramStr = getParamStr(params);

        return baseUrl.concat(params);
    }


    public static String getParamStr(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Log.d(TAG, "param : " + sb.toString());
        return sb.toString();
    }

}
