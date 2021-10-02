package it.codingjam.poc.debeziumengine.services;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class CdcEngine {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private DebeziumEngine<ChangeEvent<String, String>> engine;

    @PostConstruct
    void init() {
        var properties = readProperties("postgres.properties");
        engine = DebeziumEngine.create(Json.class)
                .using(properties)
                .using(newConnectorCallback())
                .using((success, message, error) -> {
                    log.info(message);
                    executor.shutdown();
                })
                .notifying(event -> log.info(event.toString()))
                .build();
        executor.execute(engine);
    }

    @PreDestroy
    void beforeShutdown() throws IOException {
        engine.close();
    }

    private DebeziumEngine.ConnectorCallback newConnectorCallback() {
        return new DebeziumEngine.ConnectorCallback() {
            @Override
            public void connectorStarted() {
                DebeziumEngine.ConnectorCallback.super.connectorStarted();
                log.info("connectorStarted");
            }

            @Override
            public void connectorStopped() {
                DebeziumEngine.ConnectorCallback.super.connectorStopped();
                log.info("connectorStopped");
            }

            @Override
            public void taskStarted() {
                DebeziumEngine.ConnectorCallback.super.taskStarted();
                log.info("taskStarted");
            }

            @Override
            public void taskStopped() {
                DebeziumEngine.ConnectorCallback.super.taskStopped();
                log.info("taskStarted");
            }
        };
    }

    private Properties readProperties(String fileName) {
        try (var propFile = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            var properties = new Properties();
            properties.load(propFile);
            return properties;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
