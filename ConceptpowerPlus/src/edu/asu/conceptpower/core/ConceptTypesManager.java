package edu.asu.conceptpower.core;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.db4o.ObjectContainer;

public class ConceptTypesManager {

	private TypeDatabaseClient client;
	
	public ConceptTypesManager(ObjectContainer typeDB) throws IOException {
		this.client = new TypeDatabaseClient(typeDB);
	}
	
	public void addConceptType(ConceptType type) {
		
		String id = null;
		while (true) {
			id = UUID.randomUUID().toString();
			ConceptType exist = client.getType(id);
			if (exist == null)
				break;
		}
		
		type.setTypeId(id);
		
		client.addType(type);
	}
	
	public void storeModifiedConceptType(ConceptType type) {
		client.update(type);
	}
	
	public ConceptType[] getAllTypes() {
		return client.getAllTypes();
	}
	
	public ConceptType getType(String id) {
		return client.getType(id);
	}
	
	public void close() {
		client.close();
	}
}
