package edu.asu.conceptpower.app.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface POS {

	public final static String NOUN = "noun";
	public final static String VERB = "verb";
	public final static String ADVERB = "adverb";
	public final static String ADJECTIVE = "adjective";
	public final static String OTHER = "other";
	
	public final static List<String> posValues = new ArrayList<String>(Arrays.asList(new String[] {POS.NOUN, POS.VERB, POS.ADVERB, POS.ADJECTIVE, POS.OTHER}));

    public static Map<String, String> getPosMap() {
        Map<String, String> posMap = new LinkedHashMap<>();
        posMap.put(POS.NOUN, "Noun");
        posMap.put(POS.VERB, "Verb");
        posMap.put(POS.ADVERB, "Adverb");
        posMap.put(POS.ADJECTIVE, "Adjective");
        return posMap;
    }
} 
