package com.tksflysun.hi.ui.main.fragement.friendlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.R;
import com.tksflysun.hi.adapter.FriendListRecyclerViewAdapter;
import com.tksflysun.hi.base.BaseFragment;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.FriendDao;
import com.tksflysun.hi.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends BaseFragment<FriendListPresenter> implements FriendListContract.View {
    private FriendListRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<Friend> friends = new ArrayList<>();
    private Handler handler;

    public FriendListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fragement", "create");
        View view = inflater.inflate(R.layout.fragment_friend_list, container
                , false);
        recyclerView = view.findViewById(R.id.friend_list);
        initViews();
        handler = new Handler();
        List<Friend> friends =
                new FriendDao().queryByUserId(HiApplication.getInstance().getUser().getUserId());
        refreshFriendListView(friends);
        mPresenter.getFriends();
        return view;
    }


    protected void initViews() {

        //2)添加布局管理器
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //3)配置Adapter

        mAdapter = new FriendListRecyclerViewAdapter(friends,
                R.layout.item_friend_list_view) {
            @Override
            public void bindView(ViewHolder holder, Friend obj) {
                holder.setText(R.id.item_address_book_nick_name,
                        obj.getRemarkName());
                holder.setImageResource(R.id.friend_list_head_icon,
                        obj.getHeadIcon());
            }
        };
        recyclerView.setAdapter(mAdapter);

        //4) 监听 点击,注意是监听 mAdapter ，而不是 mRecyclerView
        mAdapter.setOnItemClickListener(new FriendListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("chatFriend", JSON.toJSONString(friends.get(position)));
                getContext().startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    @Override
    public FriendListPresenter initPresenter() {
        return new FriendListPresenter();
    }

    @Override
    public void refreshFriendListView(final List<Friend> lists) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                friends.clear();
                friends.addAll(lists);
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
