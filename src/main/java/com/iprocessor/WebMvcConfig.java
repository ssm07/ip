/*
package com.iprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



public class WebMvcConfig    {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("add view controller");
        registry.setOrder(Ordered.LOWEST_PRECEDENCE);
        registry.addViewController("/**").setViewName("forward:/index.html");
    }

}
*/
