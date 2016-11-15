package edu.asu.conceptpower.app.json;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.app.xml.ITypeMessage;
import edu.asu.conceptpower.app.xml.URIHelper;
import edu.asu.conceptpower.core.ConceptType;

public class JsonTypeMessage implements ITypeMessage {

    private URIHelper uriCreator;

    public JsonTypeMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    @Override
    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) {

        ObjectMapper mapper = new ObjectMapper();

        ConceptTypeJson jsonType = new ConceptTypeJson();

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
            ConceptSuperTypeJson superTypeJson = new ConceptSuperTypeJson();
            superTypeJson.setTypeId(supertype.getTypeId());
            superTypeJson.setTypeUri(uriCreator.getTypeURI(supertype));
            superTypeJson.setTypeName(StringEscapeUtils.escapeXml10(supertype.getTypeName()));
            jsonType.setSuperType(superTypeJson);
        }

        String json = null;
        try {
            json = mapper.writeValueAsString(jsonType);
        } catch (JsonProcessingException ex) {
            // TODO
            ex.printStackTrace();
        }

        return json;
    }

}
