package edu.asu.conceptpower.servlet.rest;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.tdb.assembler.Vocab;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
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
import edu.asu.conceptpower.servlet.rdf.RDFConceptMessage;
import edu.asu.conceptpower.servlet.rdf.RDFMessageFactory;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;
import edu.asu.conceptpower.servlet.xml.XMLMessageFactory;

/**
 * This class provides a method to retrieve all concepts
 * for a given word and part of speech. It answers requests of the form:
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
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        
        Model model = ModelFactory.createDefaultModel();
        
        
//        Madsrdf (http://www.loc.gov/mads/rdf/v1#)
//            RDF (http://www.w3.org/1999/02/22-rdf-syntax-ns#)
//            SKOS (http://www.w3.org/2004/02/skos/core#)
//            CIDOC CRM (http://www.cidoc-crm.org/cidoc-crm/)
//            DC Terms (http://purl.org/dc/terms/)
//            schema.org (http://schema.org/)
//            Changeset (http://purl.org/vocab/changeset/schema#) → cs
//            OWL (http://www.w3.org/2002/07/owl#)
        
        for (ConceptEntry entry : entries) {
            
          //  Resource conceptResource = model.createResource(creator.getURI(entry)).addProperty(FOAF.name, entry.getWord()).addProperty("", o);
          
            
        }

        //RDFConceptMessage returnMsg = RDFConceptMessage.createRDFConceptMessage();
        List<String> xmlEntries = new ArrayList<String>();
//        if (entries != null) {
//            xmlEntries = returnMsg.appendEntries(entryMap);
//        }
        
        
        
        String syntax = "RDF/XML-ABBREV"; // also try "N-TRIPLE" and "TURTLE"
        StringWriter out = new StringWriter();
        model.write(out, syntax);
        String result = out.toString();
        
        
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }
}
