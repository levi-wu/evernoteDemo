package com.brother.bshd.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.brother.bshd.activity.MainActivity;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.asyncclient.EvernoteUserStoreClient;
import com.evernote.client.android.login.EvernoteLoginFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/1/13.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ArrayList<AppCompatActivity> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //设置layoutView
        setContentView(getLayoutId());
        //初始化ButterKnife
        ButterKnife.bind(this);
        /*
         * 初始化，逻辑操作
         */
        init();
        //所有的活动
        list.add(this);

    }

    /**
     * 销毁所有的activity
     */
    public void finishAll() {
        for (AppCompatActivity a : list) {
            a.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.remove(this);
    }

    /**
     * 初始化操作
     */
    protected abstract void init();

    /**
     * 返回activity的布局
     *
     * @return 返回的布局id
     */
    public abstract int getLayoutId();

    /**
     * 跳转
     */
    public void toActivity(Activity src, Class dest) {
        src.startActivity(new Intent(src, dest));
    }

    /**
     * 判断网络是否可用
     */
    public boolean isNetworkAvaliable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //判断是否可用
        return netInfo != null && netInfo.isAvailable();
    }

    /**
     * 获取noteStore Client
     *
     * @return
     */
    public EvernoteNoteStoreClient getEnsc() {
        if (EvernoteSession.getInstance().isLoggedIn()) {

            return EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        }
        return null;
    }

    /**
     * 获取userStore Client
     *
     * @return
     */
    public EvernoteUserStoreClient getEusc() {
        if (EvernoteSession.getInstance().isLoggedIn()) {

            return EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient();
        }
        return null;
    }

    /**
     * 是否登录
     */

    public boolean isLogin() {
        if (EvernoteSession.getInstance().isLoggedIn()) {
            return true;
        } else {
            return false;
        }

    }

}
