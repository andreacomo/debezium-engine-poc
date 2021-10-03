package it.codingjam.poc.debeziumengine.config;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class CdcEngine {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private DebeziumEngine<ChangeEvent<String, String>> engine;

    private static final int RETRY_SEC_ON_CONSUMER_ERROR = 20;

    public CdcEngine(String configPropertiesName,
                     Consumer<ChangeEvent<String, String>> consumer) {
        var properties = readProperties(configPropertiesName);
        init(consumer, properties);
    }

    private void init(Consumer<ChangeEvent<String, String>> consumer, Properties properties) {
        engine = DebeziumEngine.create(Json.class)
                .using(properties)
                .using(newConnectorCallback())
                .using((success, message, error) -> {
                    if (success) {
                        log.info(message);
                        executor.shutdown();
                    } else {
                        log.error(error.getMessage(), error);
                        retry(consumer, properties);
                    }
                })
                .notifying(consumer)
                .build();

        executor.execute(engine);
    }

    @SneakyThrows
    private void retry(Consumer<ChangeEvent<String, String>> consumer, Properties properties) {
        log.info("Waiting {} seconds and retry...", RETRY_SEC_ON_CONSUMER_ERROR);
        TimeUnit.SECONDS.sleep(RETRY_SEC_ON_CONSUMER_ERROR);
        init(consumer, properties);
    }

    void shutdown() throws IOException {
        engine.close();
    }

    private DebeziumEngine.ConnectorCallback newConnectorCallback() {
        return new DebeziumEngine.ConnectorCallback() {
            @Override
            public void connectorStarted() {
                log.info("Connector Started");
            }

            @Override
            public void connectorStopped() {
                log.info("Connector Stopped");
            }

            @Override
            public void taskStarted() {
                log.info("Task Started");
            }

            @Override
            public void taskStopped() {
                log.info("Task Stopped");
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
