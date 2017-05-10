package com.brother.bshd.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brother.bshd.R;
import com.brother.bshd.activity.base.BaseActivity;
import com.brother.bshd.fragment.LoginFragment;
import com.brother.bshd.fragment.NotebookFragment;
import com.brother.bshd.utils.SnackbarUtil;
import com.brother.bshd.utils.ToastUtil;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;

import org.w3c.dom.Text;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements EvernoteLoginFragment.ResultCallback, View.OnClickListener {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.coordinate)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.drawer)
    public DrawerLayout mDrawer;
    @BindView(R.id.navi)
    NavigationView mNavi;

    TextView mText;
    ImageView mImg;
    private View headerView;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        //设置toolbar
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle ac = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.app_name, R.string.app_name);
        mDrawer.addDrawerListener(ac);
        ac.syncState();



        //监听器
        headerView = mNavi.getHeaderView(0);
        mText = (TextView) headerView.findViewById(R.id.btn_navigation_header);
        mImg = (ImageView) headerView.findViewById(R.id.img_navigation_header);
        mText.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
           /*
         * 初始化fragment
         */
        ft = getSupportFragmentManager().beginTransaction();

        if (!isAuthention()) {
            ft.replace(R.id.content, new LoginFragment()).commit();
        } else {
            ft.replace(R.id.content, new NotebookFragment()).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 判断是否认证成功过
     *
     * @return
     */
    public boolean isAuthention() {
        if (EvernoteSession.getInstance().isLoggedIn()) {
            return true;
        }
        return false;
    }


    /**
     * 认证结果处理
     */
    @Override
    public void onLoginFinished(boolean successful) {
        if (successful) {
            //成功
            authenticSuccess();
        } else {
            //失败
            authenticFailure();
        }
    }

    /**
     * sanckbar 提醒，认证失败
     */
    private void authenticFailure() {
        /*
         * 弹出snack bar 提示
         */
        SnackbarUtil.show(mCoordinatorLayout, R.string.snackbar_msg_login, R.string.snackbar_action_login, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新认证
                EvernoteSession.getInstance().authenticate(MainActivity.this);
            }
        });

    }

    /**
     * 认证成功的处理
     */
    private void authenticSuccess() {
        //toast
        ToastUtil.show(R.string.toast_authentic_success);
        //成功,切换
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new NotebookFragment())
                .commit();
        mToolbar.setTitle("Notebooks");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_navigation_header:
                //判断
                if (isAuthention()) {
                    EvernoteSession.getInstance().logOut();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new LoginFragment()).commit();
                }
                break;
        }
    }
}
