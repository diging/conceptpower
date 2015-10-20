package edu.asu.conceptpower.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface POS {

	public final static String NOUN = "noun";
	public final static String VERB = "verb";
	public final static String ADVERB = "adverb";
	public final static String ADJECTIVE = "adjective";
	public final static String OTHER = "other";
	
	public final static List<String> posValues = new ArrayList<String>(Arrays.asList(new String[] {POS.NOUN, POS.VERB, POS.ADVERB, POS.ADJECTIVE, POS.OTHER}));
} 
