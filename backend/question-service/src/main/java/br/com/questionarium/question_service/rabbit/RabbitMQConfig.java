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
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    // 1. Exchange do tipo “topic”
    @Bean
    public TopicExchange questionExchange() {
        return new TopicExchange("question-exchange");
    }

    // 2. Fila “question-queue”
    @Bean
    public Queue questionQueue() {
        return new Queue("question-queue", true);
    }

    // 3. Binding (fila ← exchange) usando “question.#”
    @Bean
    public Binding questionQueueBinding(Queue questionQueue, TopicExchange questionExchange) {
        return BindingBuilder
                .bind(questionQueue)
                .to(questionExchange)
                .with("question.#");
    }

    // 4. Conversor JSON para RabbitTemplate e listeners
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 5. RabbitTemplate configurado com o conversor JSON
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    // 6. AsyncRabbitTemplate usando o RabbitTemplate JSON
    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
