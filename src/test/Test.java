package test;

import java.io.StringReader;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
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
	      String text = "The schools debate team had 11 boys and 45 girls on it.";
	      TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	      LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz" );
	      lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
	      TokenizerFactory tokenizerFactory = PTBTokenizer.factory( new CoreLabelTokenFactory(), "");
	      List wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
	      Tree tree = lp.apply(wordList);
	      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
	      List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	      System.out.println(tdl);	     
	      
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
	      /*for(Map.Entry<WordEntry, List<WordEntry>> entry : types.entrySet()) {
	    	  System.out.println(entry.getKey());
	    	  list = entry.getValue();
	    	  for(WordEntry value: list) {
	    		  System.out.println("----" + value);
	    	  }
	      }*/
	      
	      n.parseNumMods(tdl, types, nouns);
	      for(Map.Entry<WordEntry, List<WordEntry>> entry : types.entrySet()) {
	    	  System.out.println(entry.getKey());
	    	  list = entry.getValue();
	    	  for(WordEntry value: list) {
	    		  System.out.println("----" + value);
	    	  }
	      }
	   }
}
