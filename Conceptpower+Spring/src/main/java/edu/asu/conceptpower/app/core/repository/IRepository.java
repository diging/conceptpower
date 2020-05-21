package edu.asu.conceptpower.app.core.repository;

import java.util.List;

public interface IRepository<T>{
    
    T findOne(long id);

    List<T> findAll();

    T create(T entity);

    T update(T entity);

    void delete(T entity);

    void deleteById(long entityId);
}