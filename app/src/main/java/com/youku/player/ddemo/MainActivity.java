package com.youku.player.ddemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.youku.player.LoadFailure;
import com.youku.player.PlayerError;
import com.youku.player.PlayerErrorInfo;
import com.youku.player.PlayerMonitor;
import com.youku.player.YoukuVideoPlayer;
import com.youku.player.entity.PlayItemBuilder;
import com.youku.player.manager.AdvertShow;
import com.youku.player.manager.AdvertType;
import com.youku.player.manager.VideoPlayType;
import com.youku.player.widget.YoukuScreenView;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivityTEST";
    // SDK自定义View
    private YoukuScreenView mYoukuScreenView;
    // 播放器实例
    private YoukuVideoPlayer mYoukuVideoPlayer;
    private CommandController intentProcessor;
    private VideoCommand videoCommand;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.youku);

        initPlayer();
        initCommand();
        intentProcessor.setImageController(new CommandController.ImageController() {
            @Override
            public void show() {
                imageView.setVisibility(View.VISIBLE);
                mYoukuScreenView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void dismiss() {
                imageView.setVisibility(View.INVISIBLE);
                mYoukuScreenView.setVisibility(View.VISIBLE);
            }
        });
        intentProcessor.startParseCommand(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intentProcessor.startParseCommand(intent);
        setIntent(intent);
    }

    private void initPlayer() {
        mYoukuScreenView = (YoukuScreenView) findViewById(R.id.screen_container);
        //初始化YoukuVideoPlayer
        mYoukuVideoPlayer = new YoukuVideoPlayer(this);
        // 设置YOUKUScreenView
        mYoukuVideoPlayer.setScreenView(mYoukuScreenView);
        // 设置屏幕显示百分比 -1 全屏 , 100 等比显示
        mYoukuVideoPlayer.setPlayerScreenPercent(-1);
        // 设置播放器回调监听
        mYoukuVideoPlayer.setPlayerMonitor(mPlayerMonitor);
        // 设置当前播放清晰度(1-标清,2-高清,3-超清,4-1080P,5-4K)， 默认为高清
        mYoukuVideoPlayer.setPreferDefinition(4);
        // 设置播放统计回掉


//        mYoukuVideoPlayer.setPlayStatCallback(null);
        //mYoukuScreenView.showLoadingPageView(BitmapFactory.decodeResource(getResources(), R.drawable.youku), View.VISIBLE);
    }

    private void initCommand() {
        intentProcessor = new CommandController(this);
        videoCommand = new VideoPlayCommand(MainActivity.this, mYoukuVideoPlayer);
        intentProcessor.setVideoCommand(videoCommand);
    }

    private void playIndex(int index) {
        mYoukuVideoPlayer.playIndex(index);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
        // 设置数据统计结束状态
        if (videoCommand != null)
            videoCommand.pausePlay();

//        mYoukuVideoPlayer.endSession(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoCommand.isStartedPlay()) {
            Log.d(TAG, "onResume startPlay : " + videoCommand.isStartedPlay());
            videoCommand.resumePlay();
        }
        // 设置数据统计开始状态
//        mYoukuVideoPlayer.startSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放播放器资源
        Log.d(TAG, "onDestroy");
        if (mYoukuVideoPlayer != null) {
            mYoukuVideoPlayer.release();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    //播放回调
    private final PlayerMonitor mPlayerMonitor = new PlayerMonitor() {
        // 播放过程中，正片视频播放点击屏幕时回调
        @Override
        public void onVideoClick() {

            Log.d(MainActivity.TAG, "onVideoClick");
        }

        // 视频暂停
        @Override
        public void onStop() {

            Log.d(MainActivity.TAG, "onStop");
        }

        // 开始加载视频信息
        @Override
        public void onStartLoading() {

            Log.d(MainActivity.TAG, "onStartLoading");
        }

        // 已经为您跳过片尾, tail:片尾时长(秒)
        @Override
        public void onSkipTail(int tail) {
            Log.d(MainActivity.TAG, "onSkipTail");

        }

        // 已经为您跳过片头, head:片头时长(秒)
        @Override
        public void onSkipHeader(int head) {
            Log.d(MainActivity.TAG, "onSkipHeader");

        }

        // 暂停广告显示
        @Override
        public void onShowPauseAdvert() {
            Log.d(MainActivity.TAG, "onShowPauseAdvert");

        }

        // 播放进度发生了变化时的回调      currentPosition 当前播放的时间点(毫秒) secondProgress 第二缓冲进度(毫秒)， duration 视频的总长(毫秒)
        @Override
        public void onProgressUpdated(int currentPosition, int secondProgress, int duration) {
            Log.d(MainActivity.TAG, "onProgressUpdated currentPos : " + currentPosition + " ,duration : " + duration);

        }

        // 上一集下一集状态变化 boolean previous, boolean next
        @Override
        public void onPreviousNextStateChange(boolean previous, boolean next) {
            Log.d(MainActivity.TAG, "onPreviousNextStateChange");

        }

        // 视频开始准备
        @Override
        public void onPreparing() {
            Log.d(MainActivity.TAG, "onPreparing");
        }

        // 视频准备完成
        @Override
        public void onPrepared() {
            Log.d(MainActivity.TAG, "onPrepared");
        }

        // 自动连播并有播放列表时，当前集播放结束.
        @Override
        public void onPlayOver(PlayItemBuilder arg0) {
            Log.d(MainActivity.TAG, "onPlayOver");

        }

        // 播放对象发生变化
        @Override
        public void onPlayItemChanged(PlayItemBuilder builder, int index) {

            Log.d(MainActivity.TAG, "onPlayItemChanged");
        }

        // 视频开始播放
        @Override
        public void onPlay() {
            Log.d(MainActivity.TAG, "onPlay");

        }

        // 视频暂停
        @Override
        public void onPause() {
            Log.d(MainActivity.TAG, "onPause");

        }

        // 视频加载成功
        @Override
        public void onLoadSuccess() {
            Log.d(MainActivity.TAG, "onLoadSuccess");

        }

        // 视频信息加载失败
        @Override
        public void onLoadFail(LoadFailure failure, HashMap<String, Object> params) {
            Log.d(MainActivity.TAG, "onVideoClick");

        }

        // 暂停广告消失
        @Override
        public void onDismissPauseAdvert() {
            Log.d(MainActivity.TAG, "onVideoClick");

        }

        // 清晰度发生改变, change 0: 变换开始, 1:变换完成
        @Override
        public void onDefinitionChanged(int change) {

            Log.d(MainActivity.TAG, "onVideoClick");
        }

        // 硬解状态的回调
        @Override
        public void onDecodeChanged(boolean arg0, int arg1, int arg2) {

            Log.d(MainActivity.TAG, "onVideoClick");
        }

        // 视频播放完成
        @Override
        public void onComplete() {
            Log.d(MainActivity.TAG, "onVideoClick");

        }

        // 缓冲进度 size unit KB/S
        @Override
        public void onBufferingSize(int size) {
            Log.d(MainActivity.TAG, "onBufferingSize");

        }

        /**
         * 视频缓冲进度
         *
         * @param type
         *              - 0   Buffer Start
         *              - 1   Buffering
         *              - 2   Buffer End
         * @param isSystemPlayer
         *              - 是否是系统播放器
         * @param progress
         *              - 缓冲进度
         */
        @Override
        public void onBuffering(int type, boolean isSystemPlayer, int progress) {

            Log.d(MainActivity.TAG, "onBuffering");
        }

        /**
         * 缓冲动作完成回调
         */
        @Override
        public void onSeekComplete() {
            Log.d(MainActivity.TAG, "onSeekComplete");
        }

        /**
         * 广告播放回调
         * @param advertType 广告类型
         * ADVERT_PRE,前贴
         * ADVERT_MID, 中插
         * ADVERT_BACK; 后贴(暂无)
         * @param advertShow 广告播放情况     AD_SHOW_START, 广告开始， AD_SHOW_COMPLETE广告结束;
         */
        @Override
        public void onAdvertPlay(AdvertType advertType, AdvertShow advertShow) {
            Log.d(MainActivity.TAG, "onAdvertPlay");
        }

        /**
         * 视频出错
         *
         * @param error 错误类型
         * @param peInfo 具体信息
         * @param obj 保留
         */
        @Override
        public void onError(PlayerError error, PlayerErrorInfo peInfo, Object obj) {
            Log.d(MainActivity.TAG, "onError");
        }

        /**
         * 播放视频类型
         * VIDEO_NORMAL,正片
         * VIDEO_TRY, 试看片
         * VIDEO_CANNOT_TRY; 不能试看
         * @param type
         */
        @Override
        public void onVideoType(VideoPlayType type) {

        }

        /**
         * 切换语言开始 change 0: 开始, 1:结束
         * @param change
         */
        @Override
        public void onLanguageChanged(int change) {

        }

        /**
         * 中插广告播放开始前10秒通知
         */
        @Override
        public void onMidAdvertWillPlay() {

        }
    };


}
