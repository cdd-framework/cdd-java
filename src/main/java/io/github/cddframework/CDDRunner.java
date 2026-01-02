package io.github.cddframework;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CDDRunner implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String port;

@Override
public void run(String... args) {
    // Example: If we launch the app, we might want to init or run based on an argument
    if (args.length > 0 && args[0].equals("init")) {
        CDD.init();
    } else {
        CDD.run();
    }
}
}