package com.brother.bshd.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;

import com.brother.bshd.App;
import com.brother.bshd.R;
import com.brother.bshd.activity.MainActivity;
import com.brother.bshd.fragment.base.BaseFragment;
import com.brother.bshd.utils.ToastUtil;
import com.evernote.client.android.EvernoteSession;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/07.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.btn_login)
    Button mLogin;
    /**
     * 主activity
     */
    private MainActivity mMain;

    /**
     * 内容设置
     *
     * @return
     */
    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_login;
    }

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mMain = (MainActivity) getActivity();
        mMain.mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMain.mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * 监听器
     */
    @Override
    protected void setListener() {

    }

    /**
     * 逻辑代码
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        /*
         * 逻辑处理
         */
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络判断
                if (mMain.isNetworkAvaliable(App.mAPPContext)) {
                    //activity认证
                    EvernoteSession.getInstance().authenticate(mMain);
                } else {
                    //无网络
                    ToastUtil.show(R.string.network_failure);
                }
            }
        });
    }
}
