package com.apa.common;

import com.apa.ServicesSpringBootTestApplication;
import com.apa.client.volumio.VolumioClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServicesSpringBootTestApplication.class)
public class AbstractCommonIT {

    @MockBean
    protected VolumioClient volumioClient;
}
