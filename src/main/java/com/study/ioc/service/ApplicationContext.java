package com.study.ioc.service;

import java.util.List;

public interface ApplicationContext<T> {
    Object getBean(String beanId);
    T getBean(Class<T> clazz);
    T getBean(String beanId, Class<T> clazz);
    List<String> getBeanNames();
}
