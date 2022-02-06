package com.urlshorter.site.audit.workwithactivemq;

import com.urlshorter.site.audit.AuditMessage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class ProducerServiceActiveMQ {

    public static ConfigurableApplicationContext context;

    public void send(AuditMessage kafkaMessage)  {

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        jmsTemplate.convertAndSend("url-shorter-audit-q", kafkaMessage);

    }

}
