# zxtaotao-cart-springboot
分布式商城系统的购物车服务；
服务主要使用Spring-Boot搭建；
作为DUBBO服务的消费者，注册到Zookeeper,调用在Zookeeper注册的userQueryService服务，通过传入Cookie中的token，到Redis缓存数据库中查询用户是否存在。
通过实现Spring-Web的HandlerInterceptor接口，实现对用户是否登陆的拦截判断。
该项目核心功能：
1，向购物车中添加商品
2，查询购物车商品列表
3，修改商品的购买数量
4，删除购物车中的商品
以上业务功能分两种情况:
1，如果用户已登陆，将直接通过MyBatis操作数据库中的购物车表。
2，如果用户未登录，所有对购物车的商品数据的操作，都将在Cookie中进行。
对于商品的的查询，是通过HttpClient，远程调用商品服务获得数据。
