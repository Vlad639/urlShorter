package com.urlshorter.site;

import com.urlshorter.site.audit.AuditProducer;
import com.urlshorter.site.audit.workwithactivemq.ProducerServiceActiveMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiteApplication {

	public static void main(String[] args) {

		ProducerServiceActiveMQ.context = SpringApplication.run(SiteApplication.class, args);

		if (args.length > 0)
			AuditProducer.auditName = args[0];

	}

}
