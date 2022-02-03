package com.urlshorter.site.workwithkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void produce(KafkaMessage kafkaMessage) {
        kafkaTemplate.send("url-shorter-audit", kafkaMessage);
    }
}
