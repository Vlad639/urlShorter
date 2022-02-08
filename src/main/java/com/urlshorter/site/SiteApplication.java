package com.urlshorter.site;

import com.urlshorter.site.audit.Producer;
import com.urlshorter.site.audit.auditproducersimplements.ActiveMQAuditProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiteApplication {

	public static void main(String[] args) {

		ActiveMQAuditProducer.context = SpringApplication.run(SiteApplication.class, args);

		if (args.length > 0)
			Producer.auditName = args[0];

	}

}
