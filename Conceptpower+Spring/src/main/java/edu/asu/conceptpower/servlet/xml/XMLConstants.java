package edu.asu.conceptpower.servlet.xml;

public interface XMLConstants {

	public final static String NAMESPACE = "http://www.digitalhps.org/";
	public final static String NAMESPACE_PREFIX = "digitalHPS";
	public final static String ID_NAMESPACE = "http://www.digitalhps.org/concepts/";
	public final static String TYPE_NAMESPACE = "http://www.digitalhps.org/types/";
	public final static String TYPE_PREFIX = "TYPE_";
	
	// nodes
	public final static String CONCEPTPOWER_ANSWER = "conceptpowerReply";
	public final static String CONCEPT_ENTRY = "conceptEntry";
	public final static String TYPE_ENTRY = "type_entry";
	
	public final static String ID = "id";
	public final static String CONCEPT_URI = "concept_uri";
	public final static String CONCEPT_ID = "concept_id";
	
	public final static String POS = "pos";
	public final static String LEMMA = "lemma";
	public final static String DESCRIPTION = "description";
	public final static String CONCEPT_LIST = "conceptList";
	public final static String CREATOR_ID = "creator_id";
	public final static String EQUAL_TO = "equal_to";
	public final static String MODIFIED_BY = "modified_by";
	public final static String SIMILAR_TO = "similar_to";
	public final static String SYNONYM_IDS = "synonym_ids";
	public final static String IS_DELETED = "deleted";
	public final static String WORDNET_ID = "wordnet_id";
	public final static String TYPE = "type";
	public final static String SUPERTYPE = "supertype";
	public final static String TYPE_ID_ATTR = "type_id";
	public final static String TYPE_URI_ATTR = "type_uri";
	public final static String MATCHES = "matches";
}
