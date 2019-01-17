package com.springboot.lesson2;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @Auther: lesson2
 * @Date: 2018/12/3
 * @Description:
 */
@Component
@Profile({"prodDB"})
public class ProdDBConnector implements DBConnector {
    @Override
    public void configure() {
        System.out.println("生产数据库...");
    }
}
