package com.springboot.bbp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Auther: heq
 * @Date: 2018/12/4
 * @Description:
 */
@Component
public class Bean4BBP implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(Bean4BBP.class);

    public Bean4BBP(){
        log.info("construct Bean4BBP");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("init Bean4BBP");
    }
}
