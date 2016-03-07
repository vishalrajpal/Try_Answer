package test;

import java.util.ArrayList;
import java.util.List;

public class PennPOSTagsLists {
	
	public static final List<PennPOSTags> PENN_NOUN_TAGS = new ArrayList<>();
	public static final List<PennPOSTags> PENN_ADJECTIVE_TAGS = new ArrayList<>();
	public static void initializeTagLists() {
		PENN_NOUN_TAGS.add(PennPOSTags.NN);
		PENN_NOUN_TAGS.add(PennPOSTags.NNS);
		PENN_NOUN_TAGS.add(PennPOSTags.NNP);
		PENN_NOUN_TAGS.add(PennPOSTags.NNPS);
		
		PENN_ADJECTIVE_TAGS.add(PennPOSTags.JJ);
	}
	
	public static boolean isANoun(String tag) {
		return PENN_NOUN_TAGS.contains(PennPOSTags.valueOf(tag));
	}
	
	public static boolean isAAdjective(String tag) {
		return PENN_ADJECTIVE_TAGS.contains(PennPOSTags.valueOf(tag));
		//return true;
	}
}
