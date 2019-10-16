package com.example.ribbonconsumer;

import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.web.client.RestTemplate;
import rx.Observable;


public class PersonObservableCommand extends HystrixObservableCommand<Person> {

    private RestTemplate restTemplate;
    private Long id;

    protected PersonObservableCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected Observable<Person> construct() {
        return Observable.unsafeCreate(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    Person person = restTemplate.getForObject("http://HELLO-SERVICE/getPersonById", Person.class);
                    subscriber.onNext(person);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }
}
