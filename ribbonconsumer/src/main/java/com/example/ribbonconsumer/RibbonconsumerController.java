package com.example.ribbonconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class RibbonconsumerController{

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public Person helloConsumer() {
        //return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
        //只是校验其属性是否相同，不校验类名
        ResponseEntity<Person> entity = restTemplate.getForEntity("http://HELLO-SERVICE/hello", Person.class);
        Person user = entity.getBody();
        //Person user1 = restTemplate.getForObject("http://HELLO-SERVICE/hello", Person.class);
        System.out.println("person:" + user.getName() + "--" + user.getAge());
        return user;
    }
}
