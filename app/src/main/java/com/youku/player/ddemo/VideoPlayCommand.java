package com.youku.player.ddemo;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.youku.player.YoukuVideoPlayer;
import com.youku.player.entity.PlayItemBuilder;
import com.youku.player.manager.VideoSourceType;

/**
 * Created by fanfeng on 2017/5/14.
 */

public class VideoPlayCommand implements VideoCommand {

    private static final String TAG = "VideoPlayCommand";
    private YoukuVideoPlayer mYoukuVideoPlayer;
    private boolean isStartedPlay;
    private Activity mActivity;
    private AudioManager audioManager;

    public VideoPlayCommand(Activity activity, YoukuVideoPlayer youkuVideoPlayer) {
        mActivity = activity;
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        this.mYoukuVideoPlayer = youkuVideoPlayer;
    }

    @Override
    public void startPlayUri(String uri) {
        PlayItemBuilder builder = new PlayItemBuilder(uri, false, VideoSourceType.VIDEO_TYPE_NET);
        builder.setStartPosition(0);
        mYoukuVideoPlayer.setDataSource(builder);
        mYoukuVideoPlayer.play();
    }

    @Override
    public void startPlay(String vid) {

        // 用VID初始化PlayItemBuilder
        PlayItemBuilder builder = new PlayItemBuilder(vid);
        // 设置播放开始时间， 有播放记录的情况下，设置播放记录开始播放的位置,单位(秒)
        builder.setStartPosition(0);
        //设置Player的datasource
        mYoukuVideoPlayer.setDataSource(builder);
        mYoukuVideoPlayer.play();
        /*PlayItemBuilder playItemBuilder = new PlayItemBuilder(url,false, VideoSourceType.VIDEO_TYPE_NET);
        playItemBuilder.setStartPosition(0);
        mYoukuVideoPlayer.setDataSource(playItemBuilder);
        mYoukuVideoPlayer.play();*/

        isStartedPlay = true;
    }

    @Override
    public void pausePlay() {
        if (mYoukuVideoPlayer != null) {
            mYoukuVideoPlayer.pause();
        }
    }

    @Override
    public void resumePlay() {
        if (mYoukuVideoPlayer != null) {
            mYoukuVideoPlayer.play();
        }
    }

    @Override
    public void stopPlay() {
        /*if (mYoukuVideoPlayer != null) {
            mYoukuVideoPlayer.stop();
            isStartedPlay = false;
        }*/
        mYoukuVideoPlayer.release();
        mActivity.finish();
    }

    @Override
    public boolean isStartedPlay() {
        return isStartedPlay;
    }

    @Override
    public void seekTo(int time) {
        int totalTime = mYoukuVideoPlayer.getDuration();
        if ( time >= totalTime){
            time = totalTime;
        }
        mYoukuVideoPlayer.seekTo(time);
    }

    @Override
    public void forward() {
        int totalTime = mYoukuVideoPlayer.getDuration();
        int fastTime = totalTime/5;
        Log.d(TAG, "command forward fastTime : " + fastTime + " totalTime : " + totalTime );
        if (mYoukuVideoPlayer.canSeekForward()){
            Log.d(TAG, "command forward execute : " + fastTime + " totalTime : " + totalTime );
            mYoukuVideoPlayer.fastForward(fastTime);
        }else {

        }
    }

    @Override
    public void backward() {
        int totalTime = mYoukuVideoPlayer.getDuration();
        int backwardTime = totalTime/5;
        Log.d(TAG, "command backward backwardTime : " + backwardTime + " totalTime : " + totalTime );
        if (mYoukuVideoPlayer.canSeekBackward()){
            Log.d(TAG, "command backward execute : " + backwardTime + " totalTime : " + totalTime );
            mYoukuVideoPlayer.fastBackward(backwardTime);
        }else {

        }
    }

    @Override
    public void forward(int time) {
        int totalTime = mYoukuVideoPlayer.getDuration();
        if (time >= totalTime){
            time = totalTime;
        }
        mYoukuVideoPlayer.seekTo(time);
    }

    @Override
    public void backward(int time) {
        int totalTime = mYoukuVideoPlayer.getDuration();
        if (time >= totalTime){
            time = totalTime;
        }
        mYoukuVideoPlayer.seekTo(time);
    }

    @Override
    public void volumeUp() {

    }

    @Override
    public void volumeDown() {

    }
}
