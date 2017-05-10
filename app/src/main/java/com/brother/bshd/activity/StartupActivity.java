package com.brother.bshd.activity;

import android.content.Intent;
import android.os.Handler;

import com.brother.bshd.R;
import com.brother.bshd.activity.base.BaseActivity;

/**
 * Created by wule on 2017/04/07.
 */

public class StartupActivity extends BaseActivity {

    @Override
    protected void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(StartupActivity.this, MainActivity.class));

            }
        }, 2000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_up;
    }

}
