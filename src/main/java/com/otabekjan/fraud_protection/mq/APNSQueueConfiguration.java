package com.otabekjan.fraud_protection.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APNSQueueConfiguration {

    @Value("${spring.rabbitmq.template.apns-queue}")
    private String queueName;
    @Value("${spring.rabbitmq.template.apns.routing-key}")
    private String routingKey;


    @Bean
    Queue emailQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding emailBinding(DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue())
                .to(exchange).with(routingKey);
    }
}