package com.urlshorter.site.audit.auditproducersimplements;

import com.urlshorter.site.audit.AuditMessage;
import com.urlshorter.site.audit.AuditProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaAuditProducer implements AuditProducer {

    @Autowired
    private KafkaTemplate<String, AuditMessage> kafkaTemplate;

    public void produce(AuditMessage auditMessage) {
        kafkaTemplate.send("url-shorter-audit", auditMessage);
    }
}
