package com.springboot.lesson2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Auther: lesson2
 * @Date: 2018/12/2
 * @Description:
 */
@SpringBootApplication()
public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(Application.class);
        // 非web环境
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext application = springApplication.run(args);

        //ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        //MyBean mybean = application.getBean(MyBean.class);
        //log.info("getCustomValue:" + mybean.getCustomValue());

        //TaskController taskController = application.getBean(TaskController.class);
        //taskController.helloTask();

        log.info("beanFactory===>" + application.getBeanFactory().getClass());
        application.close();
    }
}
