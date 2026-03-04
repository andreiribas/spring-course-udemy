package com.ribas.andrei.training.spring.udemy.service.config.auto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {
        "com.ribas.andrei.training.spring.udemy.service.impl",
        "com.ribas.andrei.training.spring.udemy.service.mapper"
})
public class ServiceModuleAutoConfiguration {
}
