package com.kbertv.currencyService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    public String topicExchangeName;
    @Value("${rabbitmq.currencyService.queue.call}")
    public String currencyServiceCallQueueName;
    @Value("${rabbitmq.currencyService.queue.response}")
    public String currencyServiceResponseQueueName;
    @Value("${rabbitmq.currencyService.key.call}")
    public String currencyServiceCallRoutingKey;
    @Value("${rabbitmq.currencyService.key.response}")
    public String currencyServiceResponseRoutingKey;

    public static TopicExchange exchange;

    @Autowired
    public void setExchange(@Lazy TopicExchange exchange) {
        RabbitMQConfig.exchange = exchange;
    }

    @Bean
    Queue queue() {
        return new Queue(currencyServiceCallQueueName, false);
    }

    @Bean
    Queue responseQueue() {
        return new Queue(currencyServiceResponseQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(currencyServiceCallRoutingKey);
    }

    @Bean
    Binding responseBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(currencyServiceResponseRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter producerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
