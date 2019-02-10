package com.study.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Util {
    public static InputStream loadClassPathProperties(String fileName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(fileName);
    }

    public static InputStream loadProperties(String fileName) {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + fileName;
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("[can't read file]:" + filePath);
            return null;
        }
    }
}