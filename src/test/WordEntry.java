package test;

import java.util.ArrayList;
import java.util.List;

public class WordEntry {	
	
	private Type wordType;
	private String word;
	private List<Integer> indices;
	private int quantity;
	public WordEntry(String word, Type wordType) {
		this.word = word;
		this.wordType = wordType;
		indices = new ArrayList<>();
	}

	public Type getWordType() {
		return wordType;
	}

	public String getWord() {
		return word;
	}
	
	public void prependWord(String word, int index) {
		this.word = word + " " + this.word;
		if(!indices.contains(index)) {
			indices.add(0, index);
		}
	}

	public List<Integer> getIndices() {
		return indices;
	}
	
	public void addIndex(int index) {
		if(!indices.contains(index))
			indices.add(index);
	}
	
	public void associateQuantity(int q) {
		this.quantity = q;
	}
	
	@Override
	public String toString() {
		String str = "WordEntry [wordType=" + wordType + ", quantity = " + quantity + " word=" + word
				+ ", indices= ";
		for(Integer index : indices) {
			str += index + " ";
		}
		str += "]";
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		result = prime * result
				+ ((wordType == null) ? 0 : wordType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordEntry other = (WordEntry) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (wordType != other.wordType)
			return false;
		return true;
	}
	
	
}
