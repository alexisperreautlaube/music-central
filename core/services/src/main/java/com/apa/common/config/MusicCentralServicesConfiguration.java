package com.apa.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(MusicCentralModelsConfiguration.class)
@ComponentScan(basePackages = "com.apa.common.services")
public class MusicCentralServicesConfiguration {
}
