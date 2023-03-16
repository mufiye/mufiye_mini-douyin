package org.example.mufiye.base;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    /**
     * 1. 交换机
     * 2. 队列
     * 3. 将二者进行绑定
     */
    public static final String EXCHANGE_MSG = "exchange_msg";

    public static final String QUEUE_SYS_MSG = "queue_sys_msg";

    @Bean(EXCHANGE_MSG)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_MSG)
                              .durable(true)
                              .build();
    }

    @Bean(QUEUE_SYS_MSG)
    public Queue queue() {
        return new Queue(QUEUE_SYS_MSG);
    }

    @Bean
    public Binding binding(@Qualifier(EXCHANGE_MSG) Exchange exchange,
                           @Qualifier(QUEUE_SYS_MSG) Queue queue) {
        return BindingBuilder.bind(queue)
                             .to(exchange)
                             .with("sys.msg.*")  // 定义路由规则
                             .noargs();
    }
}
