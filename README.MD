## This project for study spring cloud sample

### 2024-12-01

1.Create new project,name is spring-cloud-sd-sample
- update pom.xml
- 定义父工程的各种依赖规范，只定义不引用。

2.Create new moudle,name is cloud-provider-payment8001，payment微服务提供者  
- update pom.xml,引入各种依赖，使用父工程中的pom依赖不需要定义version
- create resources/application.properties file，定义port、log—config、datasource、mybatis、Druid等等
- create 主启动类，关键字@SpringBootApplication
- create 业务类，经典的mvc模式,此处使用controller、entities、service、service.mpl、dao、/resource/mapper/*.xml
- create 基本的数据插入和查询样例  

### 2024-12-02

1.Create new moudle,name is cloud-consumer-order80

- update pom.xml,引入各种依赖，使用父工程中的pom依赖不需要定义version
- create resources/application.properties file，定义port、log—config、datasource、mybatis、Druid等等
- create 主启动类，关键字@SpringBootApplication
- 消费者之一，调用8001微服务接口进行业务处理，只需要一个controller即可。

2.Create new moudle,name is cloud-api-common
- 工程重构，由于8001和80两个微服务都使用到了同一个entities实体，单独创建一个模块用于封装各种实体、工具类等
- mvn clean install之后，在8001和80需要使用的微服务中引入
```xml
<!-- 自定义公共属性(实体类或工具类等) -->
<dependency>
    <groupId>com.cmg.spcl</groupId>
    <artifactId>cloud-api-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2024-12-03

1.使用Eureka进行服务注册,改造8001微服务以支持eureka作为客户端
- 在8001中pom.xml引入相关赖：
```xml
<!-- Eureka client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        <version>2.2.6.RELEASE</version>
        <exclusions>
            <exclusion>
                <groupId>javax.servlet</groupId>
                <artifactId>servlet-api</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
```
- 在application.properties中配置eureka作为client关键信息：
```properties
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone= http://localhost:7001/eureka
```
- 在8001主启动类中声明：@EnableEurekaClient，同理如果希望80微服务也支持eureka也需要如上改造。

2.Create new moudle,name is cloud-eureka-server7001

- 在pom.xml中除基础配置再引入：
```xml
<!-- Eureka server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        <version>2.2.6.RELEASE</version>
        <exclusions>
            <exclusion>
                <groupId>javax.servlet</groupId>
                <artifactId>servlet-api</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
```
- 在application.properties中配置eureka作为server的关键信息：
```properties
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
```
- 启动类声明：@EnableEurekaServer
- 访问 http://localhost:7001 即可看到Eureka的主页，里面包括注册进的8001微服务

### 2024-12-04  
1.Create new moudle,name is cloud-eureka-server7002,cloud-provider-payment8002,以支持集群服务   
2.改造80微服务，增加配置：ApplicationContextConfig类，定义RestTemplate工具并注解@LoadBalanced以支持负载均衡的能力   
3.改造controller中的微服务地址由http://localhost:8001改为http://CLOUD-PAYMENT-SERVICE，其中CLOUD-PAYMENT-SERVICE是8001和8002定义的微服务名称  
4.修改8001|8002中的application.properties
```properties
#单机|集群
eureka.client.service-url.defaultZone= http://localhost:7001/eureka 
eureka.client.service-url.defaultZone=http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```
**集群相关配置：**
```markdown
1.etc/hosts add 127.0.0.1 eureka7001.com and  127.0.0.1 eureka7002.com
2.application.properties update hostname=eureka7001.com
3.service-url.defaultZone update http://eureka7002.com:7002/eureka/
4.8001 or 80 client update application.properties defaultZone update to http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```
5.update application.properties配置支更改主机名并显示IP
```properties
#Eureka config
eureka.instance.instance-id=payment8001
eureka.instance.prefer-ip-address=true
```
6.为8001主启动类加注解@EnableDiscoveryClient，controller定义 EurekaDiscoveryClient 核心功能：
- 服务注册： 将当前服务的元信息（如服务名称、IP、端口、状态等）注册到 Eureka 服务端。  
- 服务发现： 从 Eureka 服务端获取其他注册服务的实例信息。  
- 负载均衡： 提供对注册服务的负载均衡能力（通常结合 Ribbon 或 Spring Cloud LoadBalancer 使用）。  
- 健康检查： 定期向 Eureka 服务端发送心跳，报告当前服务的健康状态。  

### 2024-12-05

1.Eureka自我保护理论知识  
2.关闭eureka的自我保护,在7001的eureka的server微服务application.properties中add:
```properties
#关闭自我保护机制
eureka.server.enable-self-preservation=false
eureka.server.eviction-interval-timer-in-ms=2000
```
8001客户端update:
```properties
#eureka客户端向服务端发送心跳的时间间隔，单位为秒（默认30秒）
eureka.instance.lease-renewal-interval-in-seconds=1
#eureka服务端在收到最后一次心跳后等待时间上限，单位为秒（默认90秒），超时将剔出服务
eureka.instance.lease-expiration-duration-in-seconds=2
```

### 2024-12-06
1.Eureka2.0已停更，考虑使用Zookeeper代替：
- 官网下载Zookeeper，我使用的是win版，注意别下错了，要编译后的：
https://dlcdn.apache.org/zookeeper/zookeeper-3.9.3/apache-zookeeper-3.9.3-bin.tar.gz  
- 解压，修改conf中的zoo_sample.cfg -> zoo.cfg 内容中只需要改动本地数据目录：dataDir=D:/temp/zookeeper
- 启动：bin/目录，先启动zkServer.cmd ,再启动zkCli.cmd ,见到Welcome to ZooKeeper!即启动成功。
2.Create new moudle,name is cloud-provider-payment8004
- 修改pom.xml add:
```xml
    <!-- Zookeeper client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
        <version>2.2.5.RELEASE</version>
    </dependency>
```
- 修改application.properties,add:
```properties
spring.cloud.zookeeper.connect-string=localhost:2181
```
- 启动类，add 注解：@EnableDiscoveryClient
- 启动之后，如果有jar冲突问题，先在pom中排除再引入对应的jar即可，之后在zookeeper启动台测试：
```shell
[zk: localhost:2181(CONNECTED) 3] ls /
[services, zookeeper]
[zk: localhost:2181(CONNECTED) 7] ls /services
[cloud-payment-service8004]
[zk: localhost:2181(CONNECTED) 8] ls /services/cloud-payment-service8004
[9a6c52e1-ddc6-40a6-936e-198038e8f8af]
[zk: localhost:2181(CONNECTED) 9] ls /services/cloud-payment-service8004/9a6c52e1-ddc6-40a6-936e-198038e8f8af
[]
[zk: localhost:2181(CONNECTED) 10] get /services/cloud-payment-service8004/9a6c52e1-ddc6-40a6-936e-198038e8f8af
{"name":"cloud-payment-service8004","id":"9a6c52e1-ddc6-40a6-936e-198038e8f8af","address":"ZHANGGUOZHONG","port":8004,"sslPort":null,"payload":{"@class":"org.springframework.cloud.zookeeper.discovery.ZookeeperInstance","id":"application-1","name":"cloud-payment-service8004","metadata":{"instance_status":"UP"}},"registrationTimeUTC":1733454231753,"serviceType":"DYNAMIC","uriSpec":{"parts":[{"value":"scheme","variable":true},{"value":"://","variable":false},{"value":"address","variable":true},{"value":":","variable":false},{"value":"port","variable":true}]}}

```
即可看到刚刚成功注册进zookeeper的服务cloud-payment-service8004
- zookeeper节点是临时的，停掉微服务8004之后，过一会儿在zookeepe中就消失了，重启启动8004微服务之后，zookeeper会再发现，但是流水号不一样了。
3.Create new moudle,name is cloud-consumerzk-order80.
- 定义使用zookeeper的消费者.

### 2024-12-19
1.OpenFeign的使用,openfeign默认支持Ribbon:Create new moudle,name is cloud-payment-feign-order80   
2.启动类支持feign,add @EnableFeignClients  
3.service调用服务，add @FeignClient(value="CLOUD-PAYMENT-SERVICE"),需要注意，@GetMapping(value = "/scs/payment/searchById"）此处的value地址需要和8001服务的上下文一致。  
4.测试：需要启动7001 eureka服务，启动8001 provider服务，再启动feign的80服务。  
5.openfeign的超时控制，   
6.Hystrix的学习
**理论**  
  - 处理分布式系统所面临的问题，断路器，处理容错，服务容断，防止雪崩。
  - 服务降级（服务器忙，请稍后再试）、熔断（类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电）、限流（秒杀高并发，有序进行）、接近实时的监控。
  - 停更
  **实践**
  - Create new moudle,name is cloud-provider-hystrix-payment8001
  - pom.xml支持
```properties
        <!-- hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
            <version>2.2.7.RELEASE</version>
        </dependency>
```
- jmeter高并发测试，参考：https://jmeter.apache.org/

### 2024-12-20
1.Create new moudle,name is cloud-consumer-feign-hystrix-order80,在高并发情景下，增加80服务，看系统响应情况。   
2.超时，不再等待。出错要有兜底。 
- 超时处理，主启动类增加@EnableCircuitBreaker注解，业务类增加@HystrixCommand注解。即服务端降级配置
```java
    //超时，配置超时不再等待，另寻解决方案。
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000")
    })
```
3.客户端80服务降级配置
- application中增加：
```properties
#openfeign支持hystrix
feign.hystrix.enabled=true
```
- pom.xml中增加hystrix依赖
```xml
        <!-- hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
            <version>2.2.7.RELEASE</version>
        </dependency>
```
- 主启动类增加注解：@EnableHystrix
- 业务类增加：@HystrixCommand注解
```java
    //超时，配置超时不再等待，另寻解决方案。
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000")
    })
```
4.每个业务都有一个降级处理，代码彭胀，不合理，统一全局降级fallback处理。
- 在Controller中定义头注释
```java
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
```
- 定义全局方法
- 在需要默认设置全局降级的方法上加注解：
```java
@HystrixCommand()
```
5.和业务代码混在一起，混乱。
6.服务降级，客户端调用服务端，服务端宕机或关闭了。
- 在80服务，新建一个类实现80的service服务：
```java
@Component
public class PaymentFallbackService implements PaymentHystrixService{
```
- 在80原有的PaymentHystrixService服务中，增加注解使用刚刚的类：fallback = PaymentFallbackService.class
```java
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT",fallback = PaymentFallbackService.class)
```
7.服务熔断：类比保险丝，达到最大访问量后，直接拒绝访问，拉闸限电，然后调用服务降级的方法返回友好提示。
```markdown
熔断机制是应对雪崩效应的一种微服务链路保护机制，当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，
快速返回错误的响应信息。 当检测到该节点微服务调用响应正常后，恢复调用链路。

在Spring cloud框架里，熔断机制通过Hystrix实现，Hystrix会监控微服务间调用的状况，当失败的调用到一定阀值时，缺省5秒内20次调用失败，
熔断机制的注解是：@HystrixCommand
```
- 参考大神论文资料：https://martinfowler.com/bliki/CircuitBreaker.html
- 在service中增加熔断配置：服务降级-进而熔断-恢复调用链路  
```java
    //服务熔断,circuitBreaker.enabled 是否开启断路器，requestVolumeThreshold 请求次数，sleepWindowInMilliseconds 时间窗口期，errorThresholdPercentage 失败率达到多少跳闸
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled",value="true"),
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="60")
    })
```
- 详细参数如下：
```java
commandProperties = {
    // 没宣隔离策峪，THREAD 表示线程池 SEMAPHORE：信号池隔离
    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
    // 当丽离策喀选择信号池膈离的时候，用来没置信号他的大小（最大芹发数）
    @HystrixProperty（name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
    // 配置命令执行的超时时间
    @HystrixProperty（name =^execution.iso1ation.thread. fimeoutinli11iseconds",value = "20"）,
    // 是否启用超时时间
    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
    // 执行超时的时候是否中斷
    @HystrixProperty (name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
    // 执行被取消的时候是否中斷
    @HystrixProperty(name = "execution.isolation.thread.interruptOnCancel", value = "true"),
    // 允许回滑方法执行的最大并发数
    @HystrixProperty（name = "fallback.isolation.semaphore.maxConcurrentRequests"， value = "10"），
    //服务降級是否启用，是否执行回滑函数
    @HystrixProperty（name = "fallback.enabled"， value = "true"），
    // 是否启用断路鬆
    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
    //滚动时阅窗没置。该时间用于断路器判断健康度时需要收集信息的持续时间
    @HystrixProperty（name = "metrics.rollingStats.timeinMilliseconds"，
    // 該属性用来设置滷动时间圆统计指标信息时划分””的数量，断路器在收菜指标信息的时候会根据
    // 發置的时间窗长度拆分成需个“糖”来累计各度量值每个”糖“记录了一段时间内的来葉指标。
    // 比如10 校内拆分成.10.公俩收樂这样，所以 timeinMiLliseconds 必须能被 numBuckets 整驗。否则会地异赏@HystrixProperty（name = "metrics.rollingstats.numBuckets"， value = "10"），
    // 該属性用来没置而命令执行的延迟是否使用百分位数来跟踪和计算。如果没置为fal.se，那名所有的概票統叶部將运回-工。
    @HystrixPropenty（name = "metrics.rollingPercentile.enabled"， value = "false"），// 該属姓開密设置后分位统计的婉动窗口的持续时间，单位为喜砂。
    @HystrixProperty（name = "metrics.rollingPercentile.timeInMiiliseconds"， value = "60000"），
    // 该属性用济没置百分位统计菊动窗口中使用 “ ”的数量。
    @HystrixProperty（name = "metrics.rollingPercentie.numBuckets"， value = "60000"），
    // 該属性用来没置在执行过程中每个“据”中保留的最大执行次数。如果在烧动时间窗内发生彪过该没定值的执行次巍，
    // 就从最初的位置开始重写。例如，将该值没置为100，龄动窗口为10秒，若在10秒内一个“糖“中发生750日次热示//那会談
    //“摘”中只保留最后的100次执行的统计。另外，增加該值的大小将会增加内存量的消耗，并營加蝶序百分位数质露败计舞时间。
    @HystrixProperty（name = "metrics.rollingPercentile.bucketSize"， value = "100"），
    // 該属性用来没置采葉影响断路器状态的健康快照（请求的成功、错误百分比）的间褥等行时间。
    @HystrixProperty（name = "metrics.healthSnapshot.intervalinMi11iseconds"， value = "500"），
    // 是否开后清求緩旁
    @HystrixProperty（name = "requestCache.enabled"， value = "true"），
    // HysteixComond的执行和要件是否打印日志到MstrixRequestLog 安
    @HystrixProperty (name = "requestLog enabled", value = "true"),
}
```
### 2024-12-23
1.Hystrix图形化Dashboard搭建：
- Create new moudle,name is cloud-consumer-hystrix-dashboard9001
- 配置hystrix-dashboard,注意在8001中使用以下代码：
```java
    /**
     *此配置是为了服务监控而配置，与服务容鑽本身无光，springcLoud升級后的坑
     *ServletRegistrationBean因为springboot的默认路經不是"/hystrix.stream"，
     *只要在自己的项目里配置上下面的servlet就可以了
     */
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> hystrixMetricsStreamServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
```
- 在8001的pom.xml中引入：
```xml
<dependency>
    <groupId>com.netflix.hystrix</groupId>
    <artifactId>hystrix-metrics-event-stream</artifactId>
    <version>1.5.18</version>
</dependency> 
```
- 注意在9001中pom.xml引入：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
    <version>2.2.7.RELEASE</version>
</dependency>

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
<version>2.2.7.RELEASE</version>
</dependency>
```
- 主启动类中声明：@EnableHystrixDashboard
>解决hystrix dashboard中报错Unable to connect to Command Metric Stream
- 需要在9001 application.properties中配置：
```properties
hystrix.dashboard.proxy-stream-allow-list=localhost
```
- 访问：http://localhost:9001/hystrix 监控：http://localhost:9001/hystrix 即可。

2.服务网关，Zuul,gateway，主要是gateway

### 2024-12-24

1.SpingCloud Gateway, 参考：https://spring.io/projects/spring-cloud-gateway。
- 路由，断言，过滤。
2.Create new moudle,name is cloud-gateway-gateway9527
- pom.xml引入：
```xml
        <!-- gateway -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
            <version>2.2.5.RELEASE</version>
        </dependency>
```
- 第一种，通过application.yml配置路由规则：
```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment-service-get
          uri: http://localhost:8001
          predicates:
            - Path=/scs/payment/get/**
        - id: payment-service-lb
          uri: http://localhost:8001
          predicates:
            - Path=/scs/payment/lb/**
```
- 第二种，通过代码配置路由的方法
```java
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        //代码配置路由
        routes.route("path_route_payment_get",
                r -> r.path("/news")
                        .uri("https://www.google.com/news")).build();

        return routes.build();
    }
```
3.通过微服务名实现动态路由转发，修改yaml配置文件：
```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能，利用微服务进行路由
      routes:
        - id: payment-service-get
          #uri: http://localhost:8001
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/scs/payment/get/**
        - id: payment-service-lb
          #uri: http://localhost:8001
          uri: lb://cloud-payment-service
          predicates:
            - Path=/scs/payment/lb/**
        - id: payment-service-port
          #uri: http://localhost:8001
          uri: lb://cloud-payment-service
          predicates:
            - Path=/scs/payment/port/**
```
- lb:// 的作用   
  •	lb:// 是 Spring Cloud LoadBalancer 提供的协议标识，用于表示将请求转发给服务注册中心（如 Eureka、Consul）中注册的服务实例。        
  •	cloud-payment-service 是服务在服务注册中心中的名称，Gateway 会通过服务发现机制获取该服务的可用实例，并负载均衡地转发请求。

- 动态路由举例：predicates
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: example-route
          uri: http://localhost:8080
          predicates:
            - Path=/api/**, /user/**
            - After=2024-12-24T10:00:00+08:00[Asia/Shanghai]
            - Header=X-Request-Id, 12345
            - Host=**.example.com
            - Method=GET
```
4.gateway的filter使用:filters    
5.自定义过滤器globalFilter,举例：
```java
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
    log.info("---------------come in here:"+new Date());
    String uname = exchange.getRequest().getQueryParams().getFirst("uname");
    if(StrUtil.isEmpty(uname)){
      log.info("------------username is null,非法用户，不予放行");
      exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
      return exchange.getResponse().setComplete();
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder(){
    return 0;
  }
}
```
### 2024-12-25
1.服务配置和服务总线，SpingCloud Config & SpringCloud Bus，熟悉了解  
2.Create new moudle,name is cloud-config-center-3344
- pom.xml
```xml
        <!-- spring cloud config server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
            <version>2.2.7.RELEASE</version>
        </dependency>
```
- 配置yml
```yml
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          uri: https://github.com/bj-guozhong/springcloud-config-sd.git
          search-path:
            - springcloud-config-sd
      label: main
```
- github上新建配置项目springcloud-config-sd
- 启动，访问：http://localhost:3344/main/config-gateway.yml 即可读取远程上的配置文件
```xhell
git remote add master https://xxx@github.com/bj-guozhong/springcloud-config-sd.git

git push origin main
```
3.客户端3355读取读取配置中心3344
- Create new moudle,name is cloud-config-client-3355
- pom.xml
```xml
        <!-- spring cloud config client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>2.2.7.RELEASE</version>
        </dependency>
```
- 新的yml文件，application.yml是用户级的资源配置文件，bootstrap.yml文件是系统级的，优先级更高。
```yml
  application:
    name: cloud-config-client
  cloud:
    config:
      label: main
      name: config
      profile: dev
      uri: http://localhost:3344
```
- 如果远端配置文件中有端口配置，它会覆盖本地bootstrap.yml中的3355端口。   
4.github中修改了配置内容，3355微服务不会立刻生效，重启3355服务才可以。此时需要解决动态刷新配置的问题。
- pom.xml 
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
- yml
```yml
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
- controller增加注解
```java
@RefreshScope
```
- 运维人员修改配置中心内容后，需要发送post请求主动刷新:,手动刷新（缺点，需要运维人员去手动请求一批微服务）
```xshell
curl -X POST "http://localhost:3355/actuator/refresh"
```

### 2024-12-26
1.消息总线 SpringCloud Bus,分布式自动刷新配置功能，配合SpringCloud Config使用，Bus支持两种消息代理：RabbitMQ和Kafka
2.安装erlang & RbbitMQ,参考：https://313390.xyz/index.php/archives/65/
3.在3344配置微服务中心增加：
- pom配置支持MQ：
```xml
        <!-- RabbitMQ -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```
- 在application.yml 增加：
```yml
  # add RabbitMQ config 
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    
  # rabbitmq config,暴露BUS刷新配置的端点
  management:
    endpoints:
      web:
        exposure:
          include: 'bus-refresh'
```
4.在3355和3366客户端增加MQ支持,并进行全局广播，一次修改3355和3366都会自动刷新配置
- pom.xml add:
```xml
        <!-- RabbitMQ -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```
- 在application.yml增加：
```yml
  # add RabbitMQ config 
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```
- 配置完成后，刷新配置中心：
```bash
curl -X POST "http://localhost:3344/actuator/bus-refresh"
```
5.不想全局广播，定点通知，比如通知3355，不通知3366,参数：{destination}
- 定点通知，其中cloud-config-client为3355应用中yml配置的spring-application-name
```bash
curl -X POST "http://localhost:3344/actuator/bus-refresh/cloud-config-client:3355"
```




本项目：
git push -u master master

Netty




