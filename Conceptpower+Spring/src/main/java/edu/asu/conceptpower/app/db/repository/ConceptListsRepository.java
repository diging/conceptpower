package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ConceptList;
@Repository
public interface ConceptListsRepository extends CrudRepository<ConceptList, Integer> {

}
