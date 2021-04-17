package com.mailspaghetti.services.templateengine.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.mailspaghetti.services.templateengine.common.DirectoryHelper;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@Configuration
@EnableScheduling
public class AppConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String rootPath = DirectoryHelper.getRootDirectory(); //System.getProperty("user.dir");
        String strAbsolutePath = "file:///" + rootPath + "/webapp/documents/";
        registry.addResourceHandler("/documents/**").
                addResourceLocations(strAbsolutePath);
    }
}