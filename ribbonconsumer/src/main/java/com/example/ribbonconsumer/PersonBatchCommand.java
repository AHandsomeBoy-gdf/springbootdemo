package com.example.ribbonconsumer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

public class PersonBatchCommand extends HystrixCommand<List<Person>> {

    IPersonService personService;
    List<Long> ids;

    protected PersonBatchCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected PersonBatchCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group, threadPool);
    }

    protected PersonBatchCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected PersonBatchCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected PersonBatchCommand(Setter setter) {
        super(setter);
    }

    protected PersonBatchCommand(IPersonService personService, List<Long> ids) {
        super(Setter.withGroupKey(asKey("personServicecImpl")));
        this.personService = personService;
        this.ids = ids;
    }

    @Override
    protected List<Person> run() throws Exception {
        return personService.findAll(ids);
    }
}
