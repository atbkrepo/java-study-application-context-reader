package com.study.ioc.service;

import com.study.ioc.entity.Bean;
import com.study.ioc.entity.BeanDefinition;
import com.study.ioc.readers.BeanDefinitionReader;
import com.study.ioc.readers.impl.XmlBeanDefinitionReader;

import java.util.List;

public class GenericApplicationContext implements ApplicationContext {

    BeanDefinitionReader definitionReader;
    List<BeanDefinition> beanDefinitions;
    List<Bean> beans;

    public GenericApplicationContext(String... paths) {
        definitionReader = new XmlBeanDefinitionReader(paths);
        beanDefinitions = definitionReader.readBeanDefinitions();

        System.out.println(beanDefinitions);
    }

    public Object getBean(String beanId) {
        return null;
    }

    public Object getBean(Class clazz) {
        return null;
    }

    public Object getBean(String beanId, Class clazz) {
        return null;
    }

    public List<String> getBeanNames() {
        return null;
    }
}
