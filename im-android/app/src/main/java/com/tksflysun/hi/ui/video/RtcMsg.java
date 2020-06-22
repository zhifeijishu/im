package com.tksflysun.hi.ui.video;


import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class RtcMsg {
    private String type;
    private SessionDescription sessionDescription;
    private IceCandidate iceCandidate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SessionDescription getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(SessionDescription sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public IceCandidate getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }
}
