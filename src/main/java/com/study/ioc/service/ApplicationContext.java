package com.study.ioc.service;

import java.util.List;

public interface ApplicationContext{
    Object getBean(String beanId);
    <T> List<T> getBean(Class<T> clazz);
    <T> T getBean(String beanId, Class<T> clazz);
    List<String> getBeanNames();
}
