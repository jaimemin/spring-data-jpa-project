package com.tistory.jaimemin.springdatajpaproject;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataJpaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaProjectApplication.class, args);
    }

    /**
     * OrderSimpleApiController ordersV1처럼 Entity 자체를 반환하는 API를 위한 Bean
     *
     * @return
     */
    @Bean
    Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}
