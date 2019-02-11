package com.study.service;

public class DefaultUserService {
    MailService mailService;

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String toString() {
        return "DefaultUserService{" +
                "mailService=" + mailService +
                '}';
    }
}
