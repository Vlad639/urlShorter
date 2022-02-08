package com.urlshorter.site.audit.auditproducersimplements;

import com.urlshorter.site.audit.AuditMessage;
import com.urlshorter.site.audit.AuditProducer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class ActiveMQAuditProducer implements AuditProducer {

    public static ConfigurableApplicationContext context;

    public void produce(AuditMessage kafkaMessage)  {

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        jmsTemplate.convertAndSend("url-shorter-audit-q", kafkaMessage);

    }

}
