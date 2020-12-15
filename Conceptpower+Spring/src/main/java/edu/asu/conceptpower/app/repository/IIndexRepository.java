package edu.asu.conceptpower.app.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import edu.asu.conceptpower.app.model.IndexingEvent;

public interface IIndexRepository extends PagingAndSortingRepository<IndexingEvent, String>  {
    List<IndexingEvent> findAll();
}
