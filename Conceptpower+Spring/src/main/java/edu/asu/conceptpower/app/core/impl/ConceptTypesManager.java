package edu.asu.conceptpower.app.core.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.core.ConceptType;

@Service
public class ConceptTypesManager implements IConceptTypeManger {

	@Autowired
	private TypeDatabaseClient client;

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptTypeManger#addConceptType(edu.asu.conceptpower.core.ConceptType)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptTypeManger#storeModifiedConceptType(edu.asu.conceptpower.core.ConceptType)
	 */
	@Override
	public void storeModifiedConceptType(ConceptType type) {
		client.update(type);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptTypeManger#getAllTypes()
	 */
	@Override
	public ConceptType[] getAllTypes() {
		return client.getAllTypes();
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptTypeManger#getType(java.lang.String)
	 */
	@Override
	public ConceptType getType(String id) {
		return client.getType(id);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptTypeManger#deleteType(java.lang.String)
	 */
	@Override
	public void deleteType(String id){
		client.deleteType(id);
	}

}
