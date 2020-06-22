package com.tksflysun.hi.ui.main.fragement.messagelist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.R;
import com.tksflysun.hi.adapter.MessageListRecyclerViewAdapter;
import com.tksflysun.hi.base.BaseFragment;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.LastMsg;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.FriendDao;
import com.tksflysun.hi.dao.LastMsgDao;
import com.tksflysun.hi.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;


public class MessageListFragment extends BaseFragment<MessageListPresenter> implements MessageListContract.View {
    private MessageListRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<LastMsg> msgs = new ArrayList<>();
    private Handler handler;

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list,
                container, false);
        recyclerView = view.findViewById(R.id.message_recycle_list);
        handler = new Handler();
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<LastMsg> list =
                new LastMsgDao().queryByUserId(HiApplication.getInstance().getUser().getUserId());
        refresh(list);
    }

    protected void initViews() {

        //2)添加布局管理器
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //3)配置Adapter

        mAdapter = new MessageListRecyclerViewAdapter(msgs,
                R.layout.item_message_list_view) {
            @Override
            public void bindView(ViewHolder holder, LastMsg obj) {
                holder.setText(R.id.message_list_nick_name, obj.getNickName());
                holder.setText(R.id.message_list_last_msg, obj.getLastMsg());
                holder.setText(R.id.message_list_item_time, obj.getTime());
                holder.setImageResource(R.id.message_list_head_icon,
                        obj.getHeadIcon());
            }
        };
        recyclerView.setAdapter(mAdapter);

        //4) 监听 点击,注意是监听 mAdapter ，而不是 mRecyclerView
        mAdapter.setOnItemClickListener(new MessageListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Friend> friends =
                        new FriendDao().queryByUserId(HiApplication.getInstance().getUser().getUserId());
                Intent intent = new Intent(getContext(), ChatActivity.class);
                for (Friend f : friends) {
                    if (f.getFriendId() == msgs.get(position).getFriendId()) {
                        intent.putExtra("chatFriend", JSON.toJSONString(f));
                        break;
                    }
                }
                getContext().startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    @Override
    public MessageListPresenter initPresenter() {
        return new MessageListPresenter();
    }

    @Override
    public void refresh(final List<LastMsg> lastMsgs) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                msgs.clear();
                msgs.addAll(lastMsgs);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
