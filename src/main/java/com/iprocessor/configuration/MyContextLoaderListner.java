package com.iprocessor.configuration;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.annotation.WebListener;

@WebListener
public class MyContextLoaderListner extends ContextLoaderListener {

    public MyContextLoaderListner() {
        super(getWebApplicationContext());
    }
    private static WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.scan("com.iprocessor");
        context.refresh();
        return context;
    }
}
