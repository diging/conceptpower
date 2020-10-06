package edu.asu.conceptpower.app.db.customobjects;

import java.util.ArrayList;

import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectSet;

import edu.asu.conceptpower.app.model.ReviewRequest;

/*
 * This class is to construct a valid return object of type ObjectSet for query() method
 * @author: Keerthivasan Krishnamurthy
 */

public class PredicateObjectSet extends ArrayList<ReviewRequest> implements ObjectSet<ReviewRequest> {

    private static final long serialVersionUID = 1L;

    @Override
    public ExtObjectSet ext() {
        return null;
    }

    @Override
    public boolean hasNext() {
       return this.iterator().hasNext();
    }

    @Override
    public ReviewRequest next() {
        return this.iterator().next();
    }

    @Override
    public void reset() {
        //
    }
    
}