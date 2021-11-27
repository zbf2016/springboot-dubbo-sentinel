package com.dubbo.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * @title: MyRunable
 * @projectName dubbo-springboot
 * @description: 线程类
 * @date 2021/11/21 7:56
 */
public class MyRunable implements Runnable{

    private Logger logger = LoggerFactory.getLogger(MyRunable.class);

    //公共资源
    public volatile static Boolean isTrue = true;
    //如果不使用同步锁，多线程并发一定会出现并发安全问题  使用volatile关键字在高并发下不能完全保证线程安全
    public static Lock lock = new ReentrantLock();

    public void run() {
        try{
            String name = Thread.currentThread().getName();
            if(lock.tryLock()){
                if(isTrue){
                    logger.info("第一个抢到资源,线程名："+name);
                    logger.info("开始等待");
                    try {
                        Thread.sleep(100000);
                        logger.info("查看当前线程状态"+isTrue);
                        if(isTrue){
                            logger.info("资源没有被修改");
                            isTrue = false;
                        }else{
                            logger.info("资源已经被修改");
                        }
                    } catch (InterruptedException e) {
                        logger.error("错误信息"+e.getStackTrace());
                    }
                }else{
                    logger.info("资源已经被修改，线程名："+name);
                }
            }else{
                logger.info("没有拿到锁，线程名："+name);
            }
        }finally {
            if(lock.tryLock()){
                lock.unlock();
            }
        }

    }
}
