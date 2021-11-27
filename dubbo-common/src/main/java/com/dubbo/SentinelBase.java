package com.dubbo;


import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @title: SentinelBase
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/23 11:22
 */
public class SentinelBase {

    /**
     * 初始化流控规则和熔断规则
     * ps:因为我们没有接入 Sentinel Dashboard，所以得自己在代码里面设置好
     */
     public void setSentinel(String resourceName){
        // 初始化流控规则
        final List<FlowRule> flowRules = new ArrayList<>();
        final List<DegradeRule> degradeRules = new ArrayList<>();
        // 限流规则
        final FlowRule flowRule = new FlowRule();
        flowRule.setResource(resourceName);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 1 QPS
        flowRule.setCount(1);
        flowRules.add(flowRule);
        // 熔断规则
        final DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(resourceName);
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
    }

}
