package dev.questionarium.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    Queue queue() {
        return new Queue("emailQueue");
    }

    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

}
