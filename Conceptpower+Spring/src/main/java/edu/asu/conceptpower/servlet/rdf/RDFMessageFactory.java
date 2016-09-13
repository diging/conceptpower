package edu.asu.conceptpower.servlet.rdf;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.root.URIHelper;

/**
 * 
 * @author karthikeyanmohan
 *
 */
@Component
public class RDFMessageFactory {

    @Autowired
    private TypeDatabaseClient typeManager;

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

    public String generateRDF(ConceptEntry[] entries) {
        Model model = ModelFactory.createDefaultModel();
        String syntax = "RDF/XML";
        StringWriter out = new StringWriter();
        model.setNsPrefixes(getPrefixMap());
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

        for (ConceptEntry entry : entries) {

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
                if (matches != null && !matches.equalsIgnoreCase("")) {
                    Resource cdocResource = model.createResource(matches);
                    resource.addProperty(RDF.type, cdocResource);
                }
            }

        }
        model.write(out, syntax);
        return out.toString();
    }

    private Map<String, String> getPrefixMap() {
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
