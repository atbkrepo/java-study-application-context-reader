package com.study.service;

public class MailService {
    private int port;
    private String protocol;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "MailService{" +
                "port=" + port +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
