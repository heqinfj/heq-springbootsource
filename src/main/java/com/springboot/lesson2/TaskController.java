package com.springboot.lesson2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Auther: lesson2
 * @Date: 2018/12/3
 * @Description:
 */
@Controller
public class TaskController {

    @Autowired
    private DBConnector dbConnector;

    public void helloTask(){
        dbConnector.configure();
    }
}
