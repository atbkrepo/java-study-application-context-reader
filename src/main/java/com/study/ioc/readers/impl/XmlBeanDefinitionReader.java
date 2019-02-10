package com.study.ioc.readers.impl;

import com.study.ioc.entity.BeanDefinition;
import com.study.ioc.readers.BeanDefinitionReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.study.utils.Util.loadClassPathProperties;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    private List<BeanDefinition> beanDefinitions;

    public XmlBeanDefinitionReader(String[] paths) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            SAXBeanParser saxBeanParser = new SAXBeanParser();
            try (InputStream stream = loadClassPathProperties(paths[0])) {
                saxParser.parse(stream, saxBeanParser);
            }

            beanDefinitions = saxBeanParser.getBeanDefinitions();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    public List<BeanDefinition> readBeanDefinitions() {
        return beanDefinitions;
    }
}

class SAXBeanParser extends DefaultHandler {

    private BeanDefinition currentBeanDefinition;
    private Map<String, String> currentValueDependencies;
    private Map<String, String> currentRefDependencies;
    private List<BeanDefinition> beanDefinitions;

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parse XML...");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("BEANS")) {
            beanDefinitions = new LinkedList<>();

        } else if (qName.equalsIgnoreCase("BEAN")) {
            currentBeanDefinition = new BeanDefinition();
            currentBeanDefinition.setId(attributes.getValue("id"));
            currentBeanDefinition.setClassName(attributes.getValue("class"));
            currentValueDependencies = new HashMap<>();
            currentRefDependencies = new HashMap<>();

        } else if (qName.equalsIgnoreCase("PROPERTY")) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String ref = attributes.getValue("ref");

            if (name != null && !name.isEmpty()) {
                if (value != null && !value.isEmpty()) {
                    currentValueDependencies.put(name, value);
                } else if (ref != null && !ref.isEmpty()) {
                    currentRefDependencies.put(name, ref);
                }
            }

        } else {
            System.out.println("Unknown element: " + qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("BEANS")) {

        } else if (qName.equalsIgnoreCase("BEAN")) {
            currentBeanDefinition.setValueDependencies(currentValueDependencies);
            currentBeanDefinition.setRefDependencies(currentRefDependencies);
            beanDefinitions.add(currentBeanDefinition);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End parse XML...");
    }
}
