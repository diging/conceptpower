package edu.asu.conceptpower.rest.msg.json;

import org.apache.commons.lang3.StringEscapeUtils;

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
        jsonType.setDescription(StringEscapeUtils.escapeXml10(type.getDescription()));
        jsonType.setCreatorId(
                StringEscapeUtils.escapeXml10(type.getCreatorId() != null ? type.getCreatorId().trim() : ""));
        jsonType.setMatches(StringEscapeUtils.escapeXml10(type.getMatches() != null ? type.getMatches().trim() : ""));
        jsonType.setModifiedBy(
                StringEscapeUtils.escapeXml10(type.getModified() != null ? type.getModified().trim() : ""));
        // supertype
        if (supertype != null) {
            ConceptTypeMessage superTypeJson = new ConceptTypeMessage();
            superTypeJson.setTypeId(supertype.getTypeId());
            superTypeJson.setTypeUri(uriCreator.getTypeURI(supertype));
            superTypeJson.setTypeName(StringEscapeUtils.escapeXml10(supertype.getTypeName()));
            jsonType.setSuperType(superTypeJson);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(jsonType);
    }

}
