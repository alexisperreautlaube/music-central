package com.apa.consumer;

import com.apa.importer.ImporterSpringBootTestApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ImporterSpringBootTestApplication.class)
public class AbstractConsumerIT {

}
