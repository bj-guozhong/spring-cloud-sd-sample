## This project for study spring cloud sample

###2024-12-01

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
- 访问http://localhost:7001 即可看到Eureka的主页，里面包括注册进的8001微服务

###2024-12-04  
1.Create new moudle,name is cloud-eureka-server7002,cloud-provider-payment8002,以支持集群服务   
2.改造80微服务，增加配置：ApplicationContextConfig类，定义RestTemplate工具并注解@LoadBalanced以支持负载均衡的能力
3.改造controller中的微服务地址由http://localhost:8001改为http://CLOUD-PAYMENT-SERVICE，其中CLOUD-PAYMENT-SERVICE是8001和8002定义的微服务名称  
4.修改8001|8002中的application.properties
```properties
#单机|集群
eureka.client.service-url.defaultZone= http://localhost:7001/eureka 
eureka.client.service-url.defaultZone=http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```
集群相关配置：
```markdown
#1.etc/hosts add 127.0.0.1 eureka7001.com and  127.0.0.1 eureka7002.com
#2.application.properties update hostname=eureka7001.com
#3.service-url.defaultZone update http://eureka7002.com:7002/eureka/
#4.8001 or 80 client update application.properties defaultZone update to http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
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

###2024-12-05

1.Eureka自我保护理论知识
2.关闭eureka的自我保护
 