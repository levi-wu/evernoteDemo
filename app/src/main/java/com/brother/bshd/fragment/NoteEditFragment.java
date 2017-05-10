package com.brother.bshd.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.brother.bshd.R;
import com.brother.bshd.activity.NotebookInfoActivity;
import com.brother.bshd.fragment.base.BaseFragment;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/13.
 */

public class NoteEditFragment extends BaseFragment {
    @BindView(R.id.edit_text)
    EditText mEditxt;
    @BindView(R.id.coordinate_note_edit)
    CoordinatorLayout mCoor;

    /**
     * get note instance
     */
    private Note note;
    /**
     * 创建笔记对象
     */
    private EvernoteNoteStoreClient mEvernsc;

    /**
     * activity
     */
    private NotebookInfoActivity mNbActity;
    /**
     * toolbar
     */
    private Toolbar mToolbar;
    /**
     * key
     */
    public static final String KEY = "noteEditFragment";
    public static final String KEY_G = "guid";
    private String notebookGuid;

    /**
     * 创建fragment实例
     *
     * @return
     */
    public static NoteEditFragment newInstance( String guid) {
        NoteEditFragment noteFragment = new NoteEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_G, guid);
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    @NonNull
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            notebookGuid = b.getString(KEY_G);
        }

        //activity
        mNbActity = (NotebookInfoActivity) getActivity();

        //toolbar
        this.mToolbar = mNbActity.mToolbar;
        mToolbar.setSubtitle(mToolbar.getTitle());

        mToolbar.setTitle(note.getTitle());


        //实例化
        mEvernsc = mNbActity.getEnsc();


    }

    @Override
    protected void setListener() {

    }

    /**
     * 逻辑处理
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //异步获取note的内容
        mEvernsc.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {

            }

            @Override
            public void onException(Exception exception) {

            }
        });


    }
}
