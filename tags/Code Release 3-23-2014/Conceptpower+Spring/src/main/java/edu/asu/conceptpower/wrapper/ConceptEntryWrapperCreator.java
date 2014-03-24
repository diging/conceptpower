package edu.asu.conceptpower.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.users.impl.UsersManager;
import edu.asu.conceptpower.web.UserListController;

@Component
public class ConceptEntryWrapperCreator {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private UserListController userListConctoller;

	@Autowired
	private TypeDatabaseClient typeDatabaseClient;

	@Autowired
	private UsersManager usersManager;

	public List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries) {
		List<ConceptEntryWrapper> foundConcepts = new ArrayList<ConceptEntryWrapper>();

		if (entries == null)
			return foundConcepts;

		for (ConceptEntry entry : entries) {
			ConceptEntryWrapper wrapper = new ConceptEntryWrapper(entry);
			if (entry.getTypeId() != null && !entry.getTypeId().isEmpty())
				wrapper.setType(typeDatabaseClient.getType(entry.getTypeId()));

			if (entry.getCreatorId() != null && !entry.getCreatorId().isEmpty())
				wrapper.setCreator(usersManager.findUser(entry
						.getCreatorId()));

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
							ConceptEntry wordnetEntry = conceptManager
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
							ConceptEntry synonym = conceptManager
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
