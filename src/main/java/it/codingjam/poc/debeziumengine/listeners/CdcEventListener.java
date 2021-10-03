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
        log.info(event.key().toString());
        log.info(event.value().toString());

        // throw new RuntimeException("Ops!!");
    }
}
