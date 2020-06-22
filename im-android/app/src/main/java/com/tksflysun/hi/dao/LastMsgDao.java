package com.tksflysun.hi.dao;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.tksflysun.hi.bean.LastMsg;
import com.tksflysun.hi.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LastMsgDao {
    private Dao<LastMsg, Integer> dao;

    public LastMsgDao() {
        try {
            this.dao = DatabaseHelper.getInstance().getDao(LastMsg.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(LastMsg data) {
        try {
            List<LastMsg> list = queryById(data.getId());
            if (list == null || list.size() == 0) {
                Log.i("不存在历史聊天", "不存在历史聊天");
                dao.create(data);
            } else {
                Log.i("存在历史聊天", "存在历史聊天");
                LastMsg lastMsg = list.get(0);
                lastMsg.setLastMsg(data.getLastMsg());
                lastMsg.setTime(data.getTime());
                lastMsg.setNickName(data.getNickName());
                update(lastMsg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(LastMsg data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            Log.i("更新最后一条失败", e.getMessage());
        }
    }

    public void updateUnReadCount(long friendId, long userId, int count) {
        try {
            UpdateBuilder builder = dao.updateBuilder();
            builder.updateColumnValue("_unreadcount", count);
            builder.where().eq("_friendid", friendId).and().eq("_userid",
                    userId);
            builder.update();
        } catch (Throwable e) {
            Log.e("更新用户未读消息失败", e.getMessage());
        }
    }

    public List<LastMsg> queryByUserId(long userId) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("_userid", userId);
            return builder.query();
        } catch (Throwable e) {
            Log.e("获取用户最近消息失败", e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<LastMsg> queryById(String unionId) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("id", unionId);
            return builder.query();
        } catch (Throwable e) {
            Log.e("", e.getMessage());
        }
        return new ArrayList<>();
    }

    public void deleteAll(long userId) {
        try {
            Log.e("删除最近消息", "删除最近消息");

            DeleteBuilder builder = dao.deleteBuilder();
            builder.where().eq("_userid", userId);
            builder.delete();
        } catch (Throwable e) {
            Log.e("删除最近消息失败", e.getMessage());

        }
    }
}