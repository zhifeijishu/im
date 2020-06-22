package com.tksflysun.hi.ui.video;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.tksflysun.hi.R;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.PushCmdMsg;
import com.tksflysun.hi.bean.res.PushCmdConstants;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.tcp.IMsgListener;
import com.tksflysun.hi.tcp.handle.TcpMsgHandler;
import com.tksflysun.hi.tcp.protobuf.Im;
import com.tksflysun.hi.tcp.server.TcpServer;
import org.webrtc.*;
import org.webrtc.PeerConnectionFactory.InitializationOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 视频聊天
 */
public class VideoChatActivity extends AppCompatActivity implements IMsgListener {
    private static final String TAG = "VideoChatActivity";

    private SurfaceViewRenderer localSurfaceView;
    private SurfaceViewRenderer remoteSurfaceView;
    private EglBase eglBase;
    private PeerConnectionFactory peerConnectionFactory;

    private VideoTrack videoTrack;
    private AudioTrack audioTrack;
    private PeerConnection peerConnection;
    private List<String> streamList;
    private List<PeerConnection.IceServer> iceServers;
    private DataChannel channel;
    private MySdpObserver observer;
    private long userId;
    private long friendId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videochat);
        localSurfaceView = findViewById(R.id.video_local_view);
        remoteSurfaceView = findViewById(R.id.video_remote_view);
        TcpMsgHandler.addListener(this);
        createPeerConnection();
        boolean isCaller = getIntent().getBooleanExtra("isCaller", false);
        userId = getIntent().getLongExtra("userId", 0);
        friendId = getIntent().getLongExtra("friendId", 0);
        Log.i("isCaller", isCaller + "");
        if (!isCaller) {
            createOffer();
        } else {
            PushCmdMsg pushCmdMsg = new PushCmdMsg();
            pushCmdMsg.setCmdType(PushCmdConstants.VIDEO_CALL);
            pushCmdMsg.setMsg("");
            sendCmdMsg(pushCmdMsg);
        }
    }


    /**
     * 连接webrtc
     */
    private void createPeerConnection() {
        //Initialising PeerConnectionFactory
        InitializationOptions initializationOptions =
                InitializationOptions.builder(this).setEnableInternalTracer(true).setFieldTrials("WebRTC-H264HighProfile/Enabled/").createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);
        //创建EglBase对象
        eglBase = EglBase.create();
        PeerConnectionFactory.Options options =
                new PeerConnectionFactory.Options();
        options.disableEncryption = true;
        options.disableNetworkMonitor = true;
        peerConnectionFactory =
                PeerConnectionFactory.builder().setVideoDecoderFactory(new DefaultVideoDecoderFactory(eglBase.getEglBaseContext())).setVideoEncoderFactory(new DefaultVideoEncoderFactory(eglBase.getEglBaseContext(), true, true)).setOptions(options).createPeerConnectionFactory();
        // 配置STUN穿透服务器  转发服务器
        iceServers = new ArrayList<>();
        PeerConnection.IceServer iceServer =
                PeerConnection.IceServer.builder(Constant.STUN).createIceServer();
        iceServers.add(new PeerConnection.IceServer("turn:*.*.*.72:3478"
                , "*", "*"));
        iceServers.add(iceServer);

        streamList = new ArrayList<>();

        PeerConnection.RTCConfiguration configuration =
                new PeerConnection.RTCConfiguration(iceServers);

        PeerConnectionObserver connectionObserver = getObserver();
        peerConnection =
                peerConnectionFactory.createPeerConnection(configuration,
                        connectionObserver);


        /*
        DataChannel.Init 可配参数说明：
        ordered：是否保证顺序传输；
        maxRetransmitTimeMs：重传允许的最长时间；
        maxRetransmits：重传允许的最大次数；
        */
        DataChannel.Init init = new DataChannel.Init();
        if (peerConnection != null) {
            channel = peerConnection.createDataChannel(Constant.CHANNEL, init);
        }
        DateChannelObserver channelObserver = new DateChannelObserver();
        connectionObserver.setObserver(channelObserver);
        initView();
        initObserver();

    }

    private void initObserver() {
        observer = new MySdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                //将会话描述设置在本地
                peerConnection.setLocalDescription(this, sessionDescription);
                SessionDescription localDescription =
                        peerConnection.getLocalDescription();
                SessionDescription.Type type = localDescription.type;
                Log.e(TAG, "onCreateSuccess == " + " type == " + type);
                //接下来使用之前的WebSocket实例将offer发送给服务器
                if (type == SessionDescription.Type.OFFER) {
                    //呼叫
                    offer(sessionDescription);
                } else if (type == SessionDescription.Type.ANSWER) {
                    //应答
                    answer(sessionDescription);
                } else if (type == SessionDescription.Type.PRANSWER) {
                    //再次应答

                }
            }
        };

    }

    private PeerConnectionObserver getObserver() {
        return new PeerConnectionObserver() {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                setIceCandidate(iceCandidate);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                Log.d(TAG, "onAddStream : " + mediaStream.toString());
                List<VideoTrack> videoTracks = mediaStream.videoTracks;
                if (videoTracks != null && videoTracks.size() > 0) {
                    VideoTrack videoTrack = videoTracks.get(0);
                    if (videoTrack != null) {
                        videoTrack.addSink(remoteSurfaceView);
                    }
                }
                List<AudioTrack> audioTracks = mediaStream.audioTracks;
                if (audioTracks != null && audioTracks.size() > 0) {
                    AudioTrack audioTrack = audioTracks.get(0);
                    if (audioTrack != null) {
                        audioTrack.setVolume(Constant.VOLUME);
                    }
                }
            }
        };
    }


    /**
     * 初始化view
     */
    private void initView() {
        initSurfaceview(localSurfaceView);
        initSurfaceview(remoteSurfaceView);
        startLocalVideoCapture(localSurfaceView);
        startLocalAudioCapture();
    }

    /**
     * 创建本地视频
     *
     * @param localSurfaceView
     */
    private void startLocalVideoCapture(SurfaceViewRenderer localSurfaceView) {
        VideoSource videoSource = peerConnectionFactory.createVideoSource(true);
        SurfaceTextureHelper surfaceTextureHelper =
                SurfaceTextureHelper.create(Thread.currentThread().getName(),
                        eglBase.getEglBaseContext());
        VideoCapturer videoCapturer = createVideoCapturer();
        videoCapturer.initialize(surfaceTextureHelper, this,
                videoSource.getCapturerObserver());
        videoCapturer.startCapture(Constant.VIDEO_RESOLUTION_WIDTH,
                Constant.VIDEO_RESOLUTION_HEIGHT, Constant.VIDEO_FPS); //
        // width, height, frame per second
        videoTrack =
                peerConnectionFactory.createVideoTrack(Constant.VIDEO_TRACK_ID, videoSource);
        videoTrack.addSink(localSurfaceView);
        MediaStream localMediaStream =
                peerConnectionFactory.createLocalMediaStream(Constant.LOCAL_VIDEO_STREAM);
        localMediaStream.addTrack(videoTrack);
        peerConnection.addTrack(videoTrack, streamList);
        peerConnection.addStream(localMediaStream);
    }

    /**
     * 创建本地音频
     */
    private void startLocalAudioCapture() {
        //语音
        MediaConstraints audioConstraints = new MediaConstraints();
        //回声消除
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "googEchoCancellation", "true"));
        //自动增益
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "googAutoGainControl", "true"));
        //高音过滤
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "googHighpassFilter", "true"));
        //噪音处理
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "googNoiseSuppression", "true"));
        AudioSource audioSource =
                peerConnectionFactory.createAudioSource(audioConstraints);
        audioTrack =
                peerConnectionFactory.createAudioTrack(Constant.AUDIO_TRACK_ID, audioSource);
        MediaStream localMediaStream =
                peerConnectionFactory.createLocalMediaStream(Constant.LOCAL_AUDIO_STREAM);
        localMediaStream.addTrack(audioTrack);
        audioTrack.setVolume(Constant.VOLUME);
        peerConnection.addTrack(audioTrack, streamList);
        peerConnection.addStream(localMediaStream);
    }

    /**
     * 初始化iew
     *
     * @param localSurfaceView
     */
    private void initSurfaceview(SurfaceViewRenderer localSurfaceView) {
        localSurfaceView.init(eglBase.getEglBaseContext(), null);
        localSurfaceView.setMirror(true);
        localSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localSurfaceView.setKeepScreenOn(true);
        localSurfaceView.setZOrderMediaOverlay(true);
        localSurfaceView.setEnableHardwareScaler(false);
    }


    /**
     * 拨打电话
     */
    private void createOffer() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"));
        peerConnection.createOffer(observer, mediaConstraints);
    }

    private void createAnswer() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"));
        peerConnection.createAnswer(observer, mediaConstraints);
    }

    /**
     * 应答
     *
     * @param sdpDescription
     */
    private void answer(SessionDescription sdpDescription) {
        RtcMsg rtcMsg = new RtcMsg();
        rtcMsg.setType(Constant.OFFER);
        rtcMsg.setSessionDescription(sdpDescription);
        String text = new Gson().toJson(rtcMsg);
        Log.e(TAG, " answer " + text);
        sendRtcMsg(rtcMsg);
    }

    /**
     * 呼叫
     *
     * @param sdpDescription
     */
    private void offer(SessionDescription sdpDescription) {
        RtcMsg rtcMsg = new RtcMsg();
        rtcMsg.setType(Constant.OFFER);
        rtcMsg.setSessionDescription(sdpDescription);
        String text = new Gson().toJson(rtcMsg);
        Log.e(TAG, " offer " + text);
        sendRtcMsg(rtcMsg);
    }

    /**
     * 呼叫
     *
     * @param iceCandidate
     */
    private void setIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG, "onIceCandidate : " + iceCandidate.sdp);
        Log.d(TAG,
                "onIceCandidate : sdpMid = " + iceCandidate.sdpMid + " " +
                        "sdpMLineIndex = " + iceCandidate.sdpMLineIndex);
        RtcMsg rtcMsg = new RtcMsg();
        rtcMsg.setType(Constant.CANDIDATE);
        rtcMsg.setIceCandidate(iceCandidate);
        String text = new Gson().toJson(rtcMsg);
        Log.d(TAG, "setIceCandidate : " + text);
        sendRtcMsg(rtcMsg);
    }


    /**
     * 准备摄像头
     *
     * @return
     */
    private VideoCapturer createVideoCapturer() {
        if (Camera2Enumerator.isSupported(this)) {
            return createCameraCapturer(new Camera2Enumerator(this));
        } else {
            return createCameraCapturer(new Camera1Enumerator(true));
        }
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer =
                        enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer =
                        enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }

    private void close() {
        if (peerConnection != null) {
            peerConnection.close();
        }
        if (localSurfaceView != null) {
            localSurfaceView.release();
        }
        if (remoteSurfaceView != null) {
            remoteSurfaceView.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        close();
    }

    @Override
    public void doReceive(Object msg) {
        Log.i("receivertcmsg", JSON.toJSONString(msg));
        if (msg instanceof ChatMsg) {
            ChatMsg chatMsg = (ChatMsg) msg;
            if (chatMsg.getMsgType() != Im.MsgTypeEnum.PUSHCMD_VALUE) {
                return;
            }
            PushCmdMsg pushCmdMsg = new Gson().fromJson(chatMsg.getMsg(),
                    PushCmdMsg.class);
            if (pushCmdMsg.getCmdType() != PushCmdConstants.RTC_MSG) {
                return;
            }
            RtcMsg rtcMsg = new Gson().fromJson(pushCmdMsg.getMsg(),
                    RtcMsg.class);
            if (rtcMsg != null) {
                String type = rtcMsg.getType();
                Log.i("rtcmsgtype", type);
                switch (type) {
                    case Constant.OFFER:
                        //收到对方offer sdp
                        SessionDescription sessionDescription1 =
                                rtcMsg.getSessionDescription();
                        peerConnection.setRemoteDescription(observer,
                                sessionDescription1);
                        createAnswer();
                        break;
                    case Constant.CANDIDATE:
                        //服务端 发送 接收方sdpAnswer
                        IceCandidate iceCandidate = rtcMsg.getIceCandidate();
                        if (iceCandidate != null) {
                            peerConnection.addIceCandidate(iceCandidate);
                        }
                        break;

                }
            }
        }


    }

    @Override
    public void doReceiveList(List<Object> msg) {

    }

    private void sendRtcMsg(RtcMsg rtcMsg) {
        PushCmdMsg pushCmdMsg = new PushCmdMsg();
        pushCmdMsg.setCmdType(PushCmdConstants.RTC_MSG);
        pushCmdMsg.setMsg(new Gson().toJson(rtcMsg));
        String msgId = UUID.randomUUID().toString().
                replace("-", "");
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setState(HiConstants.MSG_STATE.SENDING);
        chatMsg.setMsg(new Gson().toJson(pushCmdMsg));
        chatMsg.setBelongType(HiConstants.MSG_BELONG.MY);
        chatMsg.setDate(System.currentTimeMillis() + "");
        chatMsg.setFromUserId(userId);
        chatMsg.setToUserId(friendId);
        chatMsg.setFriendId(friendId);
        chatMsg.setUserId(userId);
        chatMsg.setId(msgId);
        Log.i("sendRtcMsg",
                chatMsg.getToUserId() + "   " + chatMsg.getFromUserId() + "  "
                        + " " + chatMsg.getMsg());
        Im.SingleMsg singleMsg =
                Im.SingleMsg.newBuilder().setSendTimeStamp(System.currentTimeMillis()).setMsgId(msgId).setDeviceType(1).setFromUserId(HiApplication.getInstance().getUser().getUserId()).setToUserId(friendId).setMsgType(Im.MsgTypeEnum.PUSHCMD_VALUE).setContent(chatMsg.getMsg()).build();
        Im.TcpPackage tcpPackage =
                Im.TcpPackage.newBuilder().setContent(singleMsg.toByteString()).setPackageType(Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE).build();
        TcpServer.sendMsg(tcpPackage);
    }

    private void sendCmdMsg(PushCmdMsg pushCmdMsg) {

        String msgId = UUID.randomUUID().toString().
                replace("-", "");
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setState(HiConstants.MSG_STATE.SENDING);
        chatMsg.setMsg(new Gson().toJson(pushCmdMsg));
        chatMsg.setBelongType(HiConstants.MSG_BELONG.MY);
        chatMsg.setDate(System.currentTimeMillis() + "");
        chatMsg.setFromUserId(userId);
        chatMsg.setToUserId(friendId);
        chatMsg.setFriendId(friendId);
        chatMsg.setUserId(userId);
        chatMsg.setId(msgId);
        Im.SingleMsg singleMsg =
                Im.SingleMsg.newBuilder().setSendTimeStamp(System.currentTimeMillis()).setMsgId(msgId).setDeviceType(1).setFromUserId(HiApplication.getInstance().getUser().getUserId()).setToUserId(friendId).setMsgType(Im.MsgTypeEnum.PUSHCMD_VALUE).setContent(chatMsg.getMsg()).build();
        Im.TcpPackage tcpPackage =
                Im.TcpPackage.newBuilder().setContent(singleMsg.toByteString()).setPackageType(Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE).build();
        TcpServer.sendMsg(tcpPackage);
    }
}
