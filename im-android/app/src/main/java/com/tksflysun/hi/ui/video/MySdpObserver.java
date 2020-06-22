package com.tksflysun.hi.ui.video;

import android.util.Log;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;


public class MySdpObserver implements SdpObserver {

    private String TAG = "MySdpObserver";

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {

    }

    @Override
    public void onSetSuccess() {
        Log.e(TAG, "onSetSuccess ==  ");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.e(TAG, "onCreateFailure ==  " + s);
    }

    @Override
    public void onSetFailure(String s) {
        Log.e(TAG, "onSetFailure ==  " + s);
    }
}
