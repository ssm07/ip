package com.iprocessor.configuration;


import com.iprocessor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StartUpInit {

    @Autowired
    @Qualifier("gaussian")
    Chain gaussianFilter;

    @Autowired
    @Qualifier("averaging")
    Chain averagingFilter;

    @Autowired
    @Qualifier("median")
    Chain medianFilter;

    @Autowired
    @Qualifier("bilateral")
    Chain bilateralFilter;

    @Autowired
    @Qualifier("equalization")
    Chain histogramEqualization;

    @Autowired
    @Qualifier("canny")
    Chain cannyFilter;

    @Autowired
    @Qualifier("laplace")
    Chain laplace;

    @Autowired
    @Qualifier("sobel")
    Chain sobel;

    @PostConstruct
    public void init(){
        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>\n start up init \n >>>>>>>>>>>>>");
        gaussianFilter.setNextChain(averagingFilter);
        averagingFilter.setNextChain(medianFilter);
        medianFilter.setNextChain(bilateralFilter);
        bilateralFilter.setNextChain(histogramEqualization);
        histogramEqualization.setNextChain(cannyFilter);
        cannyFilter.setNextChain(laplace);
        laplace.setNextChain(sobel);

    }
}
