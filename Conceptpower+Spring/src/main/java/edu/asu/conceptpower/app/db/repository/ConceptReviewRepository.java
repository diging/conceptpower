package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;

import edu.asu.conceptpower.app.model.ReviewRequest;

public interface ConceptReviewRepository extends CrudRepository<ReviewRequest,Integer>{

}
