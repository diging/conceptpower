package edu.asu.conceptpower.app.db;

import javax.persistence.EntityManager;

public interface IDatabaseManager {

	public abstract void update(Object object);

	public abstract void store(Object object);

	public abstract Object get(String id, Class<?> clazz);

	public abstract boolean delete(String id, Class<?> clazz);

	public abstract EntityManager getManager();

}