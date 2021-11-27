package com.dubbo;

/**
 * @author Administrator
 * @title: ResourceName
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/23 14:26
 */
public enum ResourceNameEnum {

    TEST_SERVICE("com.dubbo.service.TestService","测试");

    private String name;
    private String desc;

    ResourceNameEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
