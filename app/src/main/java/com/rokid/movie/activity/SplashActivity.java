package com.rokid.movie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rokid.movie.bean.NLPBean;

import rokid.os.RKTTS;
import rokid.os.RKTTSCallback;

/**
 * Created by fanfeng on 2017/6/6.
 */

public class SplashActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    private static final String KEY_NLP = "nlp";

    private static final String SKILL_WELCOME = "ROKID.INTENT.WELCOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Splash onCreate");
        processIntent();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void processIntent() {
        if (isStartMainActivity()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    Log.d(TAG, " startMainActivity");
                }
            }, 100);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        SplashActivity.this.finish();
    }

    private boolean isStartMainActivity() {
        if (isIntentValidate(getIntent())) {
            Log.d(TAG, "result startParseCommand intent invalidate!");
            return false;
        }
        String nlp = getIntent().getStringExtra(KEY_NLP);
        Log.d(TAG, "result  Nlp ---> " + nlp);

        if (TextUtils.isEmpty(nlp)) {
            Log.d(TAG, "result NLP is empty!!!");
            welcomeTTS();
            return false;
        }
        NLPBean nlpBean = new Gson().fromJson(nlp, NLPBean.class);
        String intentEnvent = nlpBean.getIntent();
        if (SKILL_WELCOME.equals(intentEnvent)) {
            welcomeTTS();
            return false;
        }
        return true;
    }

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
    }
}
