package com.example.ribbonconsumer;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.netflix.hystrix.HystrixCollapserKey.Factory.asKey;

/**
 * 在时间窗内合并请求的命令类
 */
public class PersonCollapseCommand extends HystrixCollapser<List<Person>, Person, Long> {

    IPersonService personService;
    Long id;

    //withTimerDelayInMilliseconds设置时间窗为100s
    public PersonCollapseCommand(IPersonService personService, Long id) {
        super(Setter.withCollapserKey(asKey("personCollapseCommand"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.personService = personService;
        this.id = id;
    }


    /**
     * 单个请求参数
     * @return
     */
    @Override
    public Long getRequestArgument() {
        return id;
    }

    /**
     * 收集时间窗口内的所有单个请求
     * @param collapsedRequests
     * @return
     */
    @Override
    protected HystrixCommand<List<Person>> createCommand(Collection<CollapsedRequest<Person, Long>> collapsedRequests) {
        List<Long> ids = new ArrayList<>(collapsedRequests.size());
        ids.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        return new PersonBatchCommand(personService, ids);
    }

    /**
     * 在批量请求命令后执行，batchResponse保存了所有结果
     * @param people
     * @param collapsedRequests
     */
    @Override
    protected void mapResponseToRequests(List<Person> batchResponse, Collection<CollapsedRequest<Person, Long>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<Person, Long> collapsedRequest: collapsedRequests) {
            Person person = batchResponse.get(count++);
            collapsedRequest.setResponse(person);//为每个合并前的单个请求设置返回结果
        }
    }
}
