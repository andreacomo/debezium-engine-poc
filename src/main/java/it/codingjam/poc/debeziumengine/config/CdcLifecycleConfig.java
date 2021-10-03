package it.codingjam.poc.debeziumengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.codingjam.poc.debeziumengine.models.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CdcLifecycleConfig {

    @Bean(destroyMethod = "shutdown")
    public CdcEngine cdcEngine(ApplicationEventPublisher publisher) {
        return new CdcEngine(
                "postgres.properties",
                record -> publisher.publishEvent(new CdcEvent(record)));
    }
}
