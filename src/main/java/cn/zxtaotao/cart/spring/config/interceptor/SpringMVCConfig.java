package cn.zxtaotao.cart.spring.config.interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.zxtaotao.cart.interceptors.UserLoginHandlerInterceptor;


/**
 * SpringMVC的SpringBoot配置类
 * @author zengkang
 *
 */
@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserLoginHandlerInterceptor userLoginHandlerInterceptor;
    /**
     * 添加一个自定义的登陆拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 判断用户是否登录的拦截器
        registry.addInterceptor(userLoginHandlerInterceptor).addPathPatterns("/cart/**");
    }

}
