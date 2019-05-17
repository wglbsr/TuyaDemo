package com.tuya.open.sdk.utils;

import java.util.concurrent.*;

/**
 * @author: bilahepan
 * @date: 2019/4/16 下午5:41
 */
public class ThreadPoolFactory {

    //获取线程池
    public static ExecutorService getCustomThreadPool() {
        return ThreadPoolFactory.CustomThreadPoolExecutorHolder.poolExecutor;
    }


    /**
     * 自定义线程池，拒绝策略-抛出异常RejectedExecutionException
     * 工作队列容量 5000
     * 核心线程数 10
     * 最大线程数 30
     *
     * @author: 文若[gaotc@tuya.com]
     * @date: 2018/12/4 下午4:13
     */
    public static class CustomThreadPoolExecutorHolder {
        /**
         * 线程池
         */
        private static final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 30, 30, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(5000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
}