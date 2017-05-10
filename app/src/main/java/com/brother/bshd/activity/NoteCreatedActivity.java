package com.brother.bshd.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.brother.bshd.R;
import com.brother.bshd.activity.base.BaseActivity;
import com.brother.bshd.utils.ConstantUtils;
import com.brother.bshd.utils.DialogUtils;
import com.brother.bshd.utils.SnackbarUtil;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/13.
 */
public class NoteCreatedActivity extends BaseActivity implements View.OnClickListener {
    /**
     * toolbar
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text)
    EditText mEditxt;
    @BindView(R.id.coordinate_note_edit)
    CoordinatorLayout mCoor;
    @BindView(R.id.fab_edit)
    FloatingActionButton mFab;


    /**
     * get note instance
     */
    private Note note;
    private String notebookName;
    /**
     * 创建笔记对象
     */
    private EvernoteNoteStoreClient mEvernsc;
    /**
     * 是否处于编辑状态
     */
    private boolean isEdit = false;
    private boolean isCreated = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_note_edit;
    }

    @Override
    protected void init() {
        notebookName = getIntent().getStringExtra(ConstantUtils.KEY_NAME);
        note = (Note) getIntent().getSerializableExtra(ConstantUtils.KEY);
        mEvernsc = getEnsc();
        //toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.right);
        mToolbar.setSubtitle(notebookName);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreated) {
                    //编辑
                    create();
                } else if (isEdit) {
                    edit();
                } else {
                    finish();
                }
            }
        });

        //Fab
        mFab.setOnClickListener(this);

        //set Title
        createDialog();
        mEditxt.setHint(R.string.editxt_hint);
        mEditxt.setEnabled(true);

    }

    private void createDialog() {
        final EditText e = new EditText(this);
        DialogUtils.showAlert(this, "Note title", e, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = e.getText().toString();
                note.setTitle(title);
                mToolbar.setTitle(title);
            }
        });
    }


    /**
     * 编辑换行
     */
    @NonNull
    private String newLine(String content) {
        String[] contentS = content.split("\\n+");
        StringBuilder sb = new StringBuilder();
        //遍历
        for (String str : contentS) {
            sb.append(ConstantUtils.DIV_SATRT + str + ConstantUtils.DIV_END);
        }

        return sb.toString();
    }

    /**
     * 编辑更新note
     */
    private void edit() {
        String noteContent = mEditxt.getText().toString();
        //evetnote markup lanuage
        String eml = EvernoteUtil.NOTE_PREFIX + newLine(noteContent) + EvernoteUtil.NOTE_SUFFIX;
        //设置
        note.setContent(eml);
        //update note
        mEvernsc.updateNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                //保存
//                result.setContent(noteContent);
                //设置图标
                isEdit = false;
                mEditxt.setEnabled(false);
                mToolbar.setNavigationIcon(R.drawable.back);
                SnackbarUtil.show(mCoor, R.string.snackbar_success);
            }

            @Override
            public void onException(Exception exception) {
                SnackbarUtil.show(mCoor, R.string.snackbar_exception);
            }
        });
    }


    /**
     * 编辑更新note
     */
    private void create() {
        String noteContent = mEditxt.getText().toString();
        //evetnote markup lanuage
        String eml = EvernoteUtil.NOTE_PREFIX + newLine(noteContent) + EvernoteUtil.NOTE_SUFFIX;
        //设置
        note.setContent(eml);
        //create note
        mEvernsc.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                //设置图标
                isCreated = false;
                mEditxt.setEnabled(false);
                mToolbar.setNavigationIcon(R.drawable.back);
                SnackbarUtil.show(mCoor, R.string.snackbar_created);

                //将创建成功的note，进行赋值
                note = result;
            }

            @Override
            public void onException(Exception exception) {
                SnackbarUtil.show(mCoor, R.string.snackbar_exception);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_edit) {
            isEdit = true;
            mEditxt.setEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.right);
            //title
            createDialog();
        }
    }
}
