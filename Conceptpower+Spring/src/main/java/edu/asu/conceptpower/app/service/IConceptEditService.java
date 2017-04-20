package edu.asu.conceptpower.app.service;

import edu.asu.conceptpower.app.bean.ConceptEditBean;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.core.ConceptEntry;

public interface IConceptEditService {

    /**
     * This method edits the concept by updating the concept in the database and
     * lucene index.
     * 
     * Apart from updating the concept, this method adds and removes the wordnet
     * entries from the lucene index and database.
     * 
     * @param conceptEntry
     * @param conceptEditBean
     * @param userName
     * @throws IllegalAccessException
     * @throws LuceneException
     * @throws IndexerRunningException
     */
    public void editConcepts(ConceptEntry conceptEntry, ConceptEditBean conceptEditBean, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException;

}
