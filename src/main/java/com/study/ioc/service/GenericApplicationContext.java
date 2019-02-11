package com.study.ioc.service;

import com.study.ioc.entity.Bean;
import com.study.ioc.entity.BeanDefinition;
import com.study.ioc.readers.BeanDefinitionReader;
import com.study.ioc.readers.impl.XmlBeanDefinitionReader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericApplicationContext implements ApplicationContext {

    private List<BeanDefinition> beanDefinitions;
    private BeanDefinitionReader definitionReader;
    private List<Bean> beans;

    public GenericApplicationContext(String... paths) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        this(new XmlBeanDefinitionReader(paths));
        beanDefinitions = definitionReader.readBeanDefinitions();

        //System.out.println(beanDefinitions);
        beans = new LinkedList<>();
        createBeans();
        injectValueDependencies();
        injectRefDependencies();
    }

    public GenericApplicationContext(BeanDefinitionReader definitionReader) {
        this.definitionReader = definitionReader;
    }

    private void createBeans() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (BeanDefinition definition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(definition.getId());
            Class clazz = Class.forName(definition.getClassName());
            Object value = clazz.newInstance();
            bean.setValue(value);
            beans.add(bean);
        }

    }

    private void injectValueDependencies() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (BeanDefinition definition : beanDefinitions) {
            Class clazz = Class.forName(definition.getClassName());

            for (Field field : clazz.getDeclaredFields()) {
                String value = definition.getValueDependencies().get(field.getName());
                if (value != null) {
                    char[] fieldName = field.getName().toCharArray();
                    fieldName[0] = Character.toUpperCase(fieldName[0]);
                    Method method = clazz.getMethod("set" + new String(fieldName),int.class);
                    method.invoke(getBean(definition.getId(), clazz), value);
                }
            }
        }

    }

    private void injectRefDependencies() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (BeanDefinition definition : beanDefinitions) {
            Class clazz = Class.forName(definition.getClassName());

            for (Field field : clazz.getDeclaredFields()) {
                String ref = definition.getRefDependencies().get(field.getName());
                if (ref != null) {
                    char[] fieldName = field.getName().toCharArray();
                    fieldName[0] = Character.toUpperCase(fieldName[0]);
                    Method method = clazz.getMethod("set" + new String(fieldName));
                    method.invoke(getBean(definition.getId(), clazz), getBean(ref));
                }
            }
        }
    }

    @Override
    public Object getBean(String beanId) {
        if (beanId == null || beanId.isEmpty() || beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream().filter(bean -> bean.getId().equals(beanId)).findFirst().orElse(null);
    }

    @Override
    public List<Object> getBean(Class clazz) {
        if (clazz == null || beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream().filter(bean -> bean.getValue().getClass().equals(clazz)).collect(Collectors.toList());
    }

    @Override
    public Object getBean(String beanId, Class clazz) {
        if (beanId == null || beanId.isEmpty() || clazz == null || beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream().filter(bean -> bean.getValue().getClass().equals(clazz) && bean.getId().equals(beanId)).findAny().orElse(null);
    }

    @Override
    public List<String> getBeanNames() {
        if (beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream().map(Bean::getId).collect(Collectors.toList());
    }
}
