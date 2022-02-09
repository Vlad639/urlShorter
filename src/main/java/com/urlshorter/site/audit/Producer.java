package com.urlshorter.site.audit;

import com.urlshorter.site.audit.auditproducersimplements.ActiveMQAuditProducer;
import com.urlshorter.site.audit.auditproducersimplements.KafkaAuditProducer;
import com.urlshorter.site.other.WrongConsoleParameterException;

public class Producer {

    public  static  String auditName = "Kafka";

    AuditProducer auditProducer;

    public Producer(){
        try {
            switch (auditName) {
                case "Kafka" -> auditProducer = new KafkaAuditProducer();
                case "ActiveMQ" -> auditProducer = new ActiveMQAuditProducer();
                default -> throw new WrongConsoleParameterException("Wrong console parameter!");
            }

        } catch (WrongConsoleParameterException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public void produce(AuditMessage auditMessage){
        auditProducer.produce(auditMessage);
    }


}
