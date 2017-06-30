package com.rokid.movie.command;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rokid.movie.processor.YoukuSearchProcessor;
import com.rokid.movie.taobao.api.response.SearchVidResponseBody;
import com.rokid.movie.bean.NLPBean;
import com.rokid.movie.util.TimeParserUtil;

import java.util.List;
import java.util.Map;

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

    private static final String SKILL_WELCOME = "ROKID.INTENT.WELCOME";
    private static final String SKILL_START = "playmovie";
    private static final String SKILL_PAUSE = "pausemovie";
    private static final String SKILL_RESUME = "resumemovie";
    private static final String SKILL_STOP = "stopmovie";
    private static final String SKILL_FORWARD = "forward";
    private static final String SKILL_BACKWARD = "backward";
    private static final String SKILL_SEEKTO = "seek_to";
    private static final String SKILL_VOLUME_UP = "volume_up";
    private static final String SKILL_VOLUME_DOWN = "volume_down";

    private boolean isStarted;
    // 测试视频vid
//    private String vid = "XMTQxNjc1MDE0OA==";
//    private String vid = "XMTU3NDc4MzU1Ng==";

    private ImageController mImageController;

    private YoukuSearchProcessor searchProcessor;
    private VideoCommand videoCommand;

    Context mContext;
    private RKTTS rktts;

    public void setImageController(ImageController imageController) {
        mImageController = imageController;
    }

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
        Log.d(TAG, "result  Nlp ---> " + nlp);

        if (TextUtils.isEmpty(nlp)) {
            Log.d(TAG, "result NLP is empty!!!");
            welcomeTTS();
            return;
        }

        searchProcessor = YoukuSearchProcessor.getInstance();
        rktts = new RKTTS();

        NLPBean nlpBean = new Gson().fromJson(nlp, NLPBean.class);
        String intentEnvent = nlpBean.getIntent();

        Log.d(TAG, "result intentEvent : " + intentEnvent + "  slots " + nlpBean.getSlots());

        switch (intentEnvent) {
            case SKILL_START:
                if (mImageController != null) {
                    mImageController.dismiss();
                }
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
                Log.d(TAG, "nlp result finalKeyword " + keyword);
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
                isStarted = true;
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
            case SKILL_VOLUME_UP:
                videoCommand.volumeUp();
                break;
            case SKILL_VOLUME_DOWN:
                videoCommand.volumeDown();
                break;
            case SKILL_SEEKTO:
                Map<String, String> timeSlots = nlpBean.getSlots();
                Log.d(TAG, "nlp timeSlots : " + timeSlots);
                videoCommand.seekTo(TimeParserUtil.parseTime(timeSlots));
                break;
            case SKILL_FORWARD:
                Map<String, String> forwardTimeSlots = nlpBean.getSlots();
                Log.d(TAG, "nlp forwardTimeSlots : " + forwardTimeSlots);
                if (forwardTimeSlots == null || forwardTimeSlots.isEmpty()) {
                    videoCommand.forward();
                } else {
                    videoCommand.forwardTime(TimeParserUtil.parseTime(forwardTimeSlots));
                }
                break;
            case SKILL_BACKWARD:
                Map<String, String> backwardTimeSlots = nlpBean.getSlots();
                Log.d(TAG, "nlp backwardTimeSlots : " + backwardTimeSlots);
                if (backwardTimeSlots == null || backwardTimeSlots.isEmpty()) {
                    videoCommand.backward();
                } else {
                    videoCommand.backwardTime(TimeParserUtil.parseTime(backwardTimeSlots));
                }
                break;
            case SKILL_WELCOME:
                welcomeTTS();
                break;
            default:
                //TODO unKnow
//                welcomeTTS();
                Log.d(TAG, "unKnow command !!! " + intentEnvent);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_NO_RESULT:
                    rktts.speak("抱歉，暂时没有这部电影。试试找找看其他电影吧！", new RKTTSCallback());

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
                        rktts.speak("好的，即将为您播放该影片！", new RKTTSCallback());
                    } else {
                        rktts.speak("好的，即将为您播放" + videoName, new RKTTSCallback());
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

    private void welcomeTTS() {
        RKTTS tts = new RKTTS();
        tts.speak("欢迎来到优酷电影，请问您想看哪一部电影？", new RKTTSCallback());
        if (mImageController != null) {
            mImageController.show();
        }
    }

    public interface ImageController {
        void show();

        void dismiss();
    }

}
