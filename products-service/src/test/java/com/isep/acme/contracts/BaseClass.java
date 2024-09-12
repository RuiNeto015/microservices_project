package com.isep.acme.contracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isep.acme.ACMEApplication;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateProductDTO;
import com.isep.acme.applicationServices.ProductService;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.products.CreatedProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {BaseClass.TestConfig.class, ACMEApplication.class})
@Testcontainers
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
@Slf4j
public abstract class BaseClass {

    @Container
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer();

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQ::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", rabbitMQ::getAmqpPort);
    }

    @Autowired
    ProductService productService;

    public void createProduct() {
        try {
            this.productService.create(new CreateProductDTO("123456789", "desig", "desc"));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        RabbitMessageVerifier rabbitTemplateMessageVerifier() {
            return new RabbitMessageVerifier();
        }

        @Bean
        public Queue queueProduct() {
            return new AnonymousQueue();
        }

        @Bean
        public FanoutExchange productFanoutExchange() {
            return new FanoutExchange("acme.product.fanout");
        }

        @Bean
        public Binding bindingProduct(Queue queueProduct, FanoutExchange productFanoutExchange) {
            return BindingBuilder
                    .bind(queueProduct)
                    .to(productFanoutExchange);
        }

        @Bean
        public MessageConverter messageConverter() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            return new Jackson2JsonMessageConverter(objectMapper);
        }

    }

    static class RabbitMessageVerifier implements MessageVerifierReceiver<Message<?>> {

        private static final Log LOG = LogFactory.getLog(RabbitMessageVerifier.class);

        Map<String, BlockingQueue<Message<?>>> broker = new ConcurrentHashMap<>();

        @Override
        public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
            broker.putIfAbsent(destination, new ArrayBlockingQueue<>(1));
            BlockingQueue<Message<?>> messageQueue = broker.get(destination);
            Message<?> message;
            try {
                message = messageQueue.poll(timeout, timeUnit);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (message != null) {
                LOG.info("Removed a message from a topic/exchange [" + destination + "]");
            }
            return message;
        }

        @Override
        public Message receive(String destination, YamlContract contract) {
            return receive(destination, 15, TimeUnit.SECONDS, contract);
        }


        @RabbitListener(queues = "#{queueProduct.name}")
        private void listen(EventMessage productMessage) {
            String eventType = productMessage.getTopic();
            log.info(productMessage.toString());

            CreatedProductEvent createdProductEvent = (CreatedProductEvent) productMessage;
            Map<String, Object> headers = new HashMap<>();

            broker.putIfAbsent("product.created", new ArrayBlockingQueue<>(1));
            BlockingQueue<Message<?>> messageQueue = broker.get("product.created");
            messageQueue.add(MessageBuilder.createMessage(createdProductEvent, new MessageHeaders(headers)));
        }

    }
}
