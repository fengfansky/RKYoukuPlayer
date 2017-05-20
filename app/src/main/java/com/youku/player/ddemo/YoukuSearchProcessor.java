package com.youku.player.ddemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.TopApi;
import com.taobao.api.request.CheckinstallAppRequest;
import com.taobao.api.request.SearchRequest;
import com.taobao.api.request.YoukuRequest;
import com.taobao.api.response.SearchVidResponseBody;
import com.taobao.api.response.TvSearchResponseBody;
import com.taobao.api.response.YunosTvsearchCheckinstallappGetResponse;
import com.taobao.api.response.YunosTvsearchDataSearchResponse;
import com.taobao.api.utils.CommUtils;

import java.util.List;

/**
 * Created by fanfeng on 2017/5/18.
 */

public class YoukuSearchProcessor {

    private static final String TAG = "YoukuSearchProcessor";
    private static final String APP_PACKAGE = "com.yunos.tv.homeshell";


    public static YoukuSearchProcessor getInstance() {
        return SearchHolder.instance;
    }

    private static class SearchHolder {
        private static final YoukuSearchProcessor instance = new YoukuSearchProcessor();
    }

    private YunosTvsearchCheckinstallappGetResponse getCheckInstallApp() {
        TopApi topApi = TopApi.getInstance();

        CheckinstallAppRequest request = new CheckinstallAppRequest();

        YunosTvsearchCheckinstallappGetResponse response = (YunosTvsearchCheckinstallappGetResponse) topApi.execute(request);
        Log.e(TAG, response.toString());

        return response;
    }

    public void getSearchData(Context context, String keyword, boolean spell,
                              int page, int pageSize, SearchVidResponseCallback vidResponseCallback) {
        SearchRequest request = new SearchRequest();

        SearchRequest.TvSearchQuery query = new SearchRequest.TvSearchQuery();
        query.setPage((long) page);
        query.setPageSize((long) pageSize);
        query.setApp(APP_PACKAGE);
        query.setKeyword(keyword);
        query.setSpell(spell);
        query.setSystemInfo(CommUtils.getSystemInfo(context).toString());
        query.setLocaleInfo(CommUtils.getLocalInfo(context).toString());
        query.setUuid(CommUtils.getDeviceID());
        query.setPackageInfo(CommUtils.getSupportPackageInfo(context).toString());

        request.setQuery(query);
        TopApi topApi = TopApi.getInstance();


        YunosTvsearchDataSearchResponse response = (YunosTvsearchDataSearchResponse) topApi.execute(request);

        Log.d(TAG, "result  : " + response.getBody());

        if (response.getBody() == null) {
            vidResponseCallback.processErrorResult();
            return;
        }

        TvSearchResponseBody tvSearchBody = new Gson().fromJson(response.getBody(), TvSearchResponseBody.class);

        if (tvSearchBody == null) {
            vidResponseCallback.processErrorResult();
            return;
        }

        TvSearchResponseBody.YunosTvsearchDataSearchResponseBean searchResponse = tvSearchBody.getYunos_tvsearch_data_search_response();

        if (searchResponse == null) {
            vidResponseCallback.processErrorResult();
            return;
        }

        TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean model = searchResponse.getModel();
        if (model == null) {
            Log.i(TAG, "result model invalidate");
            vidResponseCallback.processErrorResult();
            return;
        }

        List<TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean.ListBeanX.ResultBean> resultBeanList = model.getList().getResult();

        if (resultBeanList == null || resultBeanList.isEmpty()) {
            Log.d(TAG, " result resultBeanList invalidate");
            vidResponseCallback.processErrorResult();
            return;
        }

        boolean isVidExist = false;

        for (TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean.ListBeanX.ResultBean resultBean : resultBeanList) {
            TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean.ListBeanX.ResultBean.ListBean dataList = resultBean.getList();
            if (dataList == null) {
                Log.d(TAG, "result result.getList invalidate!");
                continue;
            }
            List<TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean.ListBeanX.ResultBean.ListBean.DataBean> data = dataList.getData();
            if (data == null) {
                Log.d(TAG, "result bean invalidate");
                continue;
            }
            for (TvSearchResponseBody.YunosTvsearchDataSearchResponseBean.ModelBean.ListBeanX.ResultBean.ListBean.DataBean dataBean : data) {
                if (dataBean == null) {
                    Log.d(TAG, "result dataBean invalidate");
                    continue;
                }

                int showType = dataBean.getShow_type();

                String pId = dataBean.getId();
                if (TextUtils.isEmpty(pId)) {
                    Log.i(TAG, "result pId invalidate! ");
                    continue;
                }

                if (!keyword.equals(dataBean.getMatch_word())) {
                    Log.i(TAG, "result keyword not match continue!");
                    continue;
                }

                Log.i(TAG, "dataBean title : " + dataBean.getTitle() + " pid : " + dataBean.getId());

                try {
                    boolean isGetVid = getVid(pId, showType, vidResponseCallback);
                    if (!isGetVid) {
                        vidResponseCallback.processNoVidResult(pId);
                        continue;
                    } else {
                        isVidExist = isGetVid;
                        return;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }
        if (!isVidExist) {
            vidResponseCallback.processErrorResult();
        }
    }


    public boolean getVid(String pId, int showType, SearchVidResponseCallback vidResponseCallback) {
        YoukuRequest request = new YoukuRequest();
        request.setShowId(pId);
        TopApi topApi = TopApi.getInstance();
        TaobaoResponse response = topApi.execute(request);

        Log.d("TAG", "pId : " + pId + "  showType:  " + showType + "    get vid response : " + response.getBody());

        SearchVidResponseBody tvSearchBody = new Gson().fromJson(response.getBody(), SearchVidResponseBody.class);

        if (tvSearchBody == null) {
            Log.d(TAG, " tvSearchBody is null !!!");
            return false;
        }

        SearchVidResponseBody.YoukuTvShowGetResponseBean searchVidResponse = tvSearchBody.getYouku_tv_show_get_response();

        if (searchVidResponse == null) {
            Log.i(TAG, "searchVidResponse is null !");
            return false;
        }

        SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean searchVidResult = new Gson().fromJson(searchVidResponse.getResult(), SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.class);

        if (searchVidResult == null) {
            Log.i(TAG, "searchResult is null !!!");
            return false;
        }

        List<SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean> videoList = searchVidResult.getVideoBean();


        if (videoList == null || videoList.isEmpty()) {
            Log.i(TAG, "getVid videoList invalidate !");
            return false;
        }

        vidResponseCallback.processVideoList(videoList);

        String vid = videoList.get(0).getExtVideoStrId();
        String videoName = videoList.get(0).getRcTitle();
        vidResponseCallback.processSuccessResult(vid, videoName);

        Log.i(TAG, "result vid : " + vid + " videoName : " + videoName);
        return true;
    }

    public interface SearchVidResponseCallback {
        void processVideoList(List<SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean> videoList);

        void processSuccessResult(String vid, String videoName);

        void processNoVidResult(String pid);

        void processErrorResult();

        void processVideoUri(String uri);
    }
}
