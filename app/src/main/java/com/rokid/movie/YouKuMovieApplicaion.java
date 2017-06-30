package com.rokid.movie;

import android.app.Application;

import com.rokid.movie.taobao.api.TopApi;
import com.youku.player.YoukuVideoPlayer;
import com.youku.player.manager.PlayDataParams;
import com.youku.player.manager.Site;
import com.youku.player.setting.ReleaseConfig;


public class YouKuMovieApplicaion extends Application {
    // 是否使用CIBN域名, true:cibn域名, false:优酷域名
    private static boolean isCibn = false;
    // 是否打开LOG开关, true:打开, false:关闭
    private static boolean isDebug = true;
    // 是否使用测试地址, true: 使用测试地址, false:使用正式地址
    private static boolean isTestHost = false;
    // 是否初始化日志目录, true:初始化, false:不初始化
    private static boolean isInitLogDir = false;
    // 客户端类型，固定值
    private static String cType = "30";
    // 应用程序的名称
    private static String appName = "Youku SmartTV";

    private static final String APPKEY = "23817777";
    private static final String APPSECERETKEY = "7001eea712cb1eb79c29cd6d60657131";

    private static final String URL_ONLINE = "http://gw.api.taobao.com/router/rest";// 线上环境
    private static final String URL_PRE = "http://140.205.57.248/router/rest";// 预发环境
    private static final String URL_DAILY = "http://api.daily.taobao.net/router/rest";// 日常环境

    private static final String TOKEN_ONLINE = "https://oauth.taobao.com/token";// 线上环境
    private static final String TOKEN_PRE = "https://oauth.taobao.com/token";// 预发环境
    private static final String TOKEN_DAILY = "https://oauth.daily.taobao.net/token";// 日常环境

    @Override
    public void onCreate() {
        super.onCreate();
        TopApi.getInstance(this, URL_ONLINE, APPKEY, APPSECERETKEY);
        initPlayerSDK();
    }

    private void initPlayerSDK() {
        //初始化SDK基本参数
        PlayDataParams params = new PlayDataParams(this, Site.YOUKU, cType, appName);
        // 设置PID, 集成者向优酷申请的PID, 此PID必须申请
//        params.pid ="b689cf3401896d9a";
        PlayDataParams.pid = AppConstants.APP_KEY;
        ReleaseConfig.PLAYER_DECODE_TYPE_SYSTEM = true;
        //初始化SDK设置参数
        YoukuVideoPlayer.initialization(this, params, isDebug, isTestHost, isInitLogDir, isCibn);
    }
}
