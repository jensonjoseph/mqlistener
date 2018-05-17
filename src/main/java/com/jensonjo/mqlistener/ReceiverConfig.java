package com.jensonjo.mqlistener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by jensonkakkattil on May, 2018
 */
@Configuration
public class ReceiverConfig {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("CustomRabbitConnectionFactory")
    private ConnectionFactory customRabbitConnectionFactory;

    static final String topicExchangeName = "spring-boot-exchange";

    static final String key = "spring-boot-key";

    static final String queueName = "spring-boot";

    @Bean
    SimpleMessageListenerContainer container(@Qualifier("CustomRabbitConnectionFactory") ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin ra= new RabbitAdmin(customRabbitConnectionFactory);
        ra.afterPropertiesSet();
        ra.setAutoStartup(true);
        return ra;
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        //return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, topicExchangeName, key, null);
        binding.setAdminsThatShouldDeclare(rabbitAdmin());
        return binding;
    }

    @Bean(name = "CustomRabbitConnectionFactory")
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                environment.getProperty("rabbitmq.host"),
                environment.getProperty("rabbitmq.port", Integer.class)
        );
        factory.setUsername(environment.getProperty("rabbitmq.username"));
        factory.setPassword(environment.getProperty("rabbitmq.password"));
        return factory;
    }
}
