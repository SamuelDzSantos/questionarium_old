package br.com.questionarium.question_service.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    TopicExchange defaultExchange() {
        return new TopicExchange("default-exchange");
    }

    @Bean
    Queue questionQueue() {
        return new Queue("question-queue", true);
    }

    @Bean
    AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate template) {
        template.setMessageConverter(jackson2JsonMessageConverter());
        return new AsyncRabbitTemplate(template);
    }

    @Bean
    Binding questionQueueBinding() {
        return BindingBuilder.bind(questionQueue())
                .to(defaultExchange())
                .with("question.#");
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
