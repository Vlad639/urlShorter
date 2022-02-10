package com.urlshorter.site.audit.auditproducersimplements;

import com.urlshorter.site.audit.AuditMessage;
import com.urlshorter.site.audit.AuditProducer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaAuditProducer implements AuditProducer {

    private final Producer<String, AuditMessage> producer;

    public KafkaAuditProducer() throws ExecutionException, InterruptedException {

        final Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        producer = new KafkaProducer<>(properties);

    }

    public void produce(AuditMessage auditMessage) {
        try {
            producer.send(new ProducerRecord<>("url-shorter-audit", auditMessage)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
