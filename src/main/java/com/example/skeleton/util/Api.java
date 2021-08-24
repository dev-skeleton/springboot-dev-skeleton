package com.example.skeleton.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class Api {
    @Validated
    static public <E> ResponseEntity<E> SimpleJsonResp(@NotNull Integer status, E entity) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(entity);
    }

    @Validated
    static public String SimpleJsonMsg(@NotNull List<Pair<String, Object>> fields) {
        var msg = fields.stream().reduce(new HashMap<String, Object>(), (m, f) -> {
            m.put(f.getKey(), f.getValue());
            return m;
        }, (a, b) -> null);
        return JSON.toJSONString(msg);
    }

    @Validated
    static public String SimpleStatusMsg(@NotNull Integer code, @NotBlank String msg) {
        return SimpleJsonMsg(List.of(Pair.of("code", code), Pair.of("occurredAt", LocalDateTime.now()), Pair.of("msg", msg)));
    }

    @Validated
    static public ResponseEntity<?> SimpleJsonResp(@NotNull HttpStatus status, @NotNull List<Pair<String, Object>> fields) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(fields.stream().reduce(new HashMap<String, Object>(), (m, f) -> {
            m.put(f.getKey(), f.getValue());
            return m;
        }, (a, b) -> null));
    }

    @Validated
    static public <E> ResponseEntity<E> SimpleJsonResp(@NotNull HttpStatus status, @NotNull E entity) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(entity);
    }
}
