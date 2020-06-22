package com.tksflysun.hi.ui.video;

import android.util.Log;
import org.webrtc.DataChannel;


public class DateChannelObserver implements DataChannel.Observer {

    private String TAG = "DateChannelObserver";

    @Override
    public void onBufferedAmountChange(long l) {
        Log.d(TAG, "onBufferedAmountChange : " + l);
    }

    @Override
    public void onStateChange() {

    }

    @Override
    public void onMessage(DataChannel.Buffer buffer) {
        Log.d(TAG, "onMessage DataChannel : " + buffer.toString());
    }
}
