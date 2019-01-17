package com.wisely.ch5_2_2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂
 *
 * @Auther: HeQin
 * @Date: 2018/10/26 10:37
 * @Description:
 */
public class ThreadPoolFactory {

    private ThreadPoolFactory() {

    }

    // 线程池维护线程的最小数量
    private static int corePoolSize = 8;

    // 线程池维护线程的最大数量
    private static int maxNumPoolSize = 16;

    // 线程池维护线程所允许的空闲时间(单位秒)
    private static long keepAliveTime = 600;

    // 线程池维护线程所允许的空闲时间单元
    private static TimeUnit timeUnit = TimeUnit.SECONDS;

    // 有界队列最大长度
    private static int maxQueueSize = 100;//1000

    // 有界队列，当使用有限的 MAX_POOL_SIZE 时，有界队列（如
    // ArrayBlockingQueue）有助于防止资源耗尽，但是可能较难调整和控制。
    private static ArrayBlockingQueue<Runnable> queue = null;

    static {
        // 获取配置信息
        queue = new ArrayBlockingQueue<Runnable>(maxQueueSize);
    }

    private static class ThreadPoolExecutorInstance {
        // ThreadPoolExecutor.CallerRunsPolicy 表示任务被拒绝时的执行策略(调用者的线程中直接执行该任务)
        private static RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        static {
            //ThreadPoolExecutor.AbortPolicy --》A handler for rejected tasks that throws a RejectedExecutionException.
            //rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
            rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        private static final ThreadPoolExecutor instance = new ThreadPoolExecutor(corePoolSize, maxNumPoolSize,
                keepAliveTime, timeUnit, queue, rejectedExecutionHandler);

    }

    public static ThreadPoolExecutor getInstance() {
        return ThreadPoolExecutorInstance.instance;
    }

}
