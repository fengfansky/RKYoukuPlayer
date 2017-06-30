package com.rokid.movie.taobao.api.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lizhi.ywp on 2017-05-17.
 */

public class SupportApp {
    public static class LocalAppInfo {
        public String packageName = "";
        public int versionCode;

        public LocalAppInfo(String packageName, int versionCode) {
            this.packageName = packageName;
            this.versionCode = versionCode;
        }

        @Override
        public String toString() {
            return "packageName:" + packageName + ", versionCode:" + versionCode;
        }
    }

    //内置支持的应用
    final private List<String> mDefaultSupportApp = new ArrayList<>(
            Arrays.asList(
                    "com.yunos.tv.yingshi.boutique",
                    "com.yunos.tv.appstore",
                    "com.ali.tv.gamecenter",
                    "com.xiami.tv",
                    "com.yunos.tvtaobao"));

    final private List<String> mAppStoreList = new ArrayList<String>(Arrays.asList(
            "com.ali.tv.gamecenter",
            "com.yunos.tv.appstore"));

    //当前系统支持的应用，可能包括默认的应用，需要去重
    private List<String> mSupportApps;

    private List<String> getSupportAppInfo() {
        return mSupportApps;
    }

    public static List<LocalAppInfo> getSupportApp(Context context) {
        SupportApp supportApp = new SupportApp();

        supportApp.onSaveRequestData(null, null, false);

        return supportApp.getLocalSupportApp(context);
    }

    public List<LocalAppInfo> getLocalSupportApp(Context context) {
        List<String> serverSupportAppList = getSupportAppInfo();
        List<LocalAppInfo> localSupportAppList = new ArrayList<LocalAppInfo>();
        List<String> appStoreList = mAppStoreList;

        if (null != serverSupportAppList) {
            //读取支持的应用列表信息
            for (String packageName : serverSupportAppList) {
                PackageInfo packageInfo = CommUtils.getPackageInfo(context, packageName);
                if (null != packageInfo) {
                    LocalAppInfo localAppInfo = new LocalAppInfo(packageName, packageInfo.versionCode);
                    localSupportAppList.add(localAppInfo);
                    if (appStoreList.contains(packageName)) {
                        appStoreList.remove(packageName);
                    }
                }
            }
        }

        //默认的应用商店必须添加
        for (String appStorePackage : appStoreList) {
            PackageInfo packageInfo = CommUtils.getPackageInfo(context, appStorePackage);
            if (null != packageInfo) {
                LocalAppInfo localAppInfo = new LocalAppInfo(appStorePackage, packageInfo.versionCode);
                localSupportAppList.add(localAppInfo);
            }
        }
        return localSupportAppList;
    }

    public void onSaveRequestData(JSONObject jsonObject, Object data, boolean isSaved) {
        List<String> infos = (List<String>) data;

        Set<String> apps = new HashSet<String>();
        if (null == infos) {
            infos = new ArrayList<String>();
        } else {
            for (String packageName : infos) {
                if (!TextUtils.isEmpty(packageName)) {
                    apps.add(packageName);
                }
            }
        }
        for (String packageName : mDefaultSupportApp) {
            if (!TextUtils.isEmpty(packageName) && !apps.contains(packageName)) {
                infos.add(packageName);
            }
        }

        mSupportApps = infos;
    }
}
