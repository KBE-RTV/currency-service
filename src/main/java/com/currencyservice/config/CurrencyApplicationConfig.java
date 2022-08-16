package com.currencyservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CurrencyApplicationConfig {

    public final String TOPIC_EXCHANGE_NAME = "kbe_topic_exchange";
    public static final String CURRENCY_SERVICE_CALL_QUEUE_NAME = "currency_call_queue";
    public static final String CURRENCY_SERVICE_CALL_ROUTING_KEY = "currencyService.call";
    public static final String CURRENCY_SERVICE_RESPONSE_ROUTING_KEY = "currencyService.response";
    public static TopicExchange exchange;

    @Autowired
    public void setExchange(@Lazy TopicExchange exchange) {
        CurrencyApplicationConfig.exchange = exchange;
    }

    @Bean
    Queue queue() {
        return new Queue(CURRENCY_SERVICE_CALL_QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CURRENCY_SERVICE_CALL_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter producerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
