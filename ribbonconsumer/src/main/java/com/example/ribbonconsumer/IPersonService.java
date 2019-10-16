package com.example.ribbonconsumer;

import java.util.List;

public interface IPersonService {
    Person find(Long id);
    List<Person> findAll(List<Long> ids);
}
