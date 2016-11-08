package edu.asu.conceptpower.app.db.objectdb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.db.IDatabaseManager;


//@Component
public class ObjectDbDatabaseManager implements IDatabaseManager {

	@PersistenceContext 
	protected EntityManager manager;
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.db.objectdb.IDatabaseManager#update(java.lang.Object)
	 */
	@Override
	@Transactional
	public void update(Object object) {
		manager.merge(object);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.db.objectdb.IDatabaseManager#store(java.lang.Object)
	 */
	@Override
	@Transactional
	public void store(Object object) {
		manager.persist(object);
		manager.flush();
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.db.objectdb.IDatabaseManager#get(java.lang.String, java.lang.Class)
	 */
	@Override
	@Transactional
	public Object get(String id, Class<?> clazz) {
		Object object = manager.find(clazz, id);
		return object;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.db.objectdb.IDatabaseManager#delete(java.lang.String, java.lang.Class)
	 */
	@Override
	@Transactional
	public boolean delete(String id, Class<?> clazz) {
		Object obj = manager.find(clazz, id);
		manager.remove(obj);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.db.objectdb.IDatabaseManager#getManager()
	 */
	@Override
	public EntityManager getManager() {
		return manager;
	}
}