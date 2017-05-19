package com.youku.player.ddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.taobao.api.response.SearchVidResponseBody;
import com.youku.bean.NLPBean;

import java.util.List;
import java.util.Map;

/**
 * Created by fanfeng on 2017/5/8.
 */

public class CommandProcessor {

    public static final String TAG = "CommandProcessor";

    private static final String KEY_NLP = "nlp";
    private static final String KEY_INTENT = "intent";

    private static final String MSG_VID = "vid";
    private static final String MSG_URI = "uri";

    private static final int MSG_TYPE_URI = 1;
    private static final int MSG_TYPE_VID = 2;

    private static final String SKILL_START = "playmovie";
    private static final String SKILL_PAUSE = "pausemovie";
    private static final String SKILL_RESUME = "resumemovie";
    private static final String SKILL_STOP = "stopmovie";

    // 测试视频vid
//    private String vid = "XMTQxNjc1MDE0OA==";
//    private String vid = "XMTU3NDc4MzU1Ng==";

    private VideoCommand videoCommand;

    Context mContext;

    public CommandProcessor(Context mContext) {
        this.mContext = mContext;
    }

    public void setVideoCommand(VideoCommand videoCommand) {
        this.videoCommand = videoCommand;
    }

    public void startParse(Intent intent) {
        if (isIntentValidate(intent)) {
            Log.d(TAG, "startParse intent invalidate!");
            return;
        }
        String nlp = intent.getStringExtra(KEY_NLP);
        if (TextUtils.isEmpty(nlp)) {
            Log.d(TAG, "NLP is empty!!!");
            return;
        }
        Log.d(TAG, "parseIntent Nlp ---> " + nlp);

        NLPBean nlpBean = new Gson().fromJson(nlp, NLPBean.class);
        String intentEnvent = nlpBean.getIntent();

        Map<String, String> slots = nlpBean.getSlots();

        final String movieName = slots.get("movie");

        switch (intentEnvent) {
            case SKILL_START:
                if (videoCommand == null || TextUtils.isEmpty(movieName)) {
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        YoukuSearchProcessor searchProcessor = new YoukuSearchProcessor();
                        searchProcessor.getSearchData(mContext, movieName, false, 1, 20, new YoukuSearchProcessor.SearchVidResponseCallback() {
                            @Override
                            public void processVideoList(List<SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean> videoList) {
                                if (videoList == null || videoList.isEmpty()) {
                                    Log.i(TAG, "invalidate videoList !");
                                    for (SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean videoBean : videoList) {
                                        Log.i(TAG, "processVideoList videoBean : " + videoBean.toString());
                                    }

                                }

                            }

                            @Override
                            public void processSuccessResult(String vid) {
                                Log.d(TAG, "processSuccessResult vid " + vid);
                                Message message = new Message();
                                message.what = MSG_TYPE_VID;
                                Bundle bundle = new Bundle();
                                bundle.putString(MSG_VID, vid);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }

                            @Override
                            public void processEmptyResult(String pid) {

                                Log.d(TAG, "processEmptyResult pid " + pid);
                            }

                            @Override
                            public void processVideoUri(String uri) {

                                Log.d(TAG, "processVideoUri uri " + uri);
                                Message message = new Message();
                                message.what = MSG_TYPE_URI;
                                Bundle bundle = new Bundle();
                                bundle.putString(MSG_URI, uri);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }
                        });
                    }
                }).start();

                break;
            case SKILL_PAUSE:
                videoCommand.pausePlay();
                break;
            case SKILL_RESUME:
                videoCommand.resumePlay();
                break;
            case SKILL_STOP:
                videoCommand.stopPlay();

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_TYPE_URI:
                    String uri = (String) msg.getData().get(MSG_URI);
                    if (!TextUtils.isEmpty(uri)) {
                        Log.i(TAG, "handle uri " + uri);
                        videoCommand.startPlayUri(uri);
                        return;
                    }
                    break;
                case MSG_TYPE_VID:
                    String vid = (String) msg.getData().get(MSG_VID);
                    if (!TextUtils.isEmpty(vid)) {
                        Log.i(TAG, "handle vid " + vid);
                        videoCommand.startPlay(vid);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private boolean isIntentValidate(Intent intent) {
        if (null == intent) {
            Log.d(TAG, "Intent is null!!!");
            return true;
        }
        return false;
    }
}
