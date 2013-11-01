package edu.asu.conceptpower.core;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import edu.asu.conceptpower.db4o.DBNames;

public class TypeDatabaseClient {

	private ObjectContainer client;
	
	public TypeDatabaseClient(ObjectContainer container) {
		this.client = container;
	}
	
	public ConceptType getType(String id) {
		ConceptType user = new ConceptType(id);
		ObjectSet<ConceptType> results = client.queryByExample(user);
		
		// there should only be exactly one object with this id
		if (results.size() ==  1)
			return results.get(0);	
		
		return null;
	}
	
	public ConceptType findType(String name) {
		ConceptType user = new ConceptType();
		user.setTypeName(name);
		
		ObjectSet<ConceptType> results = client.queryByExample(user);
		// there should only be exactly one object with this id
		if (results.size() >=  1)
			return results.get(0);	
		
		return null;
	}
	
	public ConceptType[] getAllTypes() {
		ObjectSet<ConceptType> results = client.query(ConceptType.class);
		return results.toArray(new ConceptType[results.size()]);		
	}
	
	public ConceptType addType(ConceptType user) {
		client.store(user);
		client.commit();
		return user;
	}
	
	public void deleteType(String id) {
		ConceptType user = new ConceptType();
		user.setTypeId(id);
		
		ObjectSet<ConceptType> results = client.queryByExample(user);
		for (ConceptType res : results) {
			client.delete(res);
			client.commit();
		}
	}
	
	public void update(ConceptType type) {
		ConceptType toBeUpdated = getType(type.getTypeId());
		toBeUpdated.setCreatorId(type.getCreatorId());
		toBeUpdated.setDescription(type.getDescription());
		toBeUpdated.setMatches(type.getMatches());
		toBeUpdated.setModified(type.getModified());
		toBeUpdated.setSupertypeId(type.getSupertypeId());
		toBeUpdated.setTypeId(type.getTypeId());
		toBeUpdated.setTypeName(type.getTypeName());
		client.store(toBeUpdated);
		client.commit();			
	}
	
	public void close() {
		client.close();
	}
}
