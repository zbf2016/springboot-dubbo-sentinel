package com.dubbo.impl;

import com.dubbo.mapper.UserMapper;
import com.dubbo.po.User;
import com.dubbo.service.TestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Administrator
 * @title: TestServiceImpl
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/20 19:06
 */
@DubboService(version = "1.0.0", interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {

    @Autowired
    private UserMapper userMapper;

    private AtomicInteger a = new AtomicInteger(1);

    private Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    public void test(String name) {
        logger.info(name);
    }

    public void ins(String threadName) {
       /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        logger.info("线程名" + threadName);
        List<User> userPOS = userMapper.selectList(null);
        userPOS.stream().forEach(a -> logger.info(a.toString()));
        try {
            userPOS.stream().forEach(a -> Optional.ofNullable(a).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e) {
            logger.error("error optional is error");
        }

    }

    public void query() {
        logger.info("查询数据量" + a.getAndIncrement());
    }

    public void delete() {
        logger.info("delete---");
    }

    public void update() {
        logger.info("update----");
    }
}
