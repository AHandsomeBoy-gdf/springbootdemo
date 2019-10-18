package com.example.feginconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    IHelloService helloService;


    @RequestMapping(value = "/feign-consumer", method = RequestMethod.GET)
    public String feignConsumer() {
        return helloService.hello();
    }

    @RequestMapping(value = "/feign-consumer2", method = RequestMethod.GET)
    public String feignConsumer2() {
        StringBuilder sb = new StringBuilder();
        sb.append(helloService.hello1("德玛")).append("\n")
                .append(helloService.hello2("亚索", 8)).append("\n")
                .append(helloService.hello3(new User("瞎子", 10))).append("\n");
        return sb.toString();
    }
}
