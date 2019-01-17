package com.springboot.bbp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <a>http://www.cnblogs.com/yuxiang1/archive/2018/06/19/9199730.html</a>
 */
@SpringBootApplication()
public class TestApplication {
    private static Logger log = LoggerFactory.getLogger(TestApplication.class);
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(TestApplication.class, args);
    }
}
