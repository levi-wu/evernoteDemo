package com.brother.bshd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.brother.bshd.R;
import com.brother.bshd.activity.MainActivity;
import com.brother.bshd.activity.NoteEidtActivity;
import com.brother.bshd.activity.NotebookInfoActivity;
import com.brother.bshd.adapter.NoteAdapter;
import com.brother.bshd.callBack.RecyclerViewCallback;
import com.brother.bshd.fragment.base.BaseFragment;
import com.brother.bshd.model.NotebookModel;
import com.brother.bshd.utils.ConstantUtils;
import com.brother.bshd.utils.SnackbarUtil;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/11.
 */

public class NoteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewCallback {
    /**
     * 数据绑定
     */
    @BindView(R.id.coordinate_note)
    CoordinatorLayout mCoor;
    @BindView(R.id.recyclerView_note)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh_note)
    SwipeRefreshLayout mSwiperl;

    /**
     * 主activity
     */
    private NotebookInfoActivity mNoteActivity;
    /**
     * 从服务器获得的list列表
     */
    private List<Note> list = new ArrayList<>();
    /**
     * 创建笔记对象
     */
    private EvernoteNoteStoreClient mEvernsc;


    /**
     * key
     */
    public static final String KEY = "noteFragment";
    /**
     * 传递的数据
     */
    private String guid;

    /**
     * adapter
     */
    private NoteAdapter mNoteAdapter;

    /**
     * 创建fragment实例
     *
     * @param tag
     * @return
     */
    public static NoteFragment newInstance(String tag) {
        NoteFragment noteFragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, tag);
        noteFragment.setArguments(bundle);

        return noteFragment;
    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_note;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            guid = b.getString(KEY);
        }
        /*
         * 初始化Evernote 类
         */
        mEvernsc = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        //初始化activity对象
        mNoteActivity = (NotebookInfoActivity) getActivity();
        //设置recyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mNoteActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mNoteActivity, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void setListener() {
        mSwiperl.setOnRefreshListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //刷新开始
        mSwiperl.setRefreshing(true);
        //加载note
        findNote();


    }

    /**
     * 加载note
     */
    private void findNote() {
    /*
   * 初始化Evernote 类
   */
        NoteFilter noteFilter = new NoteFilter();
        //按照时间排序
        noteFilter.setOrder(NoteSortOrder.CREATED.getValue());
        noteFilter.setNotebookGuid(guid);
        //找note,获取note的信息，不包含content
        mEvernsc.findNotesAsync(noteFilter, 0, 20, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(NoteList result) {
                //获取note列表
                list = result.getNotes();
                mNoteAdapter = new NoteAdapter(mNoteActivity, list, NoteFragment.this);
                mRecyclerView.setAdapter(mNoteAdapter);
                //刷新结束
                mSwiperl.setRefreshing(false);


            }

            @Override
            public void onException(Exception exception) {
                SnackbarUtil.show(mCoor, R.string.snackbar_exception);
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwiperl.setRefreshing(true);

        /*
         * 做刷新处理
         */
        findNote();

        mSwiperl.setRefreshing(false);
    }

    /**
     * item 跳转
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        Note note = list.get(position);

        //页面跳转
//        mNoteActivity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content, NoteEditFragment.newInstance(note))
//                .addToBackStack(null)
//                .commit();

        Intent intent = new Intent(mNoteActivity, NoteEidtActivity.class);
        intent.putExtra(ConstantUtils.KEY, note);
        intent.putExtra(ConstantUtils.KEY_NAME, mNoteActivity.mToolbar.getTitle());
        startActivity(intent);

    }
}
