package com.tksflysun.im.process.task.execute;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecute {
    // 消息发送的pool，单聊，群聊
    private static final ExecutorService msgSendPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());
    // 消息请求pool
    private static final ExecutorService msgReqPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());
    // 消息应答pool
    private static final ExecutorService msgReqAckPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());

    public static void executeMsgSend(Runnable task) {
        msgSendPool.execute(task);
    }

    public static void executeMsgReqAck(Runnable task) {
        msgReqAckPool.execute(task);

    }

    public static void executeMsgReq(Runnable task) {
        msgReqPool.execute(task);

    }
}
