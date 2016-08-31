package edu.asu.conceptpower.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.xml.XMLConstants;

/**
 * This class creates URIs for concepts and types based on the in xml-config.xml
 * configured URI prefixes.
 */
@Service
public class URIHelper implements IURIHelper {

    @Autowired
    private XMLConfig xmlConfig;

    public String getURI(ConceptEntry entry) {

        String uriPrefix = xmlConfig.getUriPrefix();

        if (entry.getId() != null && !entry.getId().isEmpty())
            return uriPrefix + entry.getId();

        else
            return uriPrefix + entry.getWordnetId();
    }

    public String getTypeURI(ConceptType type) {

        return XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX + type.getTypeId();
    }

    public String getTypeURI(ConceptEntry entry) {
        return XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX + entry.getTypeId();
    }

    public String getTypeId(String typeUriOrId) {
        if (!typeUriOrId.startsWith(XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX)) {
            return typeUriOrId;
        }

        return typeUriOrId.substring((XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX).length());
    }

    public String getConceptListURI(ConceptEntry entry) {
        String list = entry.getConceptList();
        return XMLConstants.LIST_NAMESPACE + list;
    }

    public String getWordnetURI(ConceptEntry entry) {
        String wordnetId = entry.getWordnetId();
        return XMLConstants.WORDNET_NAMESPACE + wordnetId;
    }
}
