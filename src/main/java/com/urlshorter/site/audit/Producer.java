package com.urlshorter.site.audit;

import com.urlshorter.site.audit.auditproducersimplements.ActiveMQAuditProducer;
import com.urlshorter.site.audit.auditproducersimplements.KafkaAuditProducer;
import com.urlshorter.site.other.WrongConsoleParameterException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class Producer {

    public static String auditName = "Kafka";

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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void produce(AuditMessage auditMessage){
        auditProducer.produce(auditMessage);
    }


}
