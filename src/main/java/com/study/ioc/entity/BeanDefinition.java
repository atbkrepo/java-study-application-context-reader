package com.study.ioc.entity;

import java.util.Map;

public class BeanDefinition {
    private String id;
    private String ClassName;
    private Map<String, String> valueDependencies;
    private Map<String, String> refDependencies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public Map<String, String> getValueDependencies() {
        return valueDependencies;
    }

    public void setValueDependencies(Map<String, String> valueDependencies) {
        this.valueDependencies = valueDependencies;
    }

    public Map<String, String> getRefDependencies() {
        return refDependencies;
    }

    public void setRefDependencies(Map<String, String> refDependencies) {
        this.refDependencies = refDependencies;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +'\n'+
                '\t'+"id='" + id + "'," +'\n'+
                '\t'+"ClassName='" + ClassName + "'," +'\n'+
                '\t'+"valueDependencies=" + valueDependencies +','+'\n'+
                '\t'+"refDependencies=" + refDependencies +'\n'+
                '}';
    }
}
