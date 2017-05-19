package com.taobao.api;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by lizhi.ywp on 2017-05-17.
 */

public class TopApi {
    private static final String TAG = "TopApi";

    private static TopApi gInstance;
    private TaobaoClient mTopClient;
    private Context mContext;

    public static TopApi getInstance(Context context,
                                     String serverUrl,
                                     String appKey,
                                     String appSecret) {
        if (null == gInstance) {
            synchronized (TopApi.class) {
                if (null == gInstance) {
                    gInstance = new TopApi(context, serverUrl, appKey, appSecret);
                }
            }
        }
        return gInstance;
    }

    public static TopApi getInstance() {
        return gInstance;
    }

    private TopApi(Context context, String serverUrl, String appKey, String appSecret) {
        DefaultTaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
        client.setNeedEnableParser(false);
        mTopClient = client;
        mContext = context.getApplicationContext();
    }

    private <T extends TaobaoResponse> void toHttpRequest(TaobaoRequest<T> request) {
        Map<String, String> param = request.getTextParams();
        StringBuilder sbd = new StringBuilder(request.getApiMethodName() + ": ");
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                sbd.append(key).append("=").append(param.get(key)).append(", ");
            }
            sbd.setLength(sbd.length() - 2); // delete the last comma
        }
        Log.d(TAG, sbd.toString());
    }

    public TaobaoResponse execute(TaobaoRequest request) {
        TaobaoResponse rsp = null;

        request.setTimestamp(ServerTimeUtil.getTime());

        try {
            toHttpRequest(request);
            rsp = mTopClient.execute(request);
            Log.d(TAG, "ret = " + rsp.toString());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp;
    }
}
