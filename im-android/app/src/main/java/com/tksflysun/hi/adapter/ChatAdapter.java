package com.tksflysun.hi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tksflysun.hi.R;
import com.tksflysun.hi.bean.ChatMsg;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatMsg> chatMsgs;
    private ClickListener clickListener;

    public ChatAdapter(Context context, List<ChatMsg> items) {
        this.context = context;
        this.chatMsgs = items;
    }

    public interface ClickListener {
        void onItemClick(View v, int position, List<ChatMsg> items);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMsgs.get(position).getBelongType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        Log.i("viewType", viewType + "");
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_msg_list_left, parent, false);
                viewHolder = new ViewHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_msg_list_right, parent, false);
                viewHolder = new ViewHolder(view);
                break;
        }
        Log.i("viewHolder", viewHolder.toString());
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ChatMsg chatBean=chatMsgs.get(position);
        ((TextView)holder.itemView.findViewById(R.id.chat_msg_text_view)).setText(chatBean.getMsg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                clickListener.onItemClick(holder.itemView, pos, chatMsgs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
}