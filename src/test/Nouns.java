package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.trees.TypedDependency;

public class Nouns {
	
	private static final Set<String> nounInGovernerList = new HashSet<>();
	private static final Set<String> nounInDependentList = new HashSet<>();;
	private static final Set<String> nounInBothList = new HashSet<>();
	private static final Set<String> typeModifiersList = new HashSet<>();
	private static final Set<String> numberList = new HashSet<>();
	public static void initializeNounLabels() {
		
		nounInGovernerList.add("amod");
		nounInGovernerList.add("det");
		nounInGovernerList.add("nn");
		nounInGovernerList.add("poss");
		nounInGovernerList.add("possessive");
		nounInGovernerList.add("preconj");
		nounInGovernerList.add("predeterminer");
		nounInGovernerList.add("rcmod");		
		
		nounInDependentList.add("agent");
		nounInDependentList.add("dobj");
		nounInDependentList.add("iobj");
		nounInDependentList.add("nn");
		nounInDependentList.add("nsubj");
		nounInDependentList.add("nsubjpass");
		nounInDependentList.add("pobj");
		nounInDependentList.add("poss");
		nounInDependentList.add("xsubj");
		
		nounInBothList.add("poss");
		nounInBothList.add("nn");
		
		typeModifiersList.add("amod");
		typeModifiersList.add("compound");
		
		numberList.add("nummod");
	}
	
	public Map<String, WordEntry> parseNouns(List<TypedDependency> dependencies) {
		Map<String, WordEntry> entries = new HashMap<>();
		WordEntry newWordEntryValue;
		String currentRelation;
		String typeString;
		for(TypedDependency dependency : dependencies) {
			currentRelation = dependency.reln().toString();			
			if(nounInDependentList.contains(currentRelation)) {
				typeString = dependency.dep().toString();
				if(entries.containsKey(typeString)) {
					entries.get(typeString).addIndex(dependency.dep().index());
				} else {
					newWordEntryValue = new WordEntry(typeString, Type.NOUN);
					newWordEntryValue.addIndex(dependency.dep().index());
					entries.put(typeString, newWordEntryValue);
				}				
			}
			if(nounInGovernerList.contains(currentRelation)) {
				typeString = dependency.gov().toString();
				if(entries.containsKey(typeString)) {
					entries.get(typeString).addIndex(dependency.gov().index());
				} else {
					newWordEntryValue = new WordEntry(typeString, Type.NOUN);
					newWordEntryValue.addIndex(dependency.gov().index());
					entries.put(typeString, newWordEntryValue);
				}				
			}
		}
		return entries;
	}
	
	public Map<WordEntry, List<WordEntry>> parseTypes(List<TypedDependency> dependencies, Map<String, WordEntry> nounList) {
		Map<WordEntry, List<WordEntry>> nounToTypeMap = new HashMap<>();
		String currentRelation;
		WordEntry newWordEntry;
		WordEntry newTypeWordEntry;
		List<WordEntry> typeList;
		int lastIndex = -1;
		List<Integer> indices;
		
		for(TypedDependency dependency : dependencies) {
			currentRelation = dependency.reln().toString();
			if(typeModifiersList.contains(currentRelation)) {
				newWordEntry = new WordEntry(dependency.gov().toString(), Type.NOUN);
				if(nounList.containsKey(dependency.gov().toString())) {
					newWordEntry = nounList.get(dependency.gov().toString());
					newTypeWordEntry = new WordEntry(dependency.dep().toString(), Type.ADJECTIVE);
					newTypeWordEntry.addIndex(dependency.dep().index());
					typeList = nounToTypeMap.getOrDefault(newWordEntry, new ArrayList<WordEntry>());					
					if(typeList.size() != 0) {
						indices = typeList.get(typeList.size() - 1).getIndices();
						lastIndex = indices.get(indices.size() - 1);
						if(lastIndex + 1 == dependency.dep().index()) {
							newTypeWordEntry.prependWord(typeList.get(typeList.size() - 1).getWord(), lastIndex);
							typeList.remove(typeList.size() - 1);
						}
					}					
					typeList.add(newTypeWordEntry);
					nounToTypeMap.put(newWordEntry, typeList);
				}
			}			
		}
		return nounToTypeMap;
	}
	
	public void parseNumMods(List<TypedDependency> dependencies, Map<WordEntry, List<WordEntry>> nounToTypeMap, Map<String,WordEntry> nounMap) {
		String currentRelation;
		WordEntry newWordEntry;
		for(TypedDependency dependency : dependencies) {
			currentRelation = dependency.reln().toString();
			if(numberList.contains(currentRelation)) {
				newWordEntry = new WordEntry(dependency.gov().toString(), Type.NOUN);
				if(nounToTypeMap.keySet().contains(newWordEntry)) {
					for(WordEntry w : nounToTypeMap.get(newWordEntry)) {
						if(w.getIndices().contains(dependency.dep().index() + 1)) {
							w.associateQuantity(Integer.parseInt(dependency.dep().value()));
						}
					}
				} else if(nounMap.containsKey(dependency.gov().toString())) {
					WordEntry w = nounMap.get(dependency.gov().toString());
					if(w.getIndices().contains(dependency.dep().index() + 1)) {
						w.associateQuantity(Integer.parseInt(dependency.dep().value()));
					}
					nounToTypeMap.put(w, new ArrayList<>());
				}
			}
		}
	}
	
	public Set<String> parseConsecutiveTypes(List<TypedDependency> dependencies, Set<String> nounList, Set<String> typeList) {
		Set<String> consecutiveTypesList = new HashSet<>();
		
		
		
		return consecutiveTypesList;
	}
}
