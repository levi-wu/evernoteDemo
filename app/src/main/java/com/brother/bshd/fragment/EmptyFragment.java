package com.brother.bshd.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.brother.bshd.R;
import com.brother.bshd.fragment.base.BaseFragment;

/**
 * Created by wule on 2017/04/07.
 */

public class EmptyFragment extends BaseFragment {
    /**
     * key
     */
    public static final String KEY = "emptyFragment";
    /**
     * 传递的数据
     */
    private String argument;

    /**
     * 创建fragment实例
     *
     * @param tag
     * @return
     */
    public static EmptyFragment newInstance(String tag) {
        EmptyFragment emptyFragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, tag);
        emptyFragment.setArguments(bundle);

        return emptyFragment;
    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            argument = b.getString(KEY);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
