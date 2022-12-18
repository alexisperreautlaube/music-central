package com.apa;

import com.apa.server.config.McConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(McConfiguration.class)
public class McSpringBootApplication {
    public static void main(String ... args) {
        SpringApplication.run(McSpringBootApplication.class, args);
    }
}
