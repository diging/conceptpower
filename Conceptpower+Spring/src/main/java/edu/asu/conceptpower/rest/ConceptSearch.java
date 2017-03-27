package edu.asu.conceptpower.rest;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.Pagination;

/**
 * This class provides a method to search concepts. It answers requests to:
 * "http://[server.url]/conceptpower/rest/ConceptSearch?{list of fieldname=value}{operator=and/or}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
@PropertySource("classpath:config.properties")
public class ConceptSearch {

    @Autowired
    private IIndexService manager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private IMessageRegistry messageFactory;

    @Autowired
    private URIHelper uriHelper;

    @Value("${default_page_size}")
    private int numberOfRecordsPerPage;

    @Autowired
    private ConceptSearchParameterValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    private static final Logger logger = LoggerFactory.getLogger(ConceptSearch.class);

    /**
     * This method provides information of a concept for a rest interface of the
     * form
     * "http://[server.url]/conceptpower/rest/Type?id={URI or ID of concept}"
     * 
     * @param req
     *            Holds HTTP request information
     * @return
     * @throws JsonProcessingException
     * @throws IndexerRunningException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/ConceptSearch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<String> searchConcept(
            @Validated ConceptSearchParameters conceptSearchParameters, BindingResult result,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
                    throws JsonProcessingException, IllegalArgumentException, IllegalAccessException,
                    IndexerRunningException, IntrospectionException, InvocationTargetException {

        if (result.hasErrors()) {
            IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
            String errorMessage = msg.getErrorMessages(result.getAllErrors());
            return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> searchFields = new HashMap<String, String>();
        String operator = SearchParamters.OP_AND;

        int page = 1;

        for (Field field : conceptSearchParameters.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            searchFields.put(field.getName().trim(), String.valueOf(field.get(conceptSearchParameters)).trim());
        }

        preprocessConceptSearchParameterBean(conceptSearchParameters);
        PropertyDescriptor pageDescriptor = new PropertyDescriptor("page", ConceptSearchParameters.class, "getPage",
                "setPage");
        if (pageDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}) != null) {
            page = Integer.parseInt(
                    String.valueOf(pageDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {})));
        }

        ConceptEntry[] searchResults = null;
        int totalNumberOfRecords = 0;
        try {
            totalNumberOfRecords = manager.getTotalNumberOfRecordsForSearch(searchFields, operator);
            searchResults = manager.searchForConceptByPageNumberAndFieldMap(searchFields, operator, page,
                    numberOfRecordsPerPage);
        } catch (LuceneException ex) {
            logger.error("Lucene Exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (searchResults == null || searchResults.length == 0) {
            // Data is not found but still returning OK as per review comments
            IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
            return new ResponseEntity<String>(msg.getErrorMessage("No records found for the search condition."),
                    HttpStatus.OK);
        }
        Map<ConceptEntry, ConceptType> entryMap = new LinkedHashMap<ConceptEntry, ConceptType>();
        IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
        createEntryMap(searchResults, entryMap);
        Pagination pagination = new Pagination(page, totalNumberOfRecords);
        return new ResponseEntity<String>(msg.getAllConceptEntriesAndPaginationDetails(entryMap, pagination),
                HttpStatus.OK);
    }

    /**
     * 
     * This method is used for setting the correct values for concept search
     * parameters.
     * 
     * @param conceptSearchParameters
     * @param page
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NumberFormatException
     */
    private void preprocessConceptSearchParameterBean(ConceptSearchParameters conceptSearchParameters)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NumberFormatException {

        PropertyDescriptor typeUriDescriptor = new PropertyDescriptor("type_uri", ConceptSearchParameters.class,
                "getType_uri", "setType_uri");
        Method typeUriReadMethod = typeUriDescriptor.getReadMethod();
        Object typeUri = typeUriReadMethod.invoke(conceptSearchParameters, new Object[] {});
        if (typeUri != null) {
            PropertyDescriptor typeIdDescriptor = new PropertyDescriptor("type_id", ConceptSearchParameters.class,
                    "getType_id", "setType_id");
            Method typeIdWriteMethod = typeIdDescriptor.getWriteMethod();
            typeIdWriteMethod.invoke(conceptSearchParameters,
                    new Object[] { uriHelper.getTypeId(String.valueOf(typeUri)) });
        }

        PropertyDescriptor operatorDescriptor = new PropertyDescriptor(SearchParamters.OPERATOR,
                ConceptSearchParameters.class, "getOperator", "setOperator");
        if (operatorDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}) == null) {
            operatorDescriptor.getWriteMethod().invoke(conceptSearchParameters,
                    new Object[] { SearchParamters.OP_AND });
        }

        PropertyDescriptor numberOfRecordsDescriptor = new PropertyDescriptor(
                SearchParamters.NUMBER_OF_RECORDS_PER_PAGE, ConceptSearchParameters.class,
                "getNumber_of_records_per_page", "setNumber_of_records_per_page");
        if (numberOfRecordsDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}) != null) {
            numberOfRecordsPerPage = Integer.parseInt(String.valueOf(
                    numberOfRecordsDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {})));
        }

        PropertyDescriptor equalToDescriptor = new PropertyDescriptor(SearchParamters.EQUAL_TO,
                ConceptSearchParameters.class, "getEqual_to", "setEqual_to");
        if (equalToDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}) != null) {
            String[] equalTo = String
                    .valueOf(equalToDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}))
                    .split(",");
            List<String> equalsList = Arrays.asList(equalTo);
            String equalsTo = equalsList.stream().map(i -> removeTrailingBackSlash(i)).collect(Collectors.joining(","));
            equalToDescriptor.getWriteMethod().invoke(conceptSearchParameters, new Object[] { equalsTo });
        }

        PropertyDescriptor similarToDescriptor = new PropertyDescriptor(SearchParamters.EQUAL_TO,
                ConceptSearchParameters.class, "getSimilar_to", "setSimilar_to");
        if (similarToDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}) != null) {
            String[] similarTo = String
                    .valueOf(equalToDescriptor.getReadMethod().invoke(conceptSearchParameters, new Object[] {}))
                    .split(",");
            List<String> similarList = Arrays.asList(similarTo);
            String similarToString = similarList.stream().map(i -> removeTrailingBackSlash(i))
                    .collect(Collectors.joining(","));
            similarToDescriptor.getWriteMethod().invoke(conceptSearchParameters, new Object[] { similarToString });
        }
    }

    private void createEntryMap(ConceptEntry[] searchResults, Map<ConceptEntry, ConceptType> entryMap) {
        for (ConceptEntry entry : searchResults) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
        }
    }

    private String removeTrailingBackSlash(String val) {
        if (val.endsWith("/")) {
            return val.substring(0, val.length() - 1);
        }
        return val;
    }
}
