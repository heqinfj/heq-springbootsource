package com.springboot.lesson2;

import org.springframework.stereotype.Component;

/**
 * @Auther: lesson2
 * @Date: 2018/12/2
 * @Description:
 */
@Component
public class MyBean implements IMyBean {
    private String customValue;

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

}
