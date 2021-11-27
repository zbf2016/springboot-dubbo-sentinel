package com.dubbo.service;

/**
 * @author Administrator
 * @title: TestService
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/20 19:05
 */
public interface TestService {
    public void test(String name);

    public void ins(String threadName);

    public void query();

    public void delete();

    public void update();
}
