package edu.asu.conceptpower.app.db.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptEntry;
@Repository
public interface ConceptEntryRepository extends CrudRepository<ConceptEntry, Integer> {

}
