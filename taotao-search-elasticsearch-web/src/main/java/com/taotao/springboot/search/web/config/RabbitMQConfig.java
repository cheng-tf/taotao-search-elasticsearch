package com.taotao.springboot.search.web.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Title: RabbitMQConfig</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-06-09 20:42</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    // ---------------------------------Fanout 交换器-------------------------------------
    // 当消息到达时，Fanout交换器会将消息投递给所有附加到本身的队列
    @Bean
    public Queue searchMessage() {
        return new Queue("item-add.search");
    }

    @Bean
    FanoutExchange itemAddExchange() {
        return new FanoutExchange("item-add");
    }

    @Bean
    Binding bindingExchangeC(Queue searchMessage, FanoutExchange itemAddExchange) {
        return BindingBuilder.bind(searchMessage).to(itemAddExchange);
    }

}