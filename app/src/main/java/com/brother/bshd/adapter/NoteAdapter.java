package com.brother.bshd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brother.bshd.R;
import com.brother.bshd.callBack.RecyclerViewCallback;
import com.brother.bshd.utils.DateUtils;
import com.evernote.edam.type.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wule on 2017/04/11.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 实例
     */
    private List<Note> list;
    /**
     * 接口回调
     */
    private RecyclerViewCallback rvc;

    public NoteAdapter(Context context, List<Note> list, RecyclerViewCallback rvc) {
        this.context = context;
        this.list = list;
        this.rvc = rvc;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //解析
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_notebook, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //数据绑定,点击事件
        holder.noteName.setText(list.get(position).getTitle());
        String date = DateUtils.convertToStr(list.get(position).getUpdated());
        holder.noteDate.setTextColor(context.getResources().getColor(R.color.accent));
        holder.noteDate.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                rvc.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 内部类，viewholder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_note_name)
        TextView noteName;
        @BindView(R.id.txt_note_num)
        TextView noteDate;

        public ViewHolder(View itemView) {
            super(itemView);
            //数据绑定
            ButterKnife.bind(this, itemView);
        }
    }


}
