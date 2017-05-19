package com.youku.player.ddemo;

import com.youku.player.YoukuVideoPlayer;
import com.youku.player.entity.PlayItemBuilder;
import com.youku.player.manager.VideoSourceType;

/**
 * Created by fanfeng on 2017/5/14.
 */

public class VideoPlayCommand implements VideoCommand {

    private YoukuVideoPlayer mYoukuVideoPlayer;
    private boolean isStartedPlay;

    public VideoPlayCommand(YoukuVideoPlayer youkuVideoPlayer) {
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
        if (mYoukuVideoPlayer != null) {
            mYoukuVideoPlayer.stop();
            isStartedPlay = false;
        }
    }

    @Override
    public boolean isStartedPlay() {
        return isStartedPlay;
    }
}
