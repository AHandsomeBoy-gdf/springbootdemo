package com.example.ribbonconsumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public String helloService() {
        long start = System.currentTimeMillis();
        String result = restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
        long end = System.currentTimeMillis();
        System.out.println("Speed time is " + (end - start));
        return result;
    }

    public String helloFallback() {
        return "error, fuck off, no place for u!";
    }

    /**
     *  命令-同步调用 返回单个操作结果
     * @param id
     * @return
     */
    @HystrixCommand
    public Person getPersonByIdSync(Long id) {
        return restTemplate.getForObject("http://HELLO-SERVICE/getPersonById", Person.class, id);
    }

    /**
     * 命令-异步调用 返回单个操作结果
     * @param id
     * @return
     */
    @HystrixCommand
    public Future<Person> getPersonByidAsync(Long id) {
        return new AsyncResult<Person>() {
            @Override
            public Person invoke() {
                return restTemplate.getForObject("http://HELLO-SERVICE/getPersonById", Person.class, id);
            }
        };
    }

    /**
     * 返回多个操作结果，EAGER-hot Observable-observe(), LASY-cold Observable-toObservable(),依次设置命令名，命令组名，线程池划分
     * @param id
     * @return
     */
    @CacheResult(cacheKeyMethod = "getUserByIdCacheKey")
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER, commandKey = "getPersonById", groupKey = "PersonGroup", threadPoolKey = "getPersonByIdThread")
    public Observable<Person> getPersonById(@CacheKey Long id) {
        return Observable.unsafeCreate(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    Person person = restTemplate.getForObject("http://HELLO-SERVICE/getPersonById", Person.class, id);
                    subscriber.onNext(person);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public String getUserByIdCacheKey() {
        return "key";
    }
}
