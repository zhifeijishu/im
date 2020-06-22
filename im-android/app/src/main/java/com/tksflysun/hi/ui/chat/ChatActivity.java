package com.tksflysun.hi.ui.chat;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.R;
import com.tksflysun.hi.adapter.ChatAdapter;
import com.tksflysun.hi.base.BaseActivity;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.LastMsg;
import com.tksflysun.hi.common.DateUtil;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.ChatMsgDao;
import com.tksflysun.hi.dao.LastMsgDao;
import com.tksflysun.hi.tcp.IMsgListener;
import com.tksflysun.hi.tcp.handle.TcpMsgHandler;
import com.tksflysun.hi.tcp.protobuf.Im;
import com.tksflysun.hi.tcp.server.TcpServer;
import com.tksflysun.hi.ui.main.MainActivity;
import com.tksflysun.hi.ui.video.VideoChatActivity;
import com.tksflysun.hi.ui.video.VideoReceiveActivity;
import com.tksflysun.hi.ui.video.VideoSendActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends BaseActivity<ChatPresenter> implements ChatContract.View {


    RecyclerView mRecyclerView;

    private ChatAdapter mAdapter;
    private List<ChatMsg> msgs = new ArrayList<>();
    private Handler handler;
    private Button sendBtn;
    private Button videoChat;
    private EditText msgInput;
    private Friend chatFriend;
    private TextView friendNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRecyclerView = findViewById(R.id.chat_message_list);
        friendNameTextView = findViewById(R.id.chat_friend_name_text_view);
        sendBtn = findViewById(R.id.message_send_btn);
        videoChat = findViewById(R.id.vidio_chat_btn);
        msgInput = findViewById(R.id.message_input);
        chatFriend =
                JSON.parseObject(getIntent().getStringExtra("chatFriend"),
                        Friend.class);
        friendNameTextView.setText(chatFriend.getRemarkName());
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
        videoChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        VideoChatActivity.class);
                intent.putExtra("userId", chatFriend.getUserId());
                intent.putExtra("friendId", chatFriend.getFriendId());
                intent.putExtra("isCaller", true);
                startActivity(intent);
            }
        });
        initRecycle();
        TcpMsgHandler.addListener(mPresenter);
        handler = new Handler();
        List<ChatMsg> list =
                new ChatMsgDao().getMsgsByFriendId(chatFriend.getFriendId(),
                        chatFriend.getUserId());
        if (list != null && list.size() != 0) {
            refresh(list);
        }
    }

    private void sendMsg() {
        String msgId = UUID.randomUUID().toString().
                replace("-", "");
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setState(HiConstants.MSG_STATE.SENDING);
        chatMsg.setMsg(msgInput.getText().toString());
        chatMsg.setBelongType(HiConstants.MSG_BELONG.MY);
        chatMsg.setDate(System.currentTimeMillis() + "");
        chatMsg.setFromUserId(chatFriend.getUserId());
        chatMsg.setToUserId(chatFriend.getFriendId());
        chatMsg.setFriendId(chatFriend.getFriendId());
        chatMsg.setUserId(chatFriend.getUserId());
        chatMsg.setId(msgId);
        chatMsg.setMsgType(Im.MsgTypeEnum.TEXT_VALUE);
        new ChatMsgDao().insert(chatMsg);
        LastMsg lastMsg = new LastMsg();
        lastMsg.setFriendId(chatMsg.getFriendId());
        lastMsg.setLastMsg(chatMsg.getMsg());
        lastMsg.setNickName(chatFriend.getRemarkName());
        lastMsg.setTime(DateUtil.getTimeHHMM());
        lastMsg.setId(chatMsg.getFromUserId() + "_" + chatMsg.getFriendId());
        lastMsg.setUserId(chatMsg.getFromUserId());
        new LastMsgDao().insert(lastMsg);
        msgs.add(chatMsg);
        msgInput.setText("");
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        Im.SingleMsg singleMsg =
                Im.SingleMsg.newBuilder().setSendTimeStamp(System.currentTimeMillis()).setMsgId(msgId).setDeviceType(1).setFromUserId(HiApplication.getInstance().getUser().getUserId()).setToUserId(chatFriend.getFriendId()).setMsgType(Im.MsgTypeEnum.TEXT_VALUE).setContent(chatMsg.getMsg()).build();
        Im.TcpPackage tcpPackage =
                Im.TcpPackage.newBuilder().setContent(singleMsg.toByteString()).setPackageType(Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE).build();
        TcpServer.sendMsg(tcpPackage);
    }

    private void initRecycle() {
        //1)添加布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter(this, msgs);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ChatAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position, List<ChatMsg> items) {

            }
        });
    }


    @Override
    public void onDestroy() {
        TcpMsgHandler.removeListener(mPresenter);
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView = null;
        }
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter();
    }


    @Override
    public void refresh(final List<ChatMsg> chatMsgs) {
        new LastMsgDao().updateUnReadCount(chatFriend.getFriendId(),
                HiApplication.getInstance().getUser().getUserId(), 0);
        handler.post(new Runnable() {
            @Override
            public void run() {
                msgs.addAll(chatMsgs);
                msgInput.setText("");
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

    }

    @Override
    public Friend getChatFriend() {
        return this.chatFriend;
    }

    @Override
    public void jumpToVideoChat(long friendId) {
        Intent intent = new Intent(getApplicationContext(),
                VideoReceiveActivity.class);
        intent.putExtra("userId", chatFriend.getUserId());
        intent.putExtra("friendId", chatFriend.getFriendId());
         startActivity(intent);
    }
}
