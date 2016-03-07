package test;

import java.util.List;

import edu.stanford.nlp.trees.TypedDependency;

public class Adjective implements PartsOfSpeech {
	
	private final String dependent;
	private final String adjectiveType;
	private final PennRelation relation;
	private final TypedDependency dependency;
	private final int dependentIndex;
	private final int governerIndex;
	private final String governer;
	private final Noun parentNoun;
	public Adjective (TypedDependency dependency, List<Noun> nounList) {
		this.dependency = dependency;
		final String[] currentDependency = dependency.dep().toString().split("/");
		governer = dependency.gov().toString().split("/")[0];
		dependent = currentDependency[0];
		relation = PennRelation.valueOfPennRelation(dependency.reln().toString().split(":")[0]);
		adjectiveType = currentDependency[1];
		dependentIndex = dependency.dep().index();
		governerIndex = dependency.gov().index();
		parentNoun = getParentNoun(nounList);
	}
	
	private Noun getParentNoun(List<Noun> nounList) {
		Noun parentNoun = null;
		for(Noun n : nounList) {
			if(n.getDependent().equals(governer) && n.getDependentIndex() == governerIndex) {
				parentNoun = n;
				n.mergeAdjective(this);
				break;
			}
		}
		return parentNoun;
	}
	
	@Override
	public String toString() {
		return "Adjective [dependency=" + dependency + ", word=" + dependent
				+ ", adjectiveType=" + adjectiveType + ", index=" + dependentIndex + "]";
	}

	@Override
	public int getGovernerIndex() {
		return governerIndex;
	}

	@Override
	public int getDependentIndex() {
		return dependentIndex;
	}

	@Override
	public String getGoverner() {
		return governer;
	}

	@Override
	public String getDependent() {
		return dependent;
	}
	
	@Override
	public int getQuantity() {
		return 0;
	}
}
