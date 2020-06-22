package com.tksflysun.hi.dao;

import android.content.Context;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FriendDao {
    private Dao<Friend, Integer> dao;

    public FriendDao() {
        try {
            this.dao = DatabaseHelper.getInstance().getDao(Friend.class);
        } catch (Throwable e) {
            Log.i("获取好友dao失败", "获取好友dao失败");
        }
    }

    public void insertAll(List<Friend> data) {
        try {
            for (Friend f : data) {
                f.setHeadIconUrl("");
            }
            deleteByUserId();
            dao.create(data);
        } catch (Throwable e) {
            Log.i("插入好友列表失败", "插入好友列表失败" + e.getMessage());
        }
    }

    public void deleteByUserId() {
        try {
            DeleteBuilder builder = dao.deleteBuilder();
            builder.where().eq("_userid",
                    HiApplication.getInstance().getUser().getUserId());
            builder.delete();
        } catch (Throwable e) {
            Log.i("删除好友列表失败", "删除好友列表失败" + e.getMessage());
        }
    }

    public void update(Friend data) {
        try {
            dao.update(data);
        } catch (Throwable e) {
            Log.i("更新好友信息失败", "更新好友信息失败" + e.getMessage());
        }
    }

    public Friend queryById(int id) {
        Friend friend = null;
        try {
            friend = dao.queryForId(id);
        } catch (Throwable e) {
            Log.i("获取好友失败", "获取好友失败" + e.getMessage());
        }
        return friend;
    }

    public List<Friend> queryByUserId(Long userId) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("_userid", userId);
            return builder.query();
        } catch (Throwable e) {
            Log.i("获取好友列表失败", "获取好友列表失败" + e.getMessage());
        }
        return new ArrayList<>();
    }

    public Friend queryByUserIdAndFriendID(Long userId, long friendId) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("_userid", userId).and().eq("_friendid",
                    friendId);
            List<Friend> friends = builder.query();
            Log.i("好友", JSON.toJSONString(friends));
            if (friends.size() == 1) {
                return friends.get(0);
            }
        } catch (Throwable e) {
            Log.e("好友queryByUserIdFriendID", "失败" + e.getMessage());
        }
        return null;
    }
}