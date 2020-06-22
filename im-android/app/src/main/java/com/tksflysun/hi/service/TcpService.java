package com.tksflysun.hi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tksflysun.hi.tcp.protobuf.Im;
import com.tksflysun.hi.tcp.server.TcpServer;

public class TcpService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        for(int i=0;i<10;i++){
            TcpServer.start();
        }
    }

    public void startServer() {

    }
}
