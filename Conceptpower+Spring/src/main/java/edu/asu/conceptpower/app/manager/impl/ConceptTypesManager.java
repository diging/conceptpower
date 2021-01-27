package edu.asu.conceptpower.app.manager.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.manager.IConceptTypeManager;
import edu.asu.conceptpower.app.model.ConceptType;

@Service
public class ConceptTypesManager implements IConceptTypeManager {

    @Autowired
    private TypeDatabaseClient client;

    @Value("${default_page_size}")
    private Integer defaultPageSize;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.conceptpower.core.IConceptTypeManger#addConceptType(edu.asu.
     * conceptpower.core.ConceptType)
     */
    @Override
    public void addConceptType(ConceptType type) {

        String id = null;
        while (true) {
            id = UUID.randomUUID().toString();
            ConceptType exist = client.getType(id);
            if (exist == null)
                break;
        }

        type.setTypeId(id);

        client.addType(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptTypeManger#storeModifiedConceptType(edu
     * .asu.conceptpower.core.ConceptType)
     */
    @Override
    public void storeModifiedConceptType(ConceptType type) {
        client.update(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.conceptpower.core.IConceptTypeManger#getAllTypes()
     */
    @Override
    public List<ConceptType> getAllTypes() {
        return client.getAllTypes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptTypeManger#getType(java.lang.String)
     */
    @Override
    public ConceptType getType(String id) {
        return client.getType(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.conceptpower.core.IConceptTypeManger#deleteType(java.lang.String)
     */
    @Override
    public void deleteType(String id) {
        client.deleteType(id);
    }

    @Override
    public int getPageCount() {
        return (int) Math.ceil((double) getTotalNumberOfConceptTypes() / (double) defaultPageSize);
    }

    /**
     * Returns the concept types based on page number, page size, sort by column
     * and sort direction.
     * 
     * If page size is -1 then default page size is set as page size.
     * 
     * If page number is less than 1 or null, then page number is set as 1.
     * 
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDirection
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    @Override
    public List<ConceptType> getConceptTypes(int page, int pageSize, String sortBy, int sortDirection)
            throws NoSuchFieldException, SecurityException {

        if (pageSize == -1) {
            pageSize = defaultPageSize;
        }
        if (page < 1) {
            page = 1;
        }
        int pageCount = getTotalNumberOfConceptTypes();
        pageCount = pageCount > 0 ? pageCount : 1;
        if (page > pageCount) {
            page = pageCount;
        }

        return client.getAllTypes(page, pageSize, sortBy, sortDirection);
    }

    private int getTotalNumberOfConceptTypes() {
        return client.getNumberOfConceptTypes();
    }

}
