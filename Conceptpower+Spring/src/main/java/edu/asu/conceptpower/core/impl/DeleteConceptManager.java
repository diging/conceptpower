package edu.asu.conceptpower.core.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IDeleteConceptManager;

@Service
public class DeleteConceptManager  implements IDeleteConceptManager{

    @Override
    public void setDelete(ConceptEntry entry) {
        entry.setDeleted(true);
    }

}
