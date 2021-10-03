package it.codingjam.poc.debeziumengine.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.engine.ChangeEvent;
import lombok.SneakyThrows;

import java.util.Map;

public class CdcEvent {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ChangeEvent<String, String> changeEvent;

    private Map<String, Object> key;

    private Map<String, Object> value;

    public CdcEvent(ChangeEvent<String, String> changeEvent) {
        this.changeEvent = changeEvent;
    }

    @Override
    public String toString() {
        return changeEvent.toString();
    }

    @SneakyThrows
    public Map<String, Object> key() {
        if (key == null) {
            key = OBJECT_MAPPER.readValue(changeEvent.key(), Map.class);
        }
        return key;
    }

    @SneakyThrows
    public Map<String, Object> value() {
        if (value == null) {
            value = OBJECT_MAPPER.readValue(changeEvent.value(), Map.class);
        }
        return value;
    }

    public String destination() {
        return changeEvent.destination();
    }
}
