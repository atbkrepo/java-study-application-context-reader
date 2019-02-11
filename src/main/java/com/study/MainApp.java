package com.study;

import com.study.ioc.service.ApplicationContext;
import com.study.ioc.service.GenericApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class MainApp {
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ApplicationContext applicationContext = new GenericApplicationContext("context.xml");

//        IMailService iMailService = (IMailService) applicationContext.getBean("mailServiceInstance");

   /*     UserService userService = applicationContext.getBean(UserService.class);
        userService.activateUsers();*/
    }


}
