package edu.asu.conceptpower.app.db.customobjects;

import java.util.ArrayList;

import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectSet;


/*
 * This class is to construct a valid return object of type ObjectSet for queryByExample() method
 * @author: Keerthivasan Krishnamurthy
 */

public class ReviewRequestObjectSet extends ArrayList<Object> implements ObjectSet<Object> {

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
    public Object next() {
        return this.iterator().next();
    }

    @Override
    public void reset() {
        //
    }
    
}