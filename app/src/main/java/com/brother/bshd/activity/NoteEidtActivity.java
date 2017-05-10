package com.brother.bshd.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.brother.bshd.R;
import com.brother.bshd.activity.base.BaseActivity;
import com.brother.bshd.utils.ConstantUtils;
import com.brother.bshd.utils.DialogUtils;
import com.brother.bshd.utils.SnackbarUtil;
import com.brother.bshd.utils.ToastUtil;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteHtmlHelper;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;
import com.google.common.html.HtmlEscapers;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.BindView;

import static android.R.attr.data;

/**
 * Created by wule on 2017/04/13.
 */

public class NoteEidtActivity extends BaseActivity implements View.OnClickListener {
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
//    @BindView(R.id.webView)
//    WebView mWebView;


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
    private boolean isEdit;
    /**
     * evernote client
     */
    private EvernoteClientFactory mClient;

    /**
     * 布局填充时
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_note_edit;
    }

    /**
     * 初始化
     */
    @Override
    protected void init() {
        mClient = EvernoteSession.getInstance().getEvernoteClientFactory();
        notebookName = getIntent().getStringExtra(ConstantUtils.KEY_NAME);
        note = (Note) getIntent().getSerializableExtra(ConstantUtils.KEY);
        mEvernsc = getEnsc();
        //toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(note.getTitle());
        mToolbar.setSubtitle(notebookName);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    //编辑
                    edit();
                } else {

                    finish();
                }
            }
        });

        //Fab
        mFab.setOnClickListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        //luoji
        processLogic();
    }

    /**
     * 逻辑处理
     *
     * @param
     */
    protected void processLogic() {
        //异步获取note的内容
        mEvernsc.getNoteContentAsync(note.getGuid(), new EvernoteCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //获取数据
                if (TextUtils.isEmpty(result)) {
                    mEditxt.setHint(R.string.editxt_hint);
                } else {
                    //html显示图片
                    mEditxt.setText(Html.fromHtml(result));
                }
            }

            @Override
            public void onException(Exception exception) {
                SnackbarUtil.show(mCoor, R.string.snackbar_exception);
            }
        });

//        final EvernoteHtmlHelper mEvernoteHtmlHelper = mClient.getHtmlHelperDefault();
//        try {
//            mEvernoteHtmlHelper.downloadNoteAsync(note.getGuid(), new EvernoteCallback<Response>() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void onSuccess(Response result) {
//
//                    try {
//                        String noteContent = mEvernoteHtmlHelper.parseBody(result);
//                        mEditxt.setText(noteContent);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onException(Exception exception) {
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }
    protected WebResourceResponse toWebResource(Response response) throws IOException {
        if (response == null || !response.isSuccessful()) {
            return null;
        }

        String mimeType = response.header("Content-Type");
        String charset = response.header("charset");
        return new WebResourceResponse(mimeType, charset, response.body().byteStream());
    }
    /**
     * click 事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_edit) {
            isEdit = true;
            mEditxt.setEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.right);
        }
    }
}