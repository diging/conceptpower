package edu.asu.conceptpower.app.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptType;
import jakarta.annotation.PostConstruct;

@Component
public class TypeDatabaseClient {

    private ObjectContainer client;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("typesDatabaseManager")
    private DatabaseManager typeDatabase;

    @Autowired
    private URIHelper uriHelper;

    @PostConstruct
    public void init() {
        this.client = typeDatabase.getClient();
    }

    public ConceptType getType(String uriOrId) {
        String id = uriHelper.getTypeId(uriOrId);
        ConceptType user = new ConceptType(id);
        ObjectSet<ConceptType> results = client.queryByExample(user);

        // there should only be exactly one object with this id
        if (results.size() == 1)
            return results.get(0);

        return null;
    }

    public ConceptType findType(String name) {
        ConceptType user = new ConceptType();
        user.setTypeName(name);

        ObjectSet<ConceptType> results = client.queryByExample(user);
        // there should only be exactly one object with this id
        if (results.size() >= 1)
            return results.get(0);

        return null;
    }

    public ConceptType[] getAllTypes() {
        ObjectSet<ConceptType> results = client.query(ConceptType.class);
        return results.toArray(new ConceptType[results.size()]);
    }

    public ConceptType addType(ConceptType user) {
        client.store(user);
        client.commit();
        return user;
    }

    public void deleteType(String id) {
        ConceptType user = new ConceptType();
        user.setTypeId(id);

        ObjectSet<ConceptType> results = client.queryByExample(user);
        for (ConceptType res : results) {
            client.delete(res);
            client.commit();
        }
    }

    public void update(ConceptType type) {
        ConceptType toBeUpdated = getType(type.getTypeId());
        toBeUpdated.setCreatorId(type.getCreatorId());
        toBeUpdated.setDescription(type.getDescription());
        toBeUpdated.setMatches(type.getMatches());
        toBeUpdated.setModified(type.getModified());
        toBeUpdated.setSupertypeId(type.getSupertypeId());
        toBeUpdated.setTypeId(type.getTypeId());
        toBeUpdated.setTypeName(type.getTypeName());
        client.store(toBeUpdated);
        client.commit();
    }

    public List<ConceptType> getAllTypes(int page, int pageSize, final String sortBy, int sortDirection)
            throws NoSuchFieldException, SecurityException {

        Query typeQuery = client.query();
        typeQuery.constrain(ConceptType.class);

        final Field sortField = ConceptType.class.getDeclaredField(sortBy);
        sortField.setAccessible(true);

        Comparator<ConceptType> conceptTypeComparator = new Comparator<ConceptType>() {

            @Override
            public int compare(ConceptType o1, ConceptType o2) {
                Object o1FieldContent;
                Object o2FieldContent;
                try {
                    if (sortDirection == IConceptDBManager.ASCENDING) {
                        o1FieldContent = sortField.get(o1);
                        o2FieldContent = sortField.get(o2);
                    } else {
                        o2FieldContent = sortField.get(o1);
                        o1FieldContent = sortField.get(o2);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("Error accessing field.", e);
                    return 0;
                }

                if (o1FieldContent instanceof Integer) {
                    return ((Integer) o1FieldContent).compareTo((Integer) o2FieldContent);
                }
                if (o1FieldContent == null || o2FieldContent == null) {
                    return 0;
                }
                return o1FieldContent.toString().compareTo(o2FieldContent.toString());
            }

        };

        typeQuery.sortBy(conceptTypeComparator);
        List<ConceptType> conceptTypes = typeQuery.execute();

        int startIndex = (page - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex > conceptTypes.size()) {
            endIndex = conceptTypes.size();
        }

        return new ArrayList<ConceptType>(conceptTypes.subList(startIndex, endIndex));
    }

}
