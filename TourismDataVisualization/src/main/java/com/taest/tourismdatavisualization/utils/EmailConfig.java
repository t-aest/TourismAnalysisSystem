package com.taest.tourismdatavisualization.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfig {

    @Autowired
    JavaMailSenderImpl javaMailSender;


}
