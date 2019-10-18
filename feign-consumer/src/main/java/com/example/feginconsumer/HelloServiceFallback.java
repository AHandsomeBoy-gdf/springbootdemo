package com.example.feginconsumer;

/**
 *  Fegin配置服务降级的方法
 */
public class HelloServiceFallback implements IHelloService {

    @Override
    public String hello() {
        return "error";
    }

    @Override
    public String hello1(String name) {
        return "error";
    }

    @Override
    public User hello2(String name, Integer age) {
        return new User("unknown", 0);
    }

    @Override
    public String hello3(User user) {
        return "error";
    }
}
