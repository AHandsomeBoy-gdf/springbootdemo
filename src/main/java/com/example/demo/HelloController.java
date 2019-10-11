package com.example.demo;


import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class HelloController {


    @Autowired
    private DiscoveryClient client;



    @Autowired
    private Registration registration;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        for (ServiceInstance instance:
             instances) {
            System.out.println("/hello, host:" + instance.getHost() + ", service_id: " + instance.getServiceId());
        }
        return "Hello world";
    }
}
