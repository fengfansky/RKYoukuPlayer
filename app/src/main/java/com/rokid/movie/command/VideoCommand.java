package com.rokid.movie.command;

/**
 * Created by fanfeng on 2017/5/9.
 */

public interface VideoCommand {
    void startPlayUri(String uri);

    void startPlay(String vid);

    void pausePlay();

    void resumePlay();

    void stopPlay();

    void releasePlayer();

    boolean isStartedPlay();

    void seekTo(int time);

    void forward();

    void backward();

    void forwardTime(int time);

    void backwardTime(int time);

    void volumeUp();

    void volumeDown();
}
