package edu.asu.conceptpower.app.wrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.core.Constants;
import edu.asu.conceptpower.app.core.ICommentsManager;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.util.IURIHelper;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ReviewRequest;

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
    
    @Autowired
    private ICommentsManager commentsManager; 
    
    /**
     * This method creates wrappers for the concept entries passed as parameter
     * 
     * @param entries
     *            Holds the concept entries to be wrapped
     * @return
     */
    @Override
    public List<ConceptEntryWrapper> createWrappers(ConceptEntry[] entries) throws LuceneException {
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
            String wordnetIds = (entry.getWordnetId() != null ? entry.getWordnetId() : "");
            {
                String[] ids = wordnetIds.trim().split(Constants.CONCEPT_SEPARATOR);
                if (ids != null) {
                    for (String id : ids) {
                        if (id != null && !id.trim().isEmpty()) {
                            ConceptEntry wordnetEntry = conceptManager.getWordnetConceptEntry(id);
                            if (wordnetEntry != null)
                                wordnetEntries.add(wordnetEntry);
                        }
                    }
                    wrapper.setWrappedWordnetEntries(wordnetEntries);

                }
            }
            
          //Fetching Review, for the concept, to display when the user searches the concept
            ReviewRequest reviewRequest = commentsManager.getReview(entry.getId());
            if(reviewRequest!=null) {
                wrapper.setReviewRequest(reviewRequest);
            }

            // build description considering all the wordnet entries wrappe
            StringBuffer sb = new StringBuffer();
            sb.append(entry.getDescription());
            if (wordnetEntries.size() > 0) {
                for (ConceptEntry wordnetConcept : wordnetEntries) {
                    if (wordnetConcept.getDescription() != null && (entry.getDescription() == null
                            || !wordnetConcept.getDescription().trim().equals(entry.getDescription().trim()))) {
                        sb.append("<br/><i>" + wordnetConcept.getWord() + "</i>");
                        sb.append("<br/>" + wordnetConcept.getDescription());
                    }
                }
            }
            wrapper.setDescription(sb.toString());

            List<ConceptEntry> synonyms = new ArrayList<ConceptEntry>();
            String synonymIds = (entry.getSynonymIds() != null ? entry.getSynonymIds() : "");
            if (synonymIds != null) {

                String[] ids = synonymIds.trim().split(Constants.SYNONYM_SEPARATOR);
                if (ids != null) {
                    for (String id : ids) {
                        if (id != null && !id.trim().isEmpty()) {
                            try {
                                ConceptEntry synonym = conceptManager.getConceptEntry(id);
                                if (synonym != null)
                                    synonyms.add(synonym);
                            } catch (IllegalArgumentException ie) {
                                if (wrapper.isError()) {
                                    wrapper.setErrorMsg(
                                            StringEscapeUtils.escapeXml10(wrapper.getErrorMsg() + "," + id));
                                } else {
                                    wrapper.setErrorMsg(
                                            "The following synonym ids do not seem to exist in the database: " + id);
                                    wrapper.setError(true);
                                }
                            }
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
