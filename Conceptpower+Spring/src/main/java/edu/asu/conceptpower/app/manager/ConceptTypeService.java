package edu.asu.conceptpower.app.manager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypeService;
import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.app.repository.IConceptTypeRepository;

@Service
public class ConceptTypeService implements IConceptTypeService{

    @Autowired
    private IConceptTypeRepository conceptTypeRepository;
    
    @Value("${default_page_size}")
    private Integer defaultPageSize;
    
    @Override
    public void addConceptType(ConceptType type) {
        String id = null;
        while (true) {
            id = UUID.randomUUID().toString();
            if (!conceptTypeRepository.existsById(id)) {
                break;
            }
        }
        
        type.setTypeId(id);
        
        conceptTypeRepository.save(type);   
    }

    @Override
    public void storeModifiedConceptType(ConceptType type) {
        conceptTypeRepository.save(type);
    }

    @Override
    public List<ConceptType> getAllTypes() {
        return conceptTypeRepository.findAll();
    }

    @Override
    public ConceptType getType(String id) {
        //TODO Must see if we need to create a custom Id to create a row 
        Optional<ConceptType> type = conceptTypeRepository.findById(id);
        return  type.isPresent() ? type.get() : null;
    }

    @Override
    public void deleteType(String id) {
        conceptTypeRepository.deleteById(id);
        
    }

    @Override
    public int getPageCount() {
       return (int) Math.ceil((double)conceptTypeRepository.findAll().size()/defaultPageSize);
    }

    @Override
    public List<ConceptType> getConceptTypes(int pageNo, int pageSize, String sortBy, int sortDirection)
            throws NoSuchFieldException, SecurityException {
        pageNo = pageNo < 0 ? 0 : pageNo;
        
        pageSize = pageSize == -1 ? defaultPageSize : pageSize;
        
        return conceptTypeRepository.findAll(
                    sortDirection == 1 ?
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()) 
                    :
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending())).getContent();
    }

}
