package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Test {
	
	public static void main(String []args) throws Exception {
//		System.out.print(Utilities.textToInt("one hundred thousand"));
		String text = "Lana picked 36 tulips and 37 roses to make flower bouquets.";
		File trainingFile = new File("training_data.txt");		
		FileReader fileReader = new FileReader(trainingFile);
		Scanner scanner = new Scanner(trainingFile);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz" );
	    lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
	    TokenizerFactory tokenizerFactory = PTBTokenizer.factory( new CoreLabelTokenFactory(), "");
	    List wordList;
	    Tree tree;
	    GrammaticalStructure gs;
	    List<TypedDependency> tdl;
	    int i = 1;
	    PennPOSTagsLists.initializeTagLists();
		SmartParser.initializeUniversalNouns();
		SmartParser.initializeUniversalAdjectives();
		while(scanner.hasNextLine()) {
			text = scanner.nextLine();
			wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
			tree = lp.apply(wordList);
			gs = gsf.newGrammaticalStructure(tree);
			tdl = gs.typedDependenciesCCprocessed();
		    System.out.println(i++ + " " + text);
		    System.out.println(tdl);		    
			SmartParser sParser = new SmartParser();
			List<Noun> nounList = sParser.parseNounsAccordingToUniversalDependencyTags(tdl);
			sParser.mergeCompoundsOfParsedNouns(nounList);
			System.out.println();
			
			List<Adjective> adjectiveList = sParser.parseAdjectivesAccordingToUniversalDependencyTags(tdl, nounList);
			System.out.println();
			
			List<TypedDependency> numMods = sParser.getAllNummods(tdl);
			System.out.println();
			
			sParser.mergeNummodsWithParsedNouns(numMods, nounList);
			sParser.printProcessedNouns(nounList);	
			System.out.println();

		}
		
		
		
		
		
		
		/*String text = "At Billy's Restaurant a group with 2 adults and 5 children came in to eat.";
	      TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	      LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz" );
	      //lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
	      TokenizerFactory tokenizerFactory = PTBTokenizer.factory( new CoreLabelTokenFactory(), "");
	      List wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
	      Tree tree = lp.apply(wordList);
	      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
	      List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	      System.out.println(tdl);	     
	      //Collection<TypedDependency> tdl1 = gs.allTypedDependencies();
	      //System.out.println(tdl1);
	      Nouns.initializeNounLabels();
	      Nouns n = new Nouns();
	      
	      Map<String, WordEntry> nouns = n.parseNouns(tdl);
	      for(Map.Entry<String,WordEntry> noun : nouns.entrySet()) {
	    	  System.out.print(noun.getKey() + ":");
	    	  for(Integer index : noun.getValue().getIndices()) {
	    		  System.out.print(index + " ");
	    	  }
	    	  System.out.println();
	      }
	      System.out.println();
	      Map<WordEntry, List<WordEntry>> types = n.parseTypes(tdl, nouns);
	      List<WordEntry> list;
	      //for(Map.Entry<WordEntry, List<WordEntry>> entry : types.entrySet()) {
	    	//  System.out.println(entry.getKey());
	    	  //list = entry.getValue();
	    	  //for(WordEntry value: list) {
	    		//  System.out.println("----" + value);
	    	 // }
	      //}
	      
	      n.parseNumMods(tdl, types, nouns);
	      for(Map.Entry<WordEntry, List<WordEntry>> entry : types.entrySet()) {
	    	  System.out.println(entry.getKey());
	    	  list = entry.getValue();
	    	  for(WordEntry value: list) {
	    		  System.out.println("----" + value);
	    	  }
	      }*/
	   }
}
