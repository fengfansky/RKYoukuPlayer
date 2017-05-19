package com.youku.http.request;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youku.bean.base.BaseBean;
import com.taobao.api.Constants;
import com.youku.player.ddemo.AppConstants;
import com.youku.player.ddemo.TimeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by fanfeng on 2017/5/15.
 */

public class YunOsSearchRequest extends BaseBean {

    public static final String TAG = "yunos";

    private String method;
    private String app_key;
    private String sign_method;
    private String sign;
    private String timestamp;
    private String format;
    private String v;
    private String query;

    //API接口名称
    public static final String PARAM_KEY_METHOD = "method";

    //TOP分配给应用的AppKey
    public static final String PARAM_KEY_APP_KEY = "app_key";
    //签名的摘要算法，可选值为：hmac，md5
    public static final String PARAM_KEY_SIGN_METHOD = "sign_method";

    /*签名算法
    参考： http://open.taobao.com/docs/doc.htm?spm=a219a.7395905.0.0.BQ8DAE&articleId=101617&docType=1&treeId=1*/
    public static final String PARAM_KEY_SIGN = "sign";
    //时间戳，格式为yyyy-MM-dd HH:mm:ss
    public static final String PARAM_KEY_TIME_STAMP = "timestamp";

    //响应格式。默认为xml格式，可选值：xml，json
    public static final String PARAM_KEY_FORMAT = "format";

    //API协议版本，可选值：2.0。
    public static final String PARAM_KEY_V = "v";

    public static final String PARAM_VALUE_APP_KEY = AppConstants.APP_KEY;
    public static final String PARAM_VALUE_SIGN_METHOD = Constants.SIGN_METHOD_MD5;

    public static final String PARAM_VALUE_V = "2.0";
    public static final String PARAM_VALUE_FORMAT = "json";

    //API接口名称
    public static final String PARAM_VALUE_METHOD = "yunos.tvsearch.data.search";

    public YunOsSearchRequest() {
        this.setMethod(PARAM_VALUE_METHOD);
        this.setApp_key(PARAM_VALUE_APP_KEY);
        this.setSign_method(PARAM_VALUE_SIGN_METHOD);
        this.setTimestamp(TimeUtils.getCurrentTimeStamp());
        this.setFormat(PARAM_VALUE_FORMAT);
        this.setV(PARAM_VALUE_V);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getSign_method() {
        return sign_method;
    }

    public void setSign_method(String sign_method) {
        this.sign_method = sign_method;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        Log.i("yunos", "sign : " + sign);
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, String> toMap() {

        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(super.toString(), stringStringMap);

    }

    private void putUnEmptyParam(Map<String, String> params, String key, String value) {
        if (params == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.d(TAG, "param invalidate ! key " + key + " value : " + value);
            return;
        }
        params.put(key, value);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public String buildQuery() throws IOException {

        Map<String, String> params = toMap();
        Log.i(TAG, "buildQuery params toMap : " + toMap().toString());
        if (params == null) {
            Log.d(TAG, "param is null !!!");
            return null;
        }

        StringBuilder query = new StringBuilder();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue().toString();
            // 忽略参数名或参数值为空的参数
            if (isNotEmpty(name) && isNotEmpty(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, "utf-8"));
            }
        }
        return query.toString();
    }

    private boolean isNotEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
