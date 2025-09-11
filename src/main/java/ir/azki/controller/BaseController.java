package ir.azki.controller;

import org.springframework.http.ResponseEntity;

public class BaseController {
    public ResponseEntity<Void> success() {
        return ResponseEntity.ok().build();
    }

    public <T> ResponseEntity<T> success(T body) {
        return ResponseEntity.ok(body);
    }

}