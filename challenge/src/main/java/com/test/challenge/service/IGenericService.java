package com.test.challenge.service;

import org.apache.coyote.BadRequestException;

import java.util.List;

public interface IGenericService<T, K> {

    T find(K k) throws BadRequestException;
    T save(T t) throws BadRequestException;
    List<T> findAll() throws BadRequestException;
    T update(T t) throws BadRequestException;
    void delete(K k) throws BadRequestException;

}
