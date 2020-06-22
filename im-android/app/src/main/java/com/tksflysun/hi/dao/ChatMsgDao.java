package com.tksflysun.hi.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ChatMsgDao {
    private Dao<ChatMsg, Integer> dao;

    public ChatMsgDao() {
        try {
            this.dao = DatabaseHelper.getInstance().getDao(ChatMsg.class);
        } catch (Throwable e) {
            Log.i("获取chatMsgDao失败","获取chatMsgDao失败");
        }
    }

    public void insert(ChatMsg data) {
        try {
            Log.i("friendid userId",
                    "" + data.getFriendId() + "  " + data.getUserId());
            dao.create(data);
        } catch (Throwable e) {
            Log.i("插入chatmsg失败","插入chatmsg失败");
        }
    }




    public ChatMsg queryById(String id) {
        ChatMsg chatMsg = null;
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("id", id);
            List<ChatMsg> chatMsgs = builder.query();
            if (chatMsgs.size() == 1) {
                chatMsg = chatMsgs.get(0);
            }
        } catch (Throwable e) {
            Log.e("查询聊天记录", e.getMessage());
        }
        return chatMsg;
    }

    public List<ChatMsg> getMsgsByFriendId(long friendId, long userId) {
        try {
            Log.i("friendid userId", "" + friendId + "  " + userId);

            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("_friendid", friendId).and().eq("_userid",
                    userId);
            List<ChatMsg> chatMsgs = builder.query();
            Log.e("查询聊天记录", "" + chatMsgs.size());
            return chatMsgs;
        } catch (Throwable e) {
            Log.e("查询聊天记录", e.getMessage());
        }
        return new ArrayList<>();
    }

    public void deleteAll(long userId) {
        try {
            Log.e("删除聊天消息", "删除聊天消息");

            DeleteBuilder builder = dao.deleteBuilder();
            builder.where().eq("_userid", userId);
            builder.delete();
        } catch (Throwable e) {
            Log.e("删除聊天消息失败", e.getMessage());

        }
    }
}