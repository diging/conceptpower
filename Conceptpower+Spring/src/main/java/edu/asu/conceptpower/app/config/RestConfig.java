package edu.asu.conceptpower.app.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
@ComponentScan("edu.asu.conceptpower.rest")
public class RestConfig {

    @Bean
    public String viafURL() {
        return "http://viaf.org/viaf/search";
    }

    @Bean
    public String searchViafURLPath() {
        return "?query=local.names%20all";
    }

    @Bean
    public String searchViafURLPath1() {
        return "+&maximumRecords=100&startRecord=";
    }

    @Bean
    public String searchViafURLPath2() {
        return "&sortKeys=holdingscount";
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(new MarshallingHttpMessageConverter(jaxbMarshaller()),
                new FormHttpMessageConverter(), new SourceHttpMessageConverter()));
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateViaf() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(new MarshallingHttpMessageConverter(jaxbMarshaller()),
                new FormHttpMessageConverter(), new SourceHttpMessageConverter()));
        return restTemplate;
    }

    @Bean
    public Jaxb2Marshaller jaxbMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(edu.asu.conceptpower.app.profile.impl.ViafReply.class);
        return marshaller;
    }

    @Bean
    public MarshallingHttpMessageConverter marshallingConverter() {
        MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter(jaxbMarshaller());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_XML));
        return converter;
    }

    @Bean
    public PropertiesFactoryBean errorMessages() {
        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
        factoryBean.setLocation(new ClassPathResource("locale/errormessages.properties"));
        return factoryBean;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(Collections.singletonList(jacksonMessageConverter()));
        return adapter;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("validatormessages");
        return messageSource;
    }

}
