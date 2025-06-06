package br.com.questionarium.email_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user-exchange";
    public static final String EMAIL_QUEUE = "emailQueue";
    public static final String USER_CREATED = "user.created";
    public static final String USER_EMAIL_ROUTING = "user.email";

    /**
     * Converte objetos Java <-> JSON nas mensagens RabbitMQ.
     */
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Garante que o listener use o MessageConverter JSON.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    /**
     * Declara a fila que receberá eventos de e-mail.
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    /**
     * Como o User Service criou "user-exchange" como exchange do tipo TOPIC,
     * declaramos aqui um TopicExchange com o mesmo nome.
     */
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    /**
     * Faz o binding da fila "emailQueue" ao exchange "user-exchange" usando a
     * routing key "user.email".
     */
    @Bean
    public Binding emailQueueBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(USER_EMAIL_ROUTING);
    }
}
