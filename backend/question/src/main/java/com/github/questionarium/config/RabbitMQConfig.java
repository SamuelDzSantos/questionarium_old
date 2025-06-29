package com.github.questionarium.config;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * Converte objetos Java <-> JSON nas mensagens RabbitMQ.
     */
    @Bean
    MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // AsyncRabbitTemplate usando o RabbitTemplate JSON
    @Bean
    AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setUseTemporaryReplyQueues(true);
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

}
