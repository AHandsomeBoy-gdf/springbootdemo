package com.example.ribbonconsumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
public class PersonServicecImpl implements IPersonService{

    @Autowired
    RestTemplate restTemplate;

    /**
     * 设置批量方法为findAll()，设置时间窗口为100
     * @param id
     * @return
     */
    @Override
    @HystrixCollapser(batchMethod = "findAll", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Person find(Long id) {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{1}", Person.class, id);
    }

    @HystrixCommand
    @Override
    public List<Person> findAll(List<Long> ids) {
        return restTemplate.getForObject("http://HELLO-SERVICE/users?ids={1}", List.class, StringUtils.join(ids, ","));
    }
}
