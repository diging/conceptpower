package edu.asu.conceptpower.app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.Comment;

/**
 * Comment repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface ICommentRepository extends PagingAndSortingRepository<Comment, String>{
    
}