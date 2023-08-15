package edu.asu.conceptpower.app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import edu.asu.conceptpower.app.model.IndexingEvent;

public interface IIndexRepository extends PagingAndSortingRepository<IndexingEvent, String>  {

    void save(IndexingEvent bean);

    Iterable<IndexingEvent> findAll();
}
