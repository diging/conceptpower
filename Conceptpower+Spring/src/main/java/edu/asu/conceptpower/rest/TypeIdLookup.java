package edu.asu.conceptpower.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.ITypeMessage;
import edu.asu.conceptpower.rest.msg.xml.XMLConstants;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides a method to query types by their ids. It answers requests
 * to: "http://[server.url]/conceptpower/rest/Type?id={URI or ID of concept}"
 * 
 * @author Julia Damerow, Chetan
 * 
 */
@Controller
public class TypeIdLookup {

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private IMessageRegistry messageFactory;

    /**
     * This method provides information of a type for a rest interface of the
     * form "http://[server.url]/conceptpower/rest/Type?id={URI or ID of
     * concept}"
     * 
     * @param req
     *            Holds HTTP request information
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/Type", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<String> getTypeById(HttpServletRequest req,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
            throws JsonProcessingException {

        String id = req.getParameter("id");
        if (id == null) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        // get type id if URI is given

        String[] pathParts = id.split("/");
        int lastIndex = pathParts.length - 1;
        String typeId = null;
        if (lastIndex > -1)
            typeId = pathParts[lastIndex];

        if (typeId == null) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        if (typeId.startsWith(XMLConstants.TYPE_PREFIX)) {
            typeId = typeId.substring(XMLConstants.TYPE_PREFIX.length());
        }

        ConceptType type = typeManager.getType(typeId);

        ITypeMessage typeMessage = messageFactory.getMessageFactory(acceptHeader).createTypeMessage();

        String entry = null;
        if (type != null) {
            ConceptType superType = null;
            if (type.getSupertypeId() != null && !type.getSupertypeId().trim().isEmpty())
                superType = typeManager.getType(type.getSupertypeId().trim());
            entry = typeMessage.getConceptTypeMessage(type, superType);
        } else {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(entry, HttpStatus.OK);
    }
}
