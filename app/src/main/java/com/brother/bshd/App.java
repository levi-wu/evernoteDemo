package com.brother.bshd;

import android.app.Application;
import android.content.Context;

import com.brother.bshd.utils.ConstantUtils;
import com.evernote.client.android.EvernoteSession;

import java.util.Locale;

/**
 * Created by wule on 2017/04/07.
 */

public class App extends Application {
    public static Context mAPPContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //init
        initEvernote();
        mAPPContext = this;
    }

    /**
     * evernote初始化
     */
    private void initEvernote() {
        new EvernoteSession.Builder(this)
                .setEvernoteService(ConstantUtils.ENVIRONMENT)
                .setForceAuthenticationInThirdPartyApp(true)
                .setSupportAppLinkedNotebooks(true)
                .setLocale(Locale.SIMPLIFIED_CHINESE)
                .build(ConstantUtils.CONSUMER_KEY, ConstantUtils.CONSUMER_SECRET)
                .asSingleton();
    }
}
