package com.apa.consumer;

import com.apa.importer.ImporterSpringBootTestApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testcontainers.junit.jupiter.Testcontainers;


@EnableWebMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ImporterSpringBootTestApplication.class)
public class AbstractConsumerIT {

}
