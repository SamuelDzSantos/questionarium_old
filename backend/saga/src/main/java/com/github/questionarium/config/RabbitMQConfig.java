package com.github.questionarium.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.questionarium.types.events.AuthEvents;
import com.github.questionarium.types.events.EmailEvents;
import com.github.questionarium.types.events.QuestionEvents;
import com.github.questionarium.types.events.UserEvents;

@Configuration
public class RabbitMQConfig {

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate template) {
        template.setUseTemporaryReplyQueues(true);
        return new AsyncRabbitTemplate(template);
    }

    @Bean
    TopicExchange defaultExchange() {
        return new TopicExchange("default-exchange");
    }

    @Bean
    Queue createAuthUserQueue() {
        return new Queue(AuthEvents.CREATE_AUTH_USER_EVENT.toString());
    }

    @Bean
    Queue createRevertCreateAuthUserQueue() {
        return new Queue(AuthEvents.REVERT_CREATE_AUTH_USER_EVENT.toString());
    }

    @Bean
    Queue createGenerateConfirmationTokenQueue() {
        return new Queue(AuthEvents.CREATE_CONFIRMATION_TOKEN_EVENT.toString());
    }

    @Bean
    Queue createUserQueue() {
        return new Queue(UserEvents.CREATE_USER_EVENT.toString());
    }

    @Bean
    Queue sendEmailQueue() {
        return new Queue(EmailEvents.SEND_EMAIL_EVENT.toString());
    }

    @Bean
    Queue getQuestionQueue() {
        return new Queue(QuestionEvents.QUESTION_QUEUE_GET.toString());
    }

    @Bean
    Queue getAlternativeQueue() {
        return new Queue(QuestionEvents.QUESTION_QUEUE_GET_ALTERNATIVE.toString());
    }

    @Bean
    Queue getAswerQueue() {
        return new Queue(QuestionEvents.QUESTION_QUEUE_GET_ASWER_KEY.toString());
    }

}
