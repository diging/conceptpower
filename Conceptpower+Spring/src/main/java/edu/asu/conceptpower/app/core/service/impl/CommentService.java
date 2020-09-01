package edu.asu.conceptpower.app.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.core.model.impl.Comment;
import edu.asu.conceptpower.app.core.repository.ICommentRepository;
import edu.asu.conceptpower.app.core.service.ICommentService;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
@Transactional
public class CommentService implements ICommentService{
    
    @Autowired
    private ICommentRepository dao;

    public CommentService() {
        super();
    }

    public void create(final Comment entity) {
        dao.create(entity);
    }

    public Comment findOne(final long id) {
        return dao.findOne(id);
    }

    public List<Comment> findAll() {
        return dao.findAll();
    }

    
}