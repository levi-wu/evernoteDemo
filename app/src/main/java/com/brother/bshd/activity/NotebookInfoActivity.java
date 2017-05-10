package com.brother.bshd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.brother.bshd.R;
import com.brother.bshd.activity.base.BaseActivity;
import com.brother.bshd.callBack.RecyclerViewCallback;
import com.brother.bshd.fragment.EmptyFragment;
import com.brother.bshd.fragment.NoteFragment;
import com.brother.bshd.utils.ConstantUtils;
import com.brother.bshd.utils.SnackbarUtil;
import com.brother.bshd.utils.ToastUtil;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;

import java.util.List;

import butterknife.BindView;

public class NotebookInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.coordinate_info)
    CoordinatorLayout mCoor;
    @BindView(R.id.fab_info)
    FloatingActionButton mInfo;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    private EvernoteNoteStoreClient mEvernsc;
    //notebook guid
    private String guid;
    //notebook note num
    private boolean isNull;
    //notebook name
    private String name;

    /**
     * 初始化
     */
    @Override
    protected void init() {
        getWindow().setEnterTransition(new Slide(Gravity.LEFT));
        getWindow().setExitTransition(new Slide(Gravity.RIGHT));
        //获得启动intent
        Intent intent = getIntent();
        guid = intent.getStringExtra(ConstantUtils.KEY);
        isNull = intent.getBooleanExtra(ConstantUtils.KEY_NULL, false);
        name = intent.getStringExtra(ConstantUtils.KEY_NAME);

        //toolbar
        initToolbar();

        //fab
        mInfo.setOnClickListener(this);

        mEvernsc = getEnsc();
    }


    /**
     * toolbar 初始化
     */
    private void initToolbar() {
         /*
         * toolbar 初始化
         */
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notebook_info;
    }


    @Override
    protected void onResume() {
        super.onResume();
         /*
         * 判断是否有note，
         */
        if (isNull) {
            //空 fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new EmptyFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, NoteFragment.newInstance(guid)).commit();
        }
    }

    /**
     * fab click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_info:
                //创建 note

                createNote();
                break;
        }
    }


    /**
     * 创建 note
     */
    private void createNote() {
        final Note n = new Note();
        n.setNotebookGuid(guid);

        Intent i = new Intent(this, NoteCreatedActivity.class);
        i.putExtra(ConstantUtils.KEY, n);
        i.putExtra(ConstantUtils.KEY_NAME, name);
        startActivityForResult(i, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //切换 fragment
        isNull = false;
    }
}
