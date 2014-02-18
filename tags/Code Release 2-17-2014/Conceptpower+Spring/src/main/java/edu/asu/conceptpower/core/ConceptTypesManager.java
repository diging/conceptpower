package edu.asu.conceptpower.core;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.asu.conceptpower.db4o.TypeDatabaseClient;

@Controller
public class ConceptTypesManager {

	@Autowired
	private TypeDatabaseClient client;

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

}
