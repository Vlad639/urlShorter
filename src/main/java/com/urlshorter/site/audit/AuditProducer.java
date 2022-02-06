package com.urlshorter.site.audit;

import com.urlshorter.site.other.WrongConsoleParameterException;
import com.urlshorter.site.audit.workwithactivemq.ProducerServiceActiveMQ;
import com.urlshorter.site.audit.workwithkafka.ProducerServiceKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditProducer {

    public static  String auditName = "Kafka";

    @Autowired
    ProducerServiceKafka producerServiceKafka;

    @Autowired
    ProducerServiceActiveMQ producerServiceActiveMQ;

    public void produce(AuditMessage auditMessage){

        try {
            if (auditName.equals("Kafka")) {
                producerServiceKafka.produce(auditMessage);
            }
            else
                if (auditName.equals("ActiveMQ")) {
                    producerServiceActiveMQ.send(auditMessage);
            } else
                throw new WrongConsoleParameterException("Wrong console parameter!");

        }
        catch (WrongConsoleParameterException exception){
            exception.printStackTrace();
            System.exit(1);
        }
    }


}
