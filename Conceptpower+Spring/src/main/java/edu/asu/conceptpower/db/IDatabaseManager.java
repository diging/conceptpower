package edu.asu.conceptpower.db;

import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

public interface IDatabaseManager {

	public abstract void update(Object object);

	public abstract void store(Object object);

	public abstract Object get(String id, Class<?> clazz);

	public abstract boolean delete(String id, Class<?> clazz);

	public abstract EntityManager getManager();

}