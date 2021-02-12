package org.progmatic.webshop.dto;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DozerFactory {

    @Bean
    public DozerBeanMapper getDozer() {
        return new DozerBeanMapper();
    }
}
