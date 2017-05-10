package com.brother.bshd.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;

import com.brother.bshd.R;
import com.brother.bshd.activity.MainActivity;
import com.brother.bshd.activity.NotebookInfoActivity;
import com.brother.bshd.adapter.NotebookAdapter;
import com.brother.bshd.callBack.RecyclerViewCallback;
import com.brother.bshd.fragment.base.BaseFragment;
import com.brother.bshd.model.NotebookModel;
import com.brother.bshd.utils.ConstantUtils;
import com.brother.bshd.utils.DialogUtils;
import com.brother.bshd.utils.SnackbarUtil;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteCollectionCounts;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.Notebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/07.
 */

public class NotebookFragment extends BaseFragment implements RecyclerViewCallback, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    /**
     * 控件绑定
     */
    @BindView(R.id.coordinate_notebook)
    CoordinatorLayout mCoordinator;
    @BindView(R.id.recyclerView_notebook)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_notebook)
    FloatingActionButton mFAB;
    @BindView(R.id.swipeRefresh_notebook)
    SwipeRefreshLayout mSwiperl;
    /**
     * 主activity
     */
    private MainActivity mMain;
    /**
     * 从服务器获得的list列表
     */
    private List<NotebookModel> list = new ArrayList<>();
    /**
     * 创建笔记对象
     */
    private EvernoteNoteStoreClient mEvernsc;
    //adapter
    NotebookAdapter mAdapter;
    //asynctask
    AsyncNoteBook mAsync = new AsyncNoteBook();
    //dialog的EditText
    private EditText editText;

    /**
     * 初始化fragment
     *
     * @return
     */
    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_notebook;
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        /*
         * 初始化Evernote 类
         */
        mEvernsc = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        //初始化activity对象
        mMain = (MainActivity) getActivity();
        //设置recyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMain, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mMain, DividerItemDecoration.VERTICAL));

    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        mSwiperl.setOnRefreshListener(this);

        //fab
        mFAB.setOnClickListener(this);
    }

    /**
     * 逻辑处理
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mAsync.execute();
    }

    /**
     * 点击事件,页面跳转
     *
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(int position) {
        //notebook的guid
        String guid = list.get(position).getNotebookGuid();
        //list 的 note 数据
        String num = list.get(position).getNoteNum();
        boolean isNull = false;
        if (num.equals("0 Note")) {
            isNull = true;
        }

        //动画跳转
        mMain.getWindow().setExitTransition(new Fade());
        mMain.getWindow().setEnterTransition(new Explode());
        //页面跳转
        Intent intent = new Intent(mMain, NotebookInfoActivity.class);
        intent.putExtra(ConstantUtils.KEY, guid);
        intent.putExtra(ConstantUtils.KEY_NULL, isNull);
        intent.putExtra(ConstantUtils.KEY_NAME, list.get(position).getNotebookName());
        startActivity(intent);
    }

    /**
     * 刷新事件
     */
    @Override
    public void onRefresh() {
        //打开
        mSwiperl.setRefreshing(true);
        //asynctask 只能同时运行一次，所以要先取消然后在执行
        list.clear();
        new AsyncNoteBook().execute();
        //关闭
        mSwiperl.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_notebook:
                //弹出框
                showDialog();
                break;
        }
    }

    /**
     * 弹出框
     */
    private void showDialog() {
        editText = new EditText(mMain);
        DialogUtils.showAlert(mMain, "New notebook", editText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //创建 note book
                final Notebook notebook = new Notebook();
                notebook.setName(editText.getText().toString());
                mEvernsc.createNotebookAsync(notebook, new EvernoteCallback<Notebook>() {
                    @Override
                    public void onSuccess(Notebook result) {
                        //更新 recyclerView

                        //打开
                        mSwiperl.setRefreshing(true);
                        //asynctask 只能同时运行一次，所以要先取消然后在执行
                        list.clear();
                        new AsyncNoteBook().execute();
                        //关闭
                        mSwiperl.setRefreshing(false);
                    }

                    @Override
                    public void onException(Exception exception) {
                        SnackbarUtil.show(mCoordinator, R.string.snackbar_exception);
                    }
                });
            }
        });
    }

/**
 *
 *----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:Wu lei
 */

    /**
     * 内部类，异步任务
     */
    class AsyncNoteBook extends AsyncTask<Void, Void, List<NotebookModel>> {
        private String name;
        private int num;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //初始化
            mSwiperl.setRefreshing(true);
        }

        @Override
        protected List<NotebookModel> doInBackground(Void... params) {
            //异步操作
            try {
                List<Notebook> noteB = mEvernsc.listNotebooks();
                for (Notebook n : noteB) {
                    name = n.getName();
                    NoteFilter nf = new NoteFilter();
                    nf.setNotebookGuid(n.getGuid());
                    NoteCollectionCounts ncc = mEvernsc.findNoteCounts(nf, false);
                    //填充的string
                    String noteC = null;
                    /*
                     * map不能为空问题
                     */
                    Map<String, Integer> map = ncc.getNotebookCounts();
                    if (map != null && map.size() != 0) {
                        num = map.get(n.getGuid());
                    } else {
                        num = 0;
                    }
                    //拼组成字符串
                    if (num > 0) {
                        noteC = num + " Notes";
                    } else {
                        noteC = num + " Note";
                    }
                    //添加到数组中
                    list.add(new NotebookModel(n.getGuid(), name, noteC));

                }

            } catch (Exception e) {
                SnackbarUtil.show(mCoordinator, R.string.snackbar_exception);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<NotebookModel> list) {
            super.onPostExecute(list);
            //做完后，进行设置适配器
            mAdapter = new NotebookAdapter(mMain, list, NotebookFragment.this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);

            //关闭
            mSwiperl.setRefreshing(false);
        }
    }
}
