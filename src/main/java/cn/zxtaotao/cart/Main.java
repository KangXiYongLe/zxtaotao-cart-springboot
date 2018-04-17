package cn.zxtaotao.cart;

import org.springframework.boot.SpringApplication;

import cn.zxtaotao.cart.spring.config.ZxTaoTaoCartApplication;

/**
 * SpringBOOT的入口类
 * @author zengkang
 *
 */
public class Main {

    public static void main(String[] args) {
        //读取配置类，和虚拟机参数，运行程序
        SpringApplication.run(ZxTaoTaoCartApplication.class, args);
    }

}
