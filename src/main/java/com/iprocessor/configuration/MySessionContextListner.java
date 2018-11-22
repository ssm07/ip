package com.iprocessor.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.annotation.WebListener;

@Configuration
@WebListener
public class MySessionContextListner extends RequestContextListener {
}
