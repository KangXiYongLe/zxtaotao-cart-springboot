package cn.zxtaotao.cart.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@SpringBootApplication
@PropertySource(value = { "classpath:jdbc.properties", "classpath:env.properties",
        "classpath:httpclient.properties", "classpath:redis.properties" }, ignoreResourceNotFound = true)
@ComponentScan(basePackages="cn.zxtaotao")
@ImportResource("classpath:dubbo/dubbo-consumer.xml")//导入dubbo的配置文件，将dubbo整合spring容器中
@EnableWebMvc//要配置报扫描的话，必须要加上@EnableWebMvc注解，不然不会扫描@Contrller注解
public class ZxTaoTaoCartApplication extends SpringBootServletInitializer{
    
    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClassName;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(ZxTaoTaoCartApplication.class);
    }
    
    @Bean
    public ServletRegistrationBean htmlServlet(){
        //注解扫描上下文
        AnnotationConfigWebApplicationContext applicationContext
                = new AnnotationConfigWebApplicationContext();
        //base package
        applicationContext.scan("cn.zxtaotao");
        
        //通过构造函数指定dispatcherServlet的上下文
        DispatcherServlet rest_dispatcherServlet
                = new DispatcherServlet(applicationContext);

        //用ServletRegistrationBean包装servlet
        ServletRegistrationBean registrationBean
                = new ServletRegistrationBean(rest_dispatcherServlet);
        registrationBean.setLoadOnStartup(1);
        //指定urlmapping
        registrationBean.addUrlMappings("*.html");
        
        /*
         * 这个语句很重要，因为name相同的ServletRegistrationBean只有一个会生效，也就是说，后注册的会覆盖掉name相同的ServletRegistrationBean。
         * 如果不指定，默认为“dispatcherServlet”而spring boot提供的DispatcherServlet的name就是“dispatcherServlet”。
         * 可以在spring boot的DispatcherServletAutoConfiguration类中找到
         */
        //指定name，如果不指定默认为dispatcherServlet
        registrationBean.setName("htmlservlet");
        return registrationBean;
    }
    
    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        return resolver;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
        // 数据库驱动
        boneCPDataSource.setDriverClass(jdbcDriverClassName);
        // 相应驱动的jdbcUrl
        boneCPDataSource.setJdbcUrl(jdbcUrl);
        // 数据库的用户名
        boneCPDataSource.setUsername(jdbcUsername);
        // 数据库的密码
        boneCPDataSource.setPassword(jdbcPassword);
        // 检查数据库连接池中空闲连接的间隔时间，单位是分，默认值：240，如果要取消则设置为0
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(60);
        // 连接池中未使用的链接最大存活时间，单位是分，默认值：60，如果要永远存活设置为0
        boneCPDataSource.setIdleMaxAgeInMinutes(30);
        // 每个分区最大的连接数
        boneCPDataSource.setMaxConnectionsPerPartition(100);
        // 每个分区最小的连接数
        boneCPDataSource.setMinConnectionsPerPartition(5);
        return boneCPDataSource;
}


}
