package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.conceptpower.app.core.model.impl.Comment;

public interface ICommentRepository extends PagingAndSortingRepository<Comment, String>{
    
}