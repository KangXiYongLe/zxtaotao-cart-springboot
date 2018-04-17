package cn.zxtaotao.cart.spring.config.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import cn.zxtaotao.common.httpclient.IdleConnectionEvictor;
/**
 * httpclient-spring的整合的配置类
 * @author zengkang
 *
 */
@Configuration
@PropertySource(value = "classpath:httpclient.properties")
public class HttpclientSpringConfig {

    @Value("${http.maxTotal}")
    private Integer httpMaxTotal;

    @Value("${http.defaultMaxPerRoute}")
    private Integer httpDefaultMaxPerRoute;

    @Value("${http.connectTimeout}")
    private Integer httpConnectTimeout;

    @Value("${http.connectionRequestTimeout}")
    private Integer httpConnectionRequestTimeout;

    @Value("${http.socketTimeout}")
    private Integer httpSocketTimeout;

    @Value("${http.staleConnectionCheckEnabled}")
    private Boolean httpStaleConnectionCheckEnabled;

    @Autowired
    private PoolingHttpClientConnectionManager manager;

    /**
     * 定义连接管理器，读取属性文件，创建连接池
     * @return
     */
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(httpMaxTotal);
        // 每个主机的最大并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpDefaultMaxPerRoute);
        return poolingHttpClientConnectionManager;
    }

    /**
     * 定期关闭无效连接，创建用于定期关闭无效连接的线程对象，通过构造方法注入连接管理对象
     * @return
     */
    @Bean
    public IdleConnectionEvictor idleConnectionEvictor() {
        return new IdleConnectionEvictor(manager);
    }

    /**
     * 定义Httpclient对象，通过工厂方法创建CloseableHttpClient对象
     * 该对象是多例的
     * @return
     */
    @Bean
    @Scope("prototype")
    public CloseableHttpClient closeableHttpClient() {
        return HttpClients.custom().setConnectionManager(this.manager).build();
    }

    /**
     * 定义请求参数对象，通过工厂方法创建RequestConfig对象 
     * @return
     */
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom().setConnectTimeout(httpConnectTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(httpConnectionRequestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(httpSocketTimeout) // 数据传输的最长时间
                .setStaleConnectionCheckEnabled(httpStaleConnectionCheckEnabled) // 提交请求前测试连接是否可用
                .build();
    }
}
