package edu.asu.conceptpower.web.wrapper;

import java.util.ArrayList;
import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.users.UsersManager;

public class ConceptEntryWrapperCreator {

	public List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries, UsersManager usersManager, ConceptTypesManager typeManager, ConceptManager dictManager) {
		List<ConceptEntryWrapper> foundConcepts = new ArrayList<ConceptEntryWrapper>();
		
		if (entries == null)
			return foundConcepts;
		
		for (ConceptEntry entry : entries) {
			ConceptEntryWrapper wrapper = new ConceptEntryWrapper(entry);
			if (entry.getTypeId() != null && !entry.getTypeId().isEmpty())
				wrapper.setType(typeManager.getType(entry.getTypeId()));
			
			if (entry.getCreatorId() != null && !entry.getCreatorId().isEmpty())
				wrapper.setCreator(usersManager.findUser(entry.getCreatorId()));
			
			// if entry wraps a wordnet concept, add it here
			if (entry.getWordnetId() != null && !entry.getWordnetId().trim().isEmpty()) {
				if (!entry.getWordnetId().equals(entry.getId())) {
					ConceptEntry wordnetEntry = dictManager.getWordnetConceptEntry(entry.getWordnetId());
					wrapper.setWrappedWordnetEntry(wordnetEntry);
					
					//wrapper.setDescription(wrapper.getEntry().getDescription() + "\n\nWordnet description: \n" + wordnetEntry.getDescription());
				}
			}
			
			List<ConceptEntry> synonyms = new ArrayList<ConceptEntry>();
			String synonymIds = (entry.getSynonymIds() != null ? entry.getSynonymIds() : ""); // + (wrapper.getWrappedWordnetEntry() != null ? Constants.SYNONYM_SEPARATOR + wrapper.getWrappedWordnetEntry().getSynonymIds() : "");
			if (synonymIds != null) {
				String[] ids = synonymIds.trim().split(Constants.SYNONYM_SEPARATOR);
				if (ids != null) {
					for (String id : ids) {
						if (id != null && !id.trim().isEmpty()) {
							ConceptEntry synonym = dictManager.getConceptEntry(id);
							if (synonym != null)
								synonyms.add(synonym);
						}
					}
					wrapper.setSynonyms(synonyms);
				}
			}
			foundConcepts.add(wrapper);
		}
		
		return foundConcepts;
	}
}
