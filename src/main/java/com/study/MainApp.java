package com.study;

import com.study.ioc.service.ApplicationContext;
import com.study.ioc.service.GenericApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new GenericApplicationContext("context.xml");
//        IMailService iMailService = (IMailService) applicationContext.getBean("mailServiceInstance");

   /*     UserService userService = applicationContext.getBean(UserService.class);
        userService.activateUsers();*/
    }


}
