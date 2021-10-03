package it.codingjam.poc.debeziumengine.listeners;

import it.codingjam.poc.debeziumengine.models.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CdcEventListener {

    @EventListener
    public void onChange(CdcEvent event) {
        log.info("Receiving changes from {}", event.destination());
        log.info("Event KEY: {} ", event.key());
        log.info("Event VALUE: {}", event.value());

        // throw new RuntimeException("Ops!!");
    }
}
