package com.tksflysun.hi.common;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.R;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.ChatMsgDao;
import com.tksflysun.hi.dao.LastMsgDao;
import com.tksflysun.hi.db.DatabaseHelper;
import com.tksflysun.hi.ui.main.MainActivity;

/**
 * @author lpf
 * 全局共享
 */
public class HiApplication extends Application {
    private static HiApplication instance;

    public static HiApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.initOrmLite(this);
        instance = this;
        registerActivityLifecycleCallbacks(new HiLifeCycle());
    }

    public void cache(String key, String value) {
        String valueEncrypt = AESUtil.encrypt(value);
        SharedPreferences sharedPreferences = getSharedPreferences("data",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, valueEncrypt);
        editor.commit();
    }

    public String getCache(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("data",
                Context.MODE_PRIVATE);
        String valueEncrypt = sharedPreferences.getString(key, "");
        return AESUtil.decrypt(valueEncrypt);
    }

    public void cleanCache() {
        //删除聊天记录
        new LastMsgDao().deleteAll(getUser().getUserId());
        new ChatMsgDao().deleteAll(getUser().getUserId());

    }

    public User getUser() {
        return JSON.parseObject(getCache(HiConstants.USREINFO), User.class);
    }

    public void notificationNewMsg() {
        if (HiLifeCycle.isApplicationInForeground()) {
            Log.i("应用在前台", "应用在前台");
            return;
        }
        if (!NotificationManagerCompat.from(getBaseContext()).areNotificationsEnabled()) {
            Log.i("通知权限没打开", "通知权限没打开");
            return;
        }
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        resultIntent.putExtra("initStatus", 0);
        PendingIntent resultPI = PendingIntent.getActivity(getBaseContext(),
                1, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder =
                new Notification.Builder(getBaseContext()).setContentTitle(
                        "新消息").setContentText("您有新的消息。").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001",
                    "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }
        builder.setContentIntent(resultPI);
        Notification n = builder.build();
        manager.notify(NOTIFICATION_ID, n);
    }

    private static final int NOTIFICATION_ID = 1001;

}
