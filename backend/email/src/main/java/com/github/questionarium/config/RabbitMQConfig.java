package com.github.questionarium.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user-exchange";

    public static final String USER_CREATED = "user.created";
    public static final String USER_EMAIL_ROUTING = "user.email";

    /**
     * Converte objetos Java <-> JSON nas mensagens RabbitMQ.
     */
    @Bean
    MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Garante que o listener use o MessageConverter JSON.
     */
    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter);
        return factory;
    }

}
