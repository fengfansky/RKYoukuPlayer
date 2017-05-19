package com.youku.player.ddemo;

/**
 * Created by fanfeng on 2017/5/9.
 */

public interface VideoCommand {
    void startPlayUri(String uri);

    void startPlay(String vid);

    void pausePlay();

    void resumePlay();

    void stopPlay();

    boolean isStartedPlay();
}
