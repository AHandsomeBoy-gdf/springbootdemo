package com.example.feginconsumer;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FullLogConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        //NONE，无记录（DEFAULT）
        //BASIC，仅记录请求方法和URL以及响应状态代码和执行时间
        //HEADERS，记录BASIC以及请求和响应标头
        //FULL，记录所有请求和响应的明细，包括头信息、请求体、元数据等
        return Logger.Level.FULL;
    }
}
