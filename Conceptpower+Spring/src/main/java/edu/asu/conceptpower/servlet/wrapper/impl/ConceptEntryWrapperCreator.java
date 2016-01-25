package edu.asu.conceptpower.servlet.wrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.Constants;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.IConceptTypeManger;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.users.IUserManager;
import edu.asu.conceptpower.servlet.util.IURIHelper;
import edu.asu.conceptpower.servlet.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.servlet.wrapper.IConceptWrapperCreator;

/**
 * This class provides methods required for creation concept wrappers
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Component
public class ConceptEntryWrapperCreator implements IConceptWrapperCreator {

	@Autowired
	private IConceptManager conceptManager;

	@Autowired
	private IConceptTypeManger typesManager;

	@Autowired
	private IUserManager usersManager;
	
	@Autowired
        private IURIHelper helper;
	
	/**
	 * This method creates wrappers for the concept entries passed as parameter
	 * 
	 * @param entries
	 *            Holds the concept entries to be wrapped
	 * @return
	 */
	@Override
	public List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries)throws LuceneException {
		List<ConceptEntryWrapper> foundConcepts = new ArrayList<ConceptEntryWrapper>();

		if (entries == null)
			return foundConcepts;

		for (ConceptEntry entry : entries) {
		    
		    ConceptEntryWrapper wrapper = new ConceptEntryWrapper(entry);
		    wrapper.setUri(helper.getURI(entry));
			if (entry.getTypeId() != null && !entry.getTypeId().isEmpty())
				wrapper.setType(typesManager.getType(entry.getTypeId()));

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
				for (ConceptEntry wordnetConcept : wordnetEntries) {
					if (wordnetConcept.getDescription() != null) {
						sb.append("<br/><i>" + wordnetConcept.getWord()
								+ "</i>");
						sb.append("<br/>" + wordnetConcept.getDescription());
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
