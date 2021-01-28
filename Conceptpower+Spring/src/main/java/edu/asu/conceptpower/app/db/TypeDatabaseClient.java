package edu.asu.conceptpower.app.db;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.app.repository.IConceptTypeRepository;
import edu.asu.conceptpower.app.util.URIHelper;

@Component
public class TypeDatabaseClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IConceptTypeRepository conceptTypeRepository;
    
    @Value("${default_page_size}")
    private Integer defaultPageSize;
    
    @Autowired
    private URIHelper uriHelper;

    public ConceptType getType(String uriOrId) {
        String id = uriHelper.getTypeId(uriOrId);
        
        Optional<ConceptType> results = conceptTypeRepository.findById(id);

        if (results.isPresent()) {
            return results.get();
        }

        return null;
    }

    public ConceptType findType(String name) {
        Optional<ConceptType> results = conceptTypeRepository.findByTypeName(name);

        if (results.isPresent()) {
            return results.get();
        }

        return null;
    }

    public List<ConceptType> getAllTypes() {
        return conceptTypeRepository.findAll();
    }

    public ConceptType addType(ConceptType user) {
        conceptTypeRepository.save(user);
        return user;
    }

    public void deleteType(String id) {
        conceptTypeRepository.deleteById(id);
    }

    public void update(ConceptType type) {
        conceptTypeRepository.save(type);
    }

    public List<ConceptType> getAllTypes(int page, int pageSize, final String sortBy, int sortDirection){
        page = page < 0 ? 0 : page;

        pageSize = pageSize < 0 ? defaultPageSize : pageSize;

        return conceptTypeRepository.findAll(
                    sortDirection == 1 ?
                    PageRequest.of(page, pageSize, Sort.by(sortBy).ascending()) 
                    :
                    PageRequest.of(page, pageSize, Sort.by(sortBy).descending())).getContent();

    }
    
    public int getNumberOfConceptTypes() {
        return (int)conceptTypeRepository.count();
    }
}
