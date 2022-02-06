package com.urlshorter.site.audit.workwithactivemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JMSConfig {
    @Bean
    public ConnectionFactory containerFactory() {
        String url = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;

        return new ActiveMQConnectionFactory(url);
    }
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        org.springframework.jms.support.converter.MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}