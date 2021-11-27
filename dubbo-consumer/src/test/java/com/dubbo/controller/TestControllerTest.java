package com.dubbo.controller;

import com.dubbo.ConsumerApplication;
import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Administrator
 * @title: TestControllerTest
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/22 18:31
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = ConsumerApplication.class)
@RunWith(SpringRunner.class)
public class TestControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    public void test(){
        for(int i=0;i<100;i++){
            try {
                Thread.sleep(500);
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/query")).andReturn();
                String contentAsString = mvcResult.getResponse().getContentAsString();
                System.out.println(contentAsString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }




}