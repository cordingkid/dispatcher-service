package com.polarbookshop.dispatcherservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class DispatchingFunctions {
    private static final Logger log = LoggerFactory.getLogger(DispatchingFunctions.class);

    // Functions registered as beans can be found and managed by Spring Cloud Functions.
    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId();
        };
    }

    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            log.info("The order with id {} is labeled.", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }
}