package com.tksflysun.hi.tcp.handle;


import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.LastMsg;
import com.tksflysun.hi.common.DateUtil;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.ChatMsgDao;
import com.tksflysun.hi.dao.FriendDao;
import com.tksflysun.hi.dao.LastMsgDao;
import com.tksflysun.hi.tcp.IMsgListener;
import com.tksflysun.hi.tcp.protobuf.Im;
import com.tksflysun.hi.tcp.server.TcpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ProtoBuf解码器，约定前四个字节用于指定需要读取的长度
 *
 * @author
 */
public class TcpMsgHandler extends ChannelInboundHandlerAdapter {
    private static List<IMsgListener> listeners = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Im.TcpPackage) {

            final Im.TcpPackage tcpPackage =
                    Im.TcpPackage.parseFrom(((Im.TcpPackage) msg).toByteString());
            TcpServer.execute(new Runnable() {
                public void run() {
                    try {
                        if (tcpPackage.getPackageType() == Im.PkgTypeEnum.PONG_VALUE) {
                        } else {
                            final List<Object> chats = new ArrayList<>();
                            switch (tcpPackage.getPackageType()) {
                                case Im.PkgTypeEnum.MSG_REQ_RES_VALUE: {
                                    Im.MsgReqRes msgReqRes =
                                            Im.MsgReqRes.parseFrom(tcpPackage.getContent());
                                    int length =
                                            msgReqRes.getSingleMessageListList().size();
                                    Log.i("消息长度", length + "");

                                    for (int i = 0; i < length; i++) {
                                        Im.SingleMsg s =
                                                msgReqRes.getSingleMessageList(i);
                                        // 命令消息暂不保存 ,这里用这种方式，因为服务器端类型写死了
                                        if (s.getContent().indexOf("cmdType") != -1) {
                                            Log.i("是rtc消息", "true");
                                            for (IMsgListener listener :
                                                    listeners) {
                                                ChatMsg chatMsg = new ChatMsg();
                                                chatMsg.setDate(s.getReceiveTimeStamp() + "");
                                                chatMsg.setBelongType(HiConstants.MSG_BELONG.OTHER);
                                                chatMsg.setState(HiConstants.MSG_STATE.SEND_ALREADY);
                                                chatMsg.setMsg(s.getContent());
                                                chatMsg.setFriendId(s.getFromUserId());
                                                chatMsg.setFromUserId(s.getFromUserId());
                                                chatMsg.setToUserId(s.getToUserId());
                                                chatMsg.setUserId(s.getToUserId());
                                                chatMsg.setMsgType(Im.MsgTypeEnum.PUSHCMD_VALUE);
                                                chatMsg.setId(s.getMsgId());
                                                listener.doReceive(chatMsg);
                                            }
                                            continue;
                                        }
                                        Log.i("服务器消息", s.getContent());
                                        if (new ChatMsgDao().queryById(s.getMsgId()) != null) {
                                            Log.i("消息已经接收过", "消息已经接收过");
                                            continue;
                                        }
                                        ChatMsg chatMsg = new ChatMsg();
                                        chatMsg.setDate(s.getReceiveTimeStamp() + "");
                                        chatMsg.setBelongType(HiConstants.MSG_BELONG.OTHER);
                                        chatMsg.setState(HiConstants.MSG_STATE.SEND_ALREADY);
                                        chatMsg.setMsg(s.getContent());
                                        chatMsg.setFriendId(s.getFromUserId());
                                        chatMsg.setFromUserId(s.getFromUserId());
                                        chatMsg.setToUserId(s.getToUserId());
                                        chatMsg.setUserId(s.getToUserId());
                                        chatMsg.setMsgType(s.getMsgType());
                                        chatMsg.setId(s.getMsgId());
                                        LastMsg lastMsg = new LastMsg();
                                        Friend f =
                                                new FriendDao().queryByUserIdAndFriendID(s.getToUserId(), s.getFromUserId());
                                        lastMsg.setFriendId(chatMsg.getFriendId());
                                        lastMsg.setLastMsg(chatMsg.getMsg());
                                        if (f != null) {
                                            Log.i("好友信息：",
                                                    JSON.toJSONString(f));
                                            lastMsg.setNickName(f.getRemarkName());
                                        }
                                        lastMsg.setTime(DateUtil.getTimeHHMM());
                                        lastMsg.setId(chatMsg.getToUserId() + "_" + chatMsg.getFriendId());
                                        lastMsg.setUserId(chatMsg.getToUserId());
                                        new LastMsgDao().insert(lastMsg);
                                        chats.add(chatMsg);
                                        new ChatMsgDao().insert(chatMsg);
                                    }

                                    for (IMsgListener i : listeners) {
                                        i.doReceiveList(chats);
                                    }
                                    if (chats.size() == 0) {
                                        break;
                                    }
                                    //接收完发送消息ack
                                    Log.i("ack包", msgReqRes.getSrlNo() + "");
                                    Im.MsgReqAck ack =
                                            Im.MsgReqAck.newBuilder().setSrlNo(msgReqRes.getSrlNo()).setUserId(HiApplication.getInstance().getUser().getUserId()).setTimeStamp(System.currentTimeMillis()).build();
                                    Im.TcpPackage send =
                                            Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.MSG_REQ_ACK_VALUE).setContent(ack.toByteString()).build();
                                    TcpServer.sendMsg(send);
                                    break;
                                }
                                case Im.PkgTypeEnum.MSG_INFORM_VALUE: {
                                    HiApplication.getInstance().notificationNewMsg();
                                    Log.i("请求消息", "请求消息");
                                    Im.MsgInform msgInform =
                                            Im.MsgInform.parseFrom(tcpPackage.getContent());
                                    Im.MsgReq msgReq =
                                            Im.MsgReq.newBuilder().setCurrentPage(1).setPageSize(10).setSrlNo(0l).setUserId(HiApplication.getInstance().getUser().getUserId()).setTimeStamp(System.currentTimeMillis()).build();
                                    Im.TcpPackage send =
                                            Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.MSG_REQ_VALUE).setContent(msgReq.toByteString()).build();
                                    TcpServer.sendMsg(send);
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                    } catch (Throwable e) {
                        Log.i("收到消息解析失败", e.getMessage());
                    }
                }

            });

        }
        ctx.fireChannelRead(msg);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        TcpServer.start();
    }

    public static void addListener(IMsgListener listener) {
        Log.i("listener", "注册监听");
        listeners.add(listener);
    }

    public static void removeListener(IMsgListener listener) {
        Log.i("listener", "销毁监听");
        listeners.remove(listener);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Log.i("长连接失效", "已经超过两个心跳的时间没有接收到服务器的消息了，重新建立连接了需要");
                TcpServer.start();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                Log.i("长连接心跳", "超过3分钟没有联系服务器了，发送心跳");
                TcpServer.sendMsg(Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.PING_VALUE).build());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
