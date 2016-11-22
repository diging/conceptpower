package edu.asu.conceptpower.rest.msg.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.ITypeMessage;

public class JsonTypeMessage implements ITypeMessage {

    private URIHelper uriCreator;

    public JsonTypeMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    @Override
    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) throws JsonProcessingException {

        ConceptTypeMessage jsonType = new ConceptTypeMessage();
        if (type != null) {
            jsonType.setTypeId(type.getTypeId());
            jsonType.setTypeUri(uriCreator.getTypeURI(type));
        }
        jsonType.setDescription(type.getDescription());
        jsonType.setCreatorId(
                type.getCreatorId() != null ? type.getCreatorId().trim() : "");
        jsonType.setMatches(type.getMatches() != null ? type.getMatches().trim() : "");
        jsonType.setModifiedBy(type.getModified() != null ? type.getModified().trim() : "");
        // supertype
        if (supertype != null) {
            ConceptTypeMessage superTypeJson = new ConceptTypeMessage();
            superTypeJson.setTypeId(supertype.getTypeId());
            superTypeJson.setTypeUri(uriCreator.getTypeURI(supertype));
            superTypeJson.setTypeName(supertype.getTypeName() != null ? supertype.getTypeName() : "");
            jsonType.setSuperType(superTypeJson);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(jsonType);
    }

}
