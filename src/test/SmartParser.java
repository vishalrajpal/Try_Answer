package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.trees.TypedDependency;

public class SmartParser {
	
	private static final Set<String> nounInGovernerList = new HashSet<>();
	private static final Set<String> nounInDependentList = new HashSet<>();;
	private static final Set<String> nounInBothList = new HashSet<>();
	private static final Set<String> typeModifiersList = new HashSet<>();
	private static final Set<String> numberList = new HashSet<>();
	private static final Set<PennRelation> universalNounsList = new HashSet<>();
	private static final Set<PennRelation> universalAdjectiveList = new HashSet<>();
	//All universal dependency tags with certainty greater than 1% according to 
	//http://universaldependencies.org/docs/en/pos/NOUN.html
	//omitting root
	public static void initializeUniversalNouns() {
		universalNounsList.add(PennRelation.nmod);
		universalNounsList.add(PennRelation.dobj);
		universalNounsList.add(PennRelation.compound);
		//universalNounsList.add(PennTreeBankRelations.root);
		universalNounsList.add(PennRelation.nsubj);
		universalNounsList.add(PennRelation.conj);
		universalNounsList.add(PennRelation.nsubjpass);
		universalNounsList.add(PennRelation.parataxis);
		universalNounsList.add(PennRelation.ccomp);
		universalNounsList.add(PennRelation.advcl);
	}
	
	public static void initializeUniversalAdjectives() {
		universalAdjectiveList.add(PennRelation.amod);
	}
	
	public List<Noun> parseNounsAccordingToUniversalDependencyTags(List<TypedDependency> dependencies) {
		String currentRelation;
		List<Noun> nounList = new ArrayList<>();
		for(TypedDependency dependency : dependencies) {
			currentRelation = dependency.reln().getShortName();
			if(universalNounsList.contains(PennRelation.valueOfPennRelation(currentRelation)) && PennPOSTagsLists.isANoun(dependency.dep().tag())) {
				System.out.println(dependency.toString() + " : " + dependency.dep().toString());
				nounList.add(new Noun(dependency));
			}
		}
		return nounList;
	}
	
	public List<Adjective> parseAdjectivesAccordingToUniversalDependencyTags(List<TypedDependency> dependencies, List<Noun> nounList) {
		String currentRelation;
		List<Adjective> adjectiveList = new ArrayList<>();
		for(TypedDependency dependency : dependencies) {
			currentRelation = dependency.reln().getShortName();
			if(universalAdjectiveList.contains(PennRelation.valueOfPennRelation(currentRelation)) && PennPOSTagsLists.isAAdjective(dependency.dep().tag())) {
				System.out.println(dependency.toString() + " : " + dependency.dep().toString());
				adjectiveList.add(new Adjective(dependency, nounList));
			}
		}
		return adjectiveList;
	}
	
	public void mergeCompoundsOfParsedNouns(List<Noun> nounList) {
		for(Noun n: nounList) {
			if(!n.getRelation().equals(PennRelation.compound)) {
				n.mergeAllCompounds(nounList);
			}
		}		
	}	
	
	public List<TypedDependency> getAllNummods(List<TypedDependency> dependencies) {
		List<TypedDependency> numMods = new ArrayList<>();
		String currentRelation;
		for(TypedDependency dependency: dependencies) {
			currentRelation = dependency.reln().getShortName();
			if(PennRelation.valueOfPennRelation(currentRelation).equals(PennRelation.nummod)) {
				System.out.println(dependency.toString() + " : " + dependency.dep().toString() + ":" + dependency.gov().originalText());
				numMods.add(dependency);
			}
		}
		return numMods;
	}
	
	public void mergeNummodsWithParsedNouns(List<TypedDependency> numMods, List<Noun> nounList) {
		String dependencyGoverner;
		for(TypedDependency dependency: numMods) {
			dependencyGoverner = dependency.gov().originalText();
			for(Noun n: nounList) {
				if(dependencyGoverner.equals(n.getDependent()) && dependency.gov().index() == n.getDependentIndex()) {
					n.associateQuantity(dependency.dep().originalText());
				}
			}
			
		}
	}
	
	public void printProcessedNouns(List<Noun> nounList) {
		for(Noun n : nounList) {
			System.out.println(n.toSmartString());
		}
	}
	
	public void printProcessedAdjectives(List<Adjective> adjectiveList) {
		for(Adjective a : adjectiveList) {
			System.out.println(a.toString());
		}
	}
	
	
	
	
	
	
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
		nounInDependentList.add("nmod:poss");
		nounInDependentList.add("nmod:at");
		nounInDependentList.add("nmod:with");
		
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
