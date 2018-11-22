package com.iprocessor;


import com.iprocessor.DTO.ImageDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



/**
 *
   Object <|-- ArrayList
   Object : equals()
ArrayList : Object[] elementData
ArrayList : size()
 *
 * */
@SpringBootApplication
@EnableAutoConfiguration
public class IpApplication  implements    WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(IpApplication.class, args);

	}


}


