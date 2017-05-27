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
    private int maxVolume;

    public VideoPlayCommand(Activity activity, YoukuVideoPlayer youkuVideoPlayer) {
        mActivity = activity;
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
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

        isStartedPlay = true;
    }

    @Override
    public void pausePlay() {
        if (mYoukuVideoPlayer == null)
            return;
        mYoukuVideoPlayer.pause();
    }

    @Override
    public void resumePlay() {
        if (mYoukuVideoPlayer == null)
            return;
        mYoukuVideoPlayer.play();
    }

    @Override
    public void stopPlay() {
        if (mYoukuVideoPlayer == null)
            return;
        mActivity.finish();
    }

    @Override
    public boolean isStartedPlay() {
        return isStartedPlay;
    }

    @Override
    public void seekTo(int time) {
        if (mYoukuVideoPlayer == null)
            return;
        int totalTime = mYoukuVideoPlayer.getDuration();
        if (time > totalTime) {
            time = totalTime;
        } else if (time < 0) {
            time = 0;
        }
        Log.d(TAG, "command forward time : " + time + " totalTime : " + totalTime);
        mYoukuVideoPlayer.seekTo(time);
    }

    @Override
    public void forward() {
        if (mYoukuVideoPlayer == null)
            return;
        seekTo(mYoukuVideoPlayer.getCurrentPosition() + mYoukuVideoPlayer.getDuration() / 15);
    }

    @Override
    public void backward() {
        if (mYoukuVideoPlayer == null)
            return;
        seekTo(mYoukuVideoPlayer.getCurrentPosition() - mYoukuVideoPlayer.getDuration() / 15);
    }

    @Override
    public void forwardTime(int time) {
        if (mYoukuVideoPlayer == null)
            return;
        seekTo(mYoukuVideoPlayer.getCurrentDefinition() + time);
    }

    @Override
    public void backwardTime(int time) {
        if (mYoukuVideoPlayer == null)
            return;
        seekTo(mYoukuVideoPlayer.getCurrentDefinition() - time);
    }


    @Override
    public void volumeUp() {
        if (audioManager == null)
            return;
        int positionVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + maxVolume / 8;
        if (positionVolume > maxVolume) {
            positionVolume = maxVolume;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, positionVolume, 0);
    }

    @Override
    public void volumeDown() {
        if (audioManager == null)
            return;
        int positionVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - maxVolume / 8;
        if (positionVolume < 0) {
            positionVolume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, positionVolume, 0);
    }
}
