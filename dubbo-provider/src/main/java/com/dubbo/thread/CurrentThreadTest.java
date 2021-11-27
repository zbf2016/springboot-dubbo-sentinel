package com.dubbo.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @title: CurrentThreadTest
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/21 8:10
 */
public class CurrentThreadTest {
    private static Logger logger = LoggerFactory.getLogger(CurrentThreadTest.class);

    public static void current(int threadNum, final Runnable runnable){
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch end = new CountDownLatch(threadNum);

        for(int i=0;i<threadNum;i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        start.await();
                        runnable.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("错误信息为"+e.getStackTrace());
                    } finally {
                        end.countDown();
                    }
                }
            }).start();
        }

        long startTime = System.currentTimeMillis();
        //所有线程开始争抢锁
        start.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            long entTime = System.currentTimeMillis();
            logger.info("所有线程执行时间"+(entTime-startTime));
        }

    }

    public static void main(String[] args) {
        MyRunable myRunable = new MyRunable();
        current(1000,myRunable);
    }
}
