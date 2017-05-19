package com.youku.http;


import android.content.Context;
import android.util.Log;

import com.taobao.api.TopApi;
import com.taobao.api.request.SearchRequest;
import com.taobao.api.response.YunosTvsearchDataSearchResponse;
import com.taobao.api.utils.CommUtils;
import com.taobao.api.TaobaoResponse;
import com.youku.player.ddemo.UrlConfig;
import com.youku.player.ddemo.AppConstants;
import com.taobao.api.request.TvSearchQuery;
import com.youku.http.request.YunOsSearchRequest;
import com.youku.http.sign.SignUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fanfeng on 2017/5/11.
 */

public class HttpClientWrapper {

    private static final String TAG = "yunos";
    private static OkHttpClient okHttpClient;
    private static final int CONNECTION_TIME_OUT = 5;
    private static final int READ_TIME_OUT = 5;
    private static final int WRITE_TIME_OUT = 5;

    private static final String PARAM_VALUE_APP = "com.yunos.tv.homeshell";

    private static final String SYSTEM_INFO = "{\n" +
            "  \"uuid\": \"E4E32F9EDA4F72DAF56CE11EB52AA274\",\n" +
            "  \"device_model\": \"MagicBox_M17\",\n" +
            "  \"device_system_version\": \"6.0.1\",\n" +
            "  \"device_sn\": \"E4E32F9EDA4F72DA\",\n" +
            "  \"device_firmware_version\": \"6.0.1-D-20161018.0952\",\n" +
            "  \"firmware\": \"6.0.1-D-20161018.0952\",\n" +
            "  \"charge_type\": \"2,3,5\",\n" +
            "  \"sw\": \"sw1080\",\n" +
            "  \"version_code\": 2100402207,\n" +
            "  \"yingshi_version\": 2100402207,\n" +
            "  \"device_media\": \"h265_4k2k,drm\",\n" +
            "  \"mac\": \"02:00:00:00:00:00\",\n" +
            "  \"ethmac\": \"28D229B21FD9\",\n" +
            "  \"from\": \"7,9\",\n" +
            "  \"license\": \"7\",\n" +
            "  \"bcp\": \"7\",\n" +
            "  \"v_model\": \"F\"\n" +
            "}";

    private static final String PACKAGE_INFO = "{\n" +
            "  \"com.youku.player.ddemo\": \"2100402207\",\n" +
            "}";

    private static final String VERSION_CODE = "2100402207";

    TopApi topApi;

    public HttpClientWrapper() {
        okHttpClient = new OkHttpClient().newBuilder()
//                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build();

        topApi = TopApi.getInstance();

    }

    public static HttpClientWrapper getInstance() {
        return SingleHolder.instance;
    }

    public TaobaoResponse getSearchData(Context context, String keyword,
                                        boolean spell, int page, int pageSize) {
        SearchRequest request = new SearchRequest();

        JSONObject query = new JSONObject();

        try {
            query.put("page", page);
            query.put("page_size", pageSize);
            query.put("app", "com.yunos.tv.homeshell");
            query.put("keyword", keyword);
            query.put("spell", spell);
            query.put("system_info", CommUtils.getSystemInfo(context).toString());
            query.put("locale_info", CommUtils.getLocalInfo(context).toString());
            query.put("uuid", CommUtils.getDeviceID());
            query.put("package_info", CommUtils.getSupportPackageInfo(context).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.setQuery(query.toString());

        YunosTvsearchDataSearchResponse response = (YunosTvsearchDataSearchResponse) topApi.execute(request);
        Log.d("getSearchData", "result = " + response.getBody());


        return response;
    }

    public Response request(Context context, String keyword,
                            boolean spell, int page, int pageSize) {

        TvSearchQuery tvSearchQuery = new TvSearchQuery();
        try {
            tvSearchQuery.setPage(page);
            tvSearchQuery.setPage_size(pageSize);
            tvSearchQuery.setApp(URLEncoder.encode(PARAM_VALUE_APP, "utf-8"));
            tvSearchQuery.setKeyword(URLEncoder.encode(keyword, "utf-8"));
            tvSearchQuery.setSpell(spell);
            tvSearchQuery.setSystem_info(URLEncoder.encode(CommUtils.getSystemInfo(context).toString(), "utf-8"));
            tvSearchQuery.setLocale_info(URLEncoder.encode(CommUtils.getLocalInfo(context).toString(), "utf-8"));
            tvSearchQuery.setUuid(CommUtils.getDeviceID());
            tvSearchQuery.setPackage_info(URLEncoder.encode(CommUtils.getSupportPackageInfo(context).toString(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        tvSearchQuery.setSpell(false);
        YunOsSearchRequest yunOsSearchRequest = new YunOsSearchRequest();
        yunOsSearchRequest.setQuery(tvSearchQuery.toString());

        try {
            yunOsSearchRequest.setSign(SignUtils.signTopRequest(yunOsSearchRequest.toMap(), AppConstants.APP_SECREPT, "md5"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String param = null;

        try {
            param = yunOsSearchRequest.buildQuery();
            Log.i(TAG, "param is " + param);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(UrlConfig.getUrl(param))
                .addHeader("Accept", "text/xml,text/javascript")
                .addHeader("User-Agent", "top-sdk-java")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .get()
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static class SingleHolder {
        private static final HttpClientWrapper instance = new HttpClientWrapper();
    }

}
