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

    // 1. Exchange do tipo “topic” usado para publicar/consumir mensagens de questão
    @Bean
    public TopicExchange questionExchange() {
        return new TopicExchange("question-exchange");
    }

    // 2. Fila “question-queue” que será vinculada a esse exchange
    @Bean
    public Queue questionQueue() {
        // durable = true para sobreviver a reinícios do broker
        return new Queue("question-queue", true);
    }

    // 3. Faz o binding (fila ← topic exchange) usando padrão de routing key
    // “question.#”
    // Assim, qualquer routingKey que comece com “question.” cairá nesta fila.
    @Bean
    public Binding questionQueueBinding(Queue questionQueue, TopicExchange questionExchange) {
        return BindingBuilder
                .bind(questionQueue)
                .to(questionExchange)
                .with("question.#");
    }

    // 4. Configura o Jackson2JsonMessageConverter para serializar objetos como JSON
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 5. Configura o AsyncRabbitTemplate, injetando o RabbitTemplate já convertido
    // em JSON.
    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate,
            Jackson2JsonMessageConverter converter) {
        rabbitTemplate.setMessageConverter(converter);
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
