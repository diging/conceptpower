package edu.asu.conceptpower.servlet.rest;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ConceptType;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;
import edu.asu.conceptpower.servlet.xml.XMLMessageFactory;

/**
 * This class provides a method to retrieve all concepts for a given word and
 * part of speech. It answers requests of the form:
 * "http://[server.url]/conceptpower/rest/ConceptLookup/{word}/{pos}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
public class ConceptLookup {

    @Autowired
    private IConceptManager dictManager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private XMLMessageFactory messageFactory;

    @Autowired
    private URIHelper creator;

    private final String madsrdf = "http://www.loc.gov/mads/rdf/v1#";
    private final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private final String cidocCrm = "http://www.cidoc-crm.org/cidoc-crm/";
    private final String dCTerms = "http://purl.org/dc/terms/";
    private final String schema = "http://schema.org/";
    private final String changeset = "http://purl.org/vocab/changeset/schema#";
    private final String owl = "http://www.w3.org/2002/07/owl#";
    private final String skos = "http://www.w3.org/2008/05/skos#";
    private final String skosType = "http://www.w3.org/2008/05/skos#Concept";
    private final String authorityType = "http://www.loc.gov/mads/rdf/v1#Authority";

    /**
     * This method provides information of a concept for a rest interface of the
     * form "http://[server.url]/conceptpower/rest/ConceptLookup/{word}/{pos}"
     * 
     * @param word
     *            String value of concept to be looked
     * @param pos
     *            String value of the POS of concept to be looked
     * @return XML containing information of given concept for given POS
     */
    @RequestMapping(value = "rest/ConceptLookup/{word}/{pos}", method = RequestMethod.GET, produces = "application/xml")
    public @ResponseBody ResponseEntity<String> getWordNetEntry(@PathVariable("word") String word,
            @PathVariable("pos") String pos) {
        ConceptEntry[] entries = null;
        try {
            entries = dictManager.getConceptListEntriesForWord(word, pos, null);
        } catch (LuceneException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        for (ConceptEntry entry : entries) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
        }

        XMLConceptMessage returnMsg = messageFactory.createXMLConceptMessage();
        List<String> xmlEntries = new ArrayList<String>();
        if (entries != null) {
            xmlEntries = returnMsg.appendEntries(entryMap);
        }

        return new ResponseEntity<String>(returnMsg.getXML(xmlEntries), HttpStatus.OK);
    }

    @RequestMapping(value = "rest/ConceptLookupRdf/{word}/{pos}", method = RequestMethod.GET, produces = "application/xml")
    public @ResponseBody ResponseEntity<String> getWordNetEntryInRdf(@PathVariable("word") String word,
            @PathVariable("pos") String pos) {
        ConceptEntry[] entries = null;
        try {
            entries = dictManager.getConceptListEntriesForWord(word, pos, null);
        } catch (LuceneException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Model model = ModelFactory.createDefaultModel();
        String syntax = "RDF/XML";
        StringWriter out = new StringWriter();
        for (ConceptEntry entry : entries) {
            generateRDF(model, syntax, out, entry);
        }
        return new ResponseEntity<String>(out.toString(), HttpStatus.OK);
    }

    private void generateRDF(Model model, String syntax, StringWriter out, ConceptEntry entry) {
        model.setNsPrefixes(setPrefixMap());

        
        if(entry.getId().equalsIgnoreCase("WID-04206225-N-03-pony")){
            System.out.println("Hold");
        }
        
        Property identifiers = model.createProperty(dCTerms + "identifiers");
        Property madsrdfProperty = model.createProperty(madsrdf + "authoritativeLabel");
        Property skosProperty = model.createProperty(skos + "prefLabel");
        Property schemaName = model.createProperty(schema + "name");
        Property schemaDescription = model.createProperty(schema + "description");
        Property madsrdfCL = model.createProperty(madsrdf + "isMemberOfMADSCollection");
        Property csCreator = model.createProperty(changeset + "creatorName");
        Property owlEqualTo = model.createProperty(owl + "sameAs");
        Property rdfsSeeAlso = model.createProperty(rdf + "seeAlso");
        Property skosRelated = model.createProperty(skos + "related");

        Resource resource = model.createResource(creator.getURI(entry));

        resource.addProperty(identifiers, entry.getId());
        resource.addProperty(madsrdfProperty, entry.getWord());
        resource.addProperty(skosProperty, entry.getWord());
        resource.addProperty(schemaName, entry.getWord());
        resource.addProperty(schemaDescription, entry.getDescription());

        if (entry.getConceptList() != null) {
            resource.addProperty(madsrdfCL, creator.getConceptListURI(entry));
        }

        if (entry.getCreatorId() != null) {
            resource.addProperty(csCreator, entry.getCreatorId());
        }

        if (entry.getModified() != null) {
            resource.addProperty(csCreator, entry.getCreatorId());
        }

        if (entry.getEqualTo() != null) {
            String[] equals = entry.getEqualTo().split(",");
            for (String equal : equals) {
                resource.addProperty(owlEqualTo, equal);
            }
        }

        if (entry.getSimilarTo() != null) {
            String[] similarTo = entry.getSimilarTo().split(",");
            for (String similar : similarTo) {
                resource.addProperty(rdfsSeeAlso, similar);
            }
        }

        if (entry.getWordnetId() != null) {
            resource.addProperty(owlEqualTo, creator.getWordnetURI(entry));
        }

        if (entry.getSynonymIds() != null) {
            String[] synonymIds = entry.getSynonymIds()
                    .split(edu.asu.conceptpower.servlet.core.Constants.SYNONYM_SEPARATOR);
            for (String synonymId : synonymIds) {
                resource.addProperty(skosRelated, synonymId);
            }
        }

        Resource typeResource = model.createResource(skosType);
        resource.addProperty(RDF.type, typeResource);

        Resource typeRes = model.createResource(authorityType);
        resource.addProperty(RDF.type, typeRes);

        if (entry.getTypeId() != null) {
            Resource typesRes = model.createResource(creator.getTypeURI(entry));
            resource.addProperty(RDF.type, typesRes);
        }

        String matchesURI = entry.getTypeId();
        if (matchesURI != null) {
            String matches = typeManager.getType(matchesURI).getMatches();
            Resource cdocResource = model.createResource(matches);
            resource.addProperty(RDF.type, cdocResource);
        }

        model.write(out, syntax);
    }

    private Map<String, String> setPrefixMap() {
        Map<String, String> prefixMap = new HashMap<String, String>();
        prefixMap.put("skos", skos);
        prefixMap.put("madsrdf", madsrdf);
        prefixMap.put("dcterms", dCTerms);
        prefixMap.put("schema", schema);
        prefixMap.put("cs", changeset);
        prefixMap.put("owl", owl);
        prefixMap.put("cidoccrm", cidocCrm);
        prefixMap.put("rdf", rdf);
        return prefixMap;
    }
}