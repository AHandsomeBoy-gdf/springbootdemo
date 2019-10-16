package com.example.ribbonconsumer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;


public class PersonCommand extends HystrixCommand<Person> {

    private RestTemplate restTemplate;
    private Long id;

    //默认使用类名作为默认的命令名称
    public PersonCommand() {
        //设置命令组名，然后才能通过组名设置命令名
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GroupName"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("CommandName")));
    }

    protected PersonCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected Person run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{1}", Person.class, id);
    }

    @Override
    protected Person getFallback() {
        return super.getFallback();
    }

    //开启请求缓存
    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    //刷新缓存，根据id进行清理
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("CommandName"),
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }
}
