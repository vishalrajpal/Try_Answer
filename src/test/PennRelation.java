package test;

public enum PennRelation {
	det,
	nummod,
	cc,
	penncase,
	advmod,
	mark,
	dep,
	xcomp,
	aux,
	acl,
	expl,
	appos,
	cop,
	//Adjective
	amod,
	compound,
	//nouns
	nmod,
	dobj,	
	root,
	nsubj,
	conj,
	nsubjpass,
	parataxis,
	ccomp,
	advcl;
	
	public static PennRelation valueOfPennRelation(String str) {
		str = str.split(":")[0];
		switch(str) {
		case "case":
			str = "penncase";
			break;
		}
		return PennRelation.valueOf(str);		
	}
}
