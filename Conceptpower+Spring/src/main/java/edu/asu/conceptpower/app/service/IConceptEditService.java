package edu.asu.conceptpower.app.service;

import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptEntry;

public interface IConceptEditService {

    /**
     * This method edits the concept by updating the concept in the database and
     * lucene index.
     * 
     * Apart from updating the concept, this method adds and removes the wordnet
     * entries from the lucene index and database.
     * 
     * @param conceptEntry
     *            - Concept entry to be updated in the database and lucene.
     * @param oldWordnetIds
     *            - Wordnet ids that belonged to the concept entry before the
     *            updation. Comma separated string.
     * @param updatedWordnetIds
     *            - Newly added wordnet ids to the concept entry. Comma
     *            separated string.
     * @param userName
     *            - UserName of the user who is updating the concept.
     * @throws IllegalAccessException
     * @throws LuceneException
     * @throws IndexerRunningException
     */
    public void editConcepts(ConceptEntry conceptEntry, String oldWordnetIds, String updatedWordnetIds, String userName)
            throws IllegalAccessException, LuceneException, IndexerRunningException;

}
