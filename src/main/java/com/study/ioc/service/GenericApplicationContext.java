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
import java.util.Map;
import java.util.stream.Collectors;

import static com.study.utils.Util.initCap;

public class GenericApplicationContext implements ApplicationContext {

    private List<BeanDefinition> beanDefinitions;
    private BeanDefinitionReader definitionReader;
    private List<Bean> beans;

    public GenericApplicationContext(String... paths) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        this(new XmlBeanDefinitionReader(paths));
        beanDefinitions = definitionReader.readBeanDefinitions();

        //System.out.println(beanDefinitions);
        beans = new LinkedList<>();
        createBeans();

    }

    public GenericApplicationContext(BeanDefinitionReader definitionReader) {
        this.definitionReader = definitionReader;
    }

    private void createBeans() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        for (BeanDefinition definition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(definition.getId());
            Class clazz = Class.forName(definition.getClassName());
            Object value = clazz.newInstance();
            bean.setValue(value);

            injectValueDependencies(definition, bean.getValue());
            injectRefDependencies(definition, bean.getValue());
            beans.add(bean);
        }

    }

    private void injectValueDependencies(BeanDefinition beanDefinition, Object bean) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        for (Map.Entry<String, String> entry : beanDefinition.getValueDependencies().entrySet()) {

            Class<?> clazz = Class.forName(beanDefinition.getClassName());
            String methodName = "set" + initCap(entry.getKey());

            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    if (field.getType().isPrimitive()) {
                        if (int.class.equals(field.getType())) {
                            method.invoke(bean, Integer.valueOf(entry.getValue()));
                        } else if (long.class.equals(field.getType())) {
                            method.invoke(bean, Long.valueOf(entry.getValue()));
                        } else if (char.class.equals(field.getType())) {
                            method.invoke(bean, entry.getValue().toCharArray()[0]);
                        } else if (short.class.equals(field.getType())) {
                            method.invoke(bean, Short.valueOf(entry.getValue()));
                        } else if (boolean.class.equals(field.getType())) {
                            method.invoke(bean, Boolean.valueOf(entry.getValue()));
                        } else if (byte.class.equals(field.getType())) {
                            method.invoke(bean, Byte.valueOf(entry.getValue()));
                        }
                    } else {
                        method.invoke(bean, entry.getValue());
                    }

                    break;
                }
            }
        }

    }

    private void injectRefDependencies(BeanDefinition beanDefinition, Object bean) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        for (Map.Entry<String, String> entry : beanDefinition.getRefDependencies().entrySet()) {

            Class<?> clazz = Class.forName(beanDefinition.getClassName());
            String methodName = "set" + initCap(entry.getKey());

            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    method.invoke(bean, field.getType().cast(((Bean)getBean(entry.getValue())).getValue()));


                    break;
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
    public <T> List<T> getBean(Class<T> clazz) {
        if (clazz == null || beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream()
                .filter(bean -> bean.getValue().getClass().equals(clazz))
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public <T> T getBean(String beanId, Class<T> clazz) {
        if (beanId == null || beanId.isEmpty() || clazz == null || beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream()
                .filter(bean -> bean.getValue().getClass().equals(clazz) && bean.getId().equals(beanId))
                .findAny()
                .map(clazz::cast)
                .orElse(null);
    }

    @Override
    public List<String> getBeanNames() {
        if (beans == null || beans.size() == 0) {
            return null;
        }
        return beans.stream().map(Bean::getId).collect(Collectors.toList());
    }
}
