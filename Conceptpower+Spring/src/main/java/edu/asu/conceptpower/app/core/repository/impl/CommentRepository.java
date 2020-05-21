package edu.asu.conceptpower.app.core.repository.impl;

import edu.asu.conceptpower.app.core.model.impl.Comment;
import edu.asu.conceptpower.app.core.repository.AbstractJPA;
import edu.asu.conceptpower.app.core.repository.ICommentRepository;

public class CommentRepository extends AbstractJPA<Comment> implements ICommentRepository{
    
    public CommentRepository() {
        super();
        setClazz(Comment.class);
    }
    
}