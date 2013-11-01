package edu.asu.conceptpower.web.wrapper;

import java.util.ArrayList;
import java.util.List;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.users.UsersManager;

public class ConceptEntryWrapperCreator {

	public List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries,
			UsersManager usersManager, ConceptTypesManager typeManager,
			ConceptManager dictManager) {
		List<ConceptEntryWrapper> foundConcepts = new ArrayList<ConceptEntryWrapper>();

		if (entries == null)
			return foundConcepts;

		for (ConceptEntry entry : entries) {
			ConceptEntryWrapper wrapper = new ConceptEntryWrapper(entry);
			if (entry.getTypeId() != null && !entry.getTypeId().isEmpty())
				wrapper.setType(typeManager.getType(entry.getTypeId()));

			if (entry.getCreatorId() != null && !entry.getCreatorId().isEmpty())
				wrapper.setCreator(usersManager.findUser(entry.getCreatorId()));

			// if entry wraps a wordnet concepts, add it here
			List<ConceptEntry> wordnetEntries = new ArrayList<ConceptEntry>();
			String wordnetIds = (entry.getWordnetId() != null ? entry
					.getWordnetId() : "");
			{
				String[] ids = wordnetIds.trim().split(
						Constants.CONCEPT_SEPARATOR);
				if (ids != null) {
					for (String id : ids) {
						if (id != null && !id.trim().isEmpty()) {
							ConceptEntry wordnetEntry = dictManager
									.getWordnetConceptEntry(id);
							if (wordnetEntry != null)
								wordnetEntries.add(wordnetEntry);
						}
					}
					wrapper.setWrappedWordnetEntries(wordnetEntries);

				}
			}

			// build description considering all the wordnet entries wrappe
			StringBuffer sb = new StringBuffer();
			if (wordnetEntries.size() > 0) {
				sb.append("Wordnet description:<br/>");
				for (ConceptEntry wordnetConcept : wordnetEntries) {
					sb.append("<br></br>");
					if (wordnetConcept.getDescription() != null) {
						sb.append("<i>" + wordnetConcept.getWord()
								+ "</i><br/>");
						sb.append(wordnetConcept.getDescription());
					}
				}
			}
			wrapper.setDescription(sb.toString());

			List<ConceptEntry> synonyms = new ArrayList<ConceptEntry>();
			String synonymIds = (entry.getSynonymIds() != null ? entry
					.getSynonymIds() : "");
			if (synonymIds != null) {

				String[] ids = synonymIds.trim().split(
						Constants.SYNONYM_SEPARATOR);
				if (ids != null) {
					for (String id : ids) {
						if (id != null && !id.trim().isEmpty()) {
							ConceptEntry synonym = dictManager
									.getConceptEntry(id);
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
