package edu.asu.conceptpower.servlet.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.xml.IConceptMessage;
import edu.asu.conceptpower.servlet.xml.NotImplementedException;

public class JsonConceptMessage implements IConceptMessage {

    private URIHelper uriCreator;

    public JsonConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries) {

        ObjectMapper mapper = new ObjectMapper();
        List<ConceptMessageJson> conceptMessages = new ArrayList<>();

        for (ConceptEntry entry : entries.keySet()) {
            conceptMessages.add(getConceptMessage(entry, entries.get(entry)));
        }
        
        String json = null;
        
        try{
            json = mapper.writeValueAsString(conceptMessages);
        } catch(JsonProcessingException ex) {
            System.out.println("Supressing error");
            ex.printStackTrace();
        }
        return json;
    }

    private ConceptMessageJson getConceptMessage(ConceptEntry entry, ConceptType type) {

        ConceptMessageJson json = new ConceptMessageJson();

        json.setId(uriCreator.getURI(entry));
        json.setLemma(StringEscapeUtils.escapeXml10(entry.getWord()));
        json.setPos(entry.getPos());
        json.setDescription(StringEscapeUtils.escapeXml10(entry.getDescription()));
        json.setConceptList(StringEscapeUtils.escapeXml10(entry.getConceptList()));

        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            json.setCreatorId(StringEscapeUtils.escapeXml10(changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : ""));
        } else {
            json.setCreatorId(
                    StringEscapeUtils.escapeXml10(entry.getCreatorId() != null ? entry.getCreatorId().trim() : ""));
        }

        json.setEqualTo(StringEscapeUtils.escapeXml10(entry.getEqualTo() != null ? entry.getEqualTo().trim() : ""));
        json.setModifiedBy(
                StringEscapeUtils.escapeXml10(entry.getModified() != null ? entry.getModified().trim() : ""));

        json.setSimilarTo(
                StringEscapeUtils.escapeXml10(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : ""));

        json.setSynonymIds(
                StringEscapeUtils.escapeXml10(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : ""));

        if (type != null) {
            ConceptTypeJson jsonType = new ConceptTypeJson();
            jsonType.setTypeId(type.getTypeId());
            jsonType.setTypeUri(uriCreator.getTypeURI(type));
            jsonType.setTypeName(StringEscapeUtils.escapeXml10(type.getTypeName()));

            json.setType(jsonType);
        }

        json.setDeleted(entry.isDeleted());
        json.setWordnetId(
                StringEscapeUtils.escapeXml10(entry.getWordnetId() != null ? entry.getWordnetId().trim() : ""));
        
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {
                List<AlternativeId> alternativeIds = new ArrayList<>();
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    AlternativeId alternativeId = new AlternativeId();
                    alternativeId.setConceptId(uri.getKey());
                    alternativeId.setConceptUri(uri.getValue());
                    alternativeIds.add(alternativeId);
                }
                json.setAlternativeIds(alternativeIds);
            }
        }
        return json;
    }

    public void appendDictionaries(List<ConceptList> lists) throws NotImplementedException {
        throw new NotImplementedException();
    }

}
