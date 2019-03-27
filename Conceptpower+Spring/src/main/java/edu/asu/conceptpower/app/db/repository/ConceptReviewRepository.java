package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.ReviewRequest;
@Repository
public interface ConceptReviewRepository extends CrudRepository<ReviewRequest,Integer>{

}
