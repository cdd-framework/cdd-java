package io.github.cddframework;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CDDRunner implements CommandLineRunner {

    private final CDDEngine cddEngine;

    @Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) {
        String selfTarget = "http://localhost:" + port;
        cddEngine.executeAudit(selfTarget);
    }
}