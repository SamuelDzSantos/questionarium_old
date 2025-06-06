package br.com.questionarium.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user-exchange";
    public static final String AUTH_QUEUE = "auth-queue";
    public static final String USER_CREATED_ROUTING = "user.created";

    // 1) Declara a fila "auth-queue"
    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE, true);
    }

    // 2) Declara um exchange tipo Topic chamado "user-exchange"
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    // 3) Faz o binding entre a fila e o exchange usando a routing key
    // "user.created"
    @Bean
    public Binding bindingAuthQueue(Queue authQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(authQueue)
                .to(userExchange)
                .with(USER_CREATED_ROUTING);
    }

    // 4) Mantém o MessageConverter JSON que você já tinha
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
