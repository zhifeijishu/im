package com.tksflysun.im.connect.task.execute;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecute {
    private static final ExecutorService receiveMsgPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());

    public static void execute(Runnable task) {
        receiveMsgPool.execute(task);
    }
}
