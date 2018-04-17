package cn.zxtaotao.cart.spring.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ和Spring-Boot整合的配置类
 * @author zengkang
 *
 */
@Configuration
public class RabbitMQSpringConfig {
    
    //注入RabbitMQ的连接工厂
    @Autowired
    private ConnectionFactory connectionFactory;

    // MQ的管理，包括队列、交换器等
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    // 定义队列，持久化队列
    @Bean
    public Queue taotaoCartLoginQueue() {
        // 默认就是自动声明的
        return new Queue("TAOTAO-CART-LOGIN-QUEUE", true);
    }

    // 声明队列，，持久化队列
    @Bean
    public Queue taotaoCartOrderSuccessQueue() {
        // 默认就是自动声明的
        return new Queue("TAOTAO-CART-ORDER-SUCCESS-QUEUE", true);
    }


}
