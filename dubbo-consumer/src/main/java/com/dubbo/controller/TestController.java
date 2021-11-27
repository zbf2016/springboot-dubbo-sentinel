package com.dubbo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.dubbo.SentinelBase;
import com.dubbo.service.TestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 * @title: TestController
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/20 19:59
 */
@RestController
public class TestController extends SentinelBase {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    private AtomicInteger atomic = new AtomicInteger(0);

   // public String RESOURCE_NAME="com.dubbo.service.TestService";

    @DubboReference(version = "1.0.0",check = false,lazy = true,retries = 0)
    private TestService testService;

    public static final String RESOURCE_NAME = "testService";
    /**
     * 初始化流控规则和熔断规则
     * ps:因为我们没有接入 Sentinel Dashboard，所以得自己在代码里面设置好
     */
   /* static{
        // 初始化流控规则
        final List<FlowRule> flowRules = new ArrayList<>();
        final List<DegradeRule> degradeRules = new ArrayList<>();
        // 限流规则
        final FlowRule flowRule = new FlowRule();
        flowRule.setResource(RESOURCE_NAME);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 1 QPS
        flowRule.setCount(1);
        flowRules.add(flowRule);
        // 熔断规则
        final DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(RESOURCE_NAME);
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        // 2个异常数
        degradeRule.setCount(2);
        // 时间窗口长度，单位为秒
        degradeRule.setTimeWindow(5);
        // 最小请求数
        degradeRule.setMinRequestAmount(5);
        // 熔断时长：当5秒内，10个请求里面出现2个异常，则进行熔断，熔断时长为10s
        degradeRule.setStatIntervalMs(10000);
        degradeRules.add(degradeRule);
        FlowRuleManager.loadRules(flowRules);
        DegradeRuleManager.loadRules(degradeRules);
    }*/



    @RequestMapping("/ins")
    public String ins() {
        current(1000,testService);
        return "ins";
    }

    @RequestMapping("/test")
    public String test(){
        testService.ins("111");
        return "test";
    }

    @RequestMapping("/query")
    public String query() {
        Entry entry = null;
        int andIncrement = atomic.getAndIncrement();
        try {
            this.setSentinel(RESOURCE_NAME);
            entry = SphU.entry(RESOURCE_NAME);
            testService.query();
        } catch (BlockException  e){
            logger.error("查询次数"+andIncrement);
            if (e instanceof DegradeException){
                logger.error("资源：{} 被熔断了,message is {}",RESOURCE_NAME,e.getMessage());
            }else{
                logger.error("资源：{} 被流控了,message is {}",RESOURCE_NAME,e.getMessage());
            }
        }catch (Exception e){
            Tracer.traceEntry(e, entry);
            logger.error("业务异常"+e.getMessage());
        }finally {
            // 务必保证 exit，务必保证每个 entry 与 exit 配对
            if (entry != null) {
                entry.exit();
            }
        }
        return "查询次数"+andIncrement;
    }

    @RequestMapping("/delete")
    public String delete() {
        testService.delete();
        return "delete";
    }

    @RequestMapping("/update")
    public String update() {
        testService.update();
        return "update";
    }

    public  void current(int threadNum, final TestService testServiceImpl) {
        for (int i = 0; i < threadNum; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                public void run() {
                    testServiceImpl.ins("第"+ finalI +"条线程，线程名："+Thread.currentThread().getName());
                }
            }).start();
        }
    }


}
