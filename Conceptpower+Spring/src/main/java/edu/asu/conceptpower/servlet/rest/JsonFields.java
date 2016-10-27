package edu.asu.conceptpower.servlet.rest;

/**
 * Field names used for JSON payload and response when adding concepts through
 * API.
 * 
 * @author Julia Damerow
 *
 */
public interface JsonFields {

    public final static String ID = "id";
    public final static String SYNONYM_IDS = "synonymids";
    public final static String WORD = "word";
    public final static String CONCEPT_LIST = "conceptlist";
    public final static String POS = "pos";
    public final static String DESCRIPTION = "description";
    public final static String EQUALS = "equals";
    public final static String SIMILAR = "similar";
    public final static String TYPE = "type";
    public final static String URI = "uri";
    public final static String WORDNET_ID = "wordnetIds";
}