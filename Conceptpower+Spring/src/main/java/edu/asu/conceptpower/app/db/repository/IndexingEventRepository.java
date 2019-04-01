package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;

import edu.asu.conceptpower.app.model.IndexingEvent;

public interface IndexingEventRepository extends CrudRepository<IndexingEvent, Integer>{

}
