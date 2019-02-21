package edu.asu.conceptpower.app.db4o;


import com.db4o.ObjectSet;

import edu.asu.conceptpower.core.ReviewRequest;

public interface ICommentsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);

    ObjectSet<ReviewRequest> queryByExample(Object example);

    
}
