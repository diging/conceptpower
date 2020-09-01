package edu.asu.conceptpower.app.core.service;

import java.util.List;

/**
 * 
 * @author Keerthivasan
 * 
 */
public interface IService<T> {
    
    public abstract void create(final T entity);
    
    public abstract T findOne(final long id);

    public abstract List<T> findAll();
}