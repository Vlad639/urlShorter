package com.urlshorter.site.audit;

public interface AuditProducer {

    void produce(AuditMessage auditMessage);

}
