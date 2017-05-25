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

import cn.com.mma.mobile.tracking.util.Logger;
import rokid.os.RKTTS;
import rokid.os.RKTTSCallback;

/**
 * Created by fanfeng on 2017/5/8.
 */

public class CommandController {

    public static final String TAG = "CommandController";

    private static final String KEY_NLP = "nlp";
    private static final String KEY_INTENT = "intent";

    private static final String MSG_URI = "uri";
    private static final String MSG_VID = "vid";
    private static final String MSG_NAME = "name";


    private static final int MSG_NO_RESULT = 0;
    private static final int MSG_TYPE_URI = 1;
    private static final int MSG_TYPE_VID = 2;

    private static final String SKILL_START = "playmovie";
    private static final String SKILL_PAUSE = "pausemovie";
    private static final String SKILL_RESUME = "resumemovie";
    private static final String SKILL_STOP = "stopmovie";
    private static final String SKILL_FORWARD = "forward";
    private static final String SKILL_BACKWARD = "backward";
    private static final String SKILL_SEEKTO = "seek_to";

    // 测试视频vid
//    private String vid = "XMTQxNjc1MDE0OA==";
//    private String vid = "XMTU3NDc4MzU1Ng==";

    private YoukuSearchProcessor searchProcessor;
    private VideoCommand videoCommand;

    Context mContext;
    private RKTTS rktts;

    public CommandController(Context mContext) {
        this.mContext = mContext;
    }

    public void setVideoCommand(VideoCommand videoCommand) {
        this.videoCommand = videoCommand;
    }

    public void startParseCommand(Intent intent) {
        if (isIntentValidate(intent)) {
            Log.d(TAG, "result startParseCommand intent invalidate!");
            return;
        }
        String nlp = intent.getStringExtra(KEY_NLP);
        if (TextUtils.isEmpty(nlp)) {
            Log.d(TAG, "result NLP is empty!!!");
            return;
        }
        searchProcessor = YoukuSearchProcessor.getInstance();
        rktts = new RKTTS();

        Log.d(TAG, "result  Nlp ---> " + nlp);

        NLPBean nlpBean = new Gson().fromJson(nlp, NLPBean.class);
        String intentEnvent = nlpBean.getIntent();

        Log.d(TAG,"result intentEvent : " + intentEnvent);

        switch (intentEnvent) {
            case SKILL_START:
                Map<String, String> slots = nlpBean.getSlots();

                final String movieName = slots.get("movie");
                String director = slots.get("director");
                String keyword = null;

                if (!TextUtils.isEmpty(movieName)) {
                    keyword = movieName;
                } else if (!TextUtils.isEmpty(director)) {
                    keyword = director;
                }
                if (videoCommand == null || TextUtils.isEmpty(keyword)) {
                    return;
                }

                final String finalKeyword = keyword;
                Logger.d("result finalKeyword " + keyword);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        searchProcessor.getSearchData(mContext, finalKeyword, false, 1, 20, new YoukuSearchProcessor.SearchVidResponseCallback() {
                            @Override
                            public void processVideoList(List<SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean> videoList) {
                                if (videoList == null || videoList.isEmpty()) {
                                    Log.i(TAG, "result invalidate videoList !");
                                    return;
                                }

                                for (SearchVidResponseBody.YoukuTvShowGetResponseBean.ResultBean.VideoBean videoBean : videoList) {
                                    Log.i(TAG, "result processVideoList videoBean : " + videoBean.toString());
                                }
                            }

                            @Override
                            public void processSuccessResult(String vid, String videoName) {
                                Log.d(TAG, "result processSuccessResult vid " + vid);
                                Message message = new Message();
                                message.what = MSG_TYPE_VID;
                                Bundle bundle = new Bundle();
                                bundle.putString(MSG_NAME, videoName);
                                bundle.putString(MSG_VID, vid);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }

                            @Override
                            public void processNoVidResult(String pid) {

                                Log.d(TAG, "result processNoVidResult pid " + pid);
                            }

                            @Override
                            public void processErrorResult() {
                                Log.d(TAG, "result processErrorResult");
                                handler.sendEmptyMessage(MSG_NO_RESULT);
                            }

                            @Override
                            public void processVideoUri(String uri) {

                                Log.d(TAG, "result processVideoUri uri " + uri);
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
                break;
            case SKILL_FORWARD:
                videoCommand.forward();
                break;
            case SKILL_BACKWARD:
                videoCommand.backward();
                break;
            case SKILL_SEEKTO:
//                videoCommand.seekTo();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_NO_RESULT:
                    rktts.speak("亲爱的，我还没有这个资源哦！", new RKTTSCallback());

                case MSG_TYPE_URI:
                    String uri = (String) msg.getData().get(MSG_URI);
                    if (!TextUtils.isEmpty(uri)) {
                        Log.i(TAG, "handle uri " + uri);
                        videoCommand.startPlayUri(uri);
                    }
                    break;
                case MSG_TYPE_VID:
                    String videoName = (String) msg.getData().get(MSG_NAME);
                    if (TextUtils.isEmpty(videoName)) {
                        rktts.speak("亲爱的，我要开始播放了哦！", new RKTTSCallback());
                    } else {
                        rktts.speak("亲爱的，我来为你播放" + videoName, new RKTTSCallback());
                    }
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
