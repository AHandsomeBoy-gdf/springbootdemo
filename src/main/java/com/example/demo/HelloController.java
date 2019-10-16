package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Random;

@RestController
public class HelloController {


    @Autowired
    private DiscoveryClient client;

    @Autowired
    private Registration registration;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public User index() {
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        for (ServiceInstance instance:
             instances) {
            System.out.println("/hello, host:" + instance.getHost() + ", service_id: " + instance.getServiceId());
        }
        User person = new User();
        person.setName("诺克萨斯");
        person.setAge("10");
        return person;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() throws Exception{
        //Hystrix默认超时时间为2000ms
        //int sleepTime = new Random().nextInt(3000);
        //System.out.println("sleep time is :" + sleepTime);
        //Thread.sleep(sleepTime);
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        for (ServiceInstance instance:
                instances) {
            System.out.println("收到请求/hello, host:" + instance.getHost() + ", service_id: " + instance.getServiceId());
        }
        return "Hello World";
    }
}
