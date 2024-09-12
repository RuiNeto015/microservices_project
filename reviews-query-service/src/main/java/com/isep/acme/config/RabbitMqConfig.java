package com.isep.acme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqConfig {

    @Value("${rabbitmq.products.fanout-exchange}")
    private String productFanoutName;

    @Value("${rabbitmq.users.fanout-exchange}")
    private String usersFanoutName;

    @Value("${rabbitmq.reviews.fanout-exchange}")
    private String reviewFanoutName;

    @Bean
    public Queue queueProduct() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue queueUser() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue queueReview() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange usersFanoutExchange() {
        return new FanoutExchange(usersFanoutName);
    }

    @Bean
    public FanoutExchange productFanoutExchange() {
        return new FanoutExchange(productFanoutName);
    }

    @Bean
    public FanoutExchange reviewFanoutExchange() {
        return new FanoutExchange(reviewFanoutName);
    }

    @Bean
    public Binding bindingProduct(Queue queueProduct, FanoutExchange productFanoutExchange) {
        return BindingBuilder
                .bind(queueProduct)
                .to(productFanoutExchange);
    }

    @Bean
    public Binding bindingUsers(Queue queueUser, FanoutExchange usersFanoutExchange) {
        return BindingBuilder
                .bind(queueUser)
                .to(usersFanoutExchange);
    }

    @Bean
    public Binding bindingReview(Queue queueReview, FanoutExchange reviewFanoutExchange) {
        return BindingBuilder
                .bind(queueReview)
                .to(reviewFanoutExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
