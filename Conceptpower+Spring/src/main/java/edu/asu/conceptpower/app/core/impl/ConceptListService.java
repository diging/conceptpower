package edu.asu.conceptpower.app.core.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListService;
import edu.asu.conceptpower.app.core.model.impl.ConceptList;
import edu.asu.conceptpower.app.core.repository.IConceptListRepository;

/*
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 * */
@Service
public class ConceptListService implements IConceptListService{
    
    @Autowired
    private IConceptListRepository conceptListRepository;
    

    protected final String LIST_PREFIX = "LIST";
    
    @Override
    public void addConceptList(String name, String description) {
        ConceptList conceptList = new ConceptList();
        conceptList.setConceptListName(name);
        conceptList.setDescription(description);
        conceptList.setId(generateId(LIST_PREFIX));
        conceptListRepository.save(conceptList);
    }

    @Override
    public void deleteConceptList(String name) {
        // TODO: Must check whether should we proceed with delete by conceptList name
        conceptListRepository.deleteByConceptListName(name);
    }

    @Override
    public List<ConceptList> getAllConceptLists() {
        return conceptListRepository.findAll();
    }

    @Override
    public ConceptList getConceptList(String name) {
        // TODO: Must check whether should we proceed with delete by conceptList name
        return conceptListRepository.findByConceptListName(name);
    }

    @Override
    public void storeModifiedConceptList(ConceptList list, String listname) {
        // TODO Must see if we need listname argument
        conceptListRepository.save(list);
    }

    @Override
    public boolean checkExistingConceptList(String name) {
        return conceptListRepository.findByConceptListName(name) != null;
    }
    
    protected String generateId(String prefix) {
        String id = prefix + UUID.randomUUID().toString();

        while (true) {

            if (id != null && !conceptListRepository.existsById(id)) {
                return id;
            }

            // try other id
            id = prefix + UUID.randomUUID().toString();
        }
    }
}
