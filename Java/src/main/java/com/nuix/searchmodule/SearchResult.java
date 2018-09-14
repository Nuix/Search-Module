package com.nuix.searchmodule;

import java.util.Collection;
import java.util.HashMap;

public class SearchResult {
	private HashMap<String,Object> otherData = new HashMap<String,Object>();
	private String term;
	private String query;
	private String shortName;
	private Collection<nuix.Item> hits;
	private Collection<nuix.Item> topLevelHits;
	private Collection<nuix.Item> familyHits;
	private Collection<nuix.Item> uniqueHits;
	private Collection<nuix.Item> uniqueTopLevel;
	private Collection<nuix.Item> uniqueFamily;
	
	private boolean hadError = false;
	private String errorMessage = "";
	
	public SearchResult(String term, String query){
		this.term = term;
		this.query = query;
	}
	
	public SearchResult(String term){
		this(term,term);
	}
	
	public void put(String key, Object value){
		otherData.put(key,value);
	}
	
	public Object get(String key){
		return otherData.get(key);
	}
	
	public Collection<nuix.Item> getHits() {
		return hits;
	}
	public void setHits(Collection<nuix.Item> hits) {
		this.hits = hits;
	}
	public Collection<nuix.Item> getTopLevelHits() {
		return topLevelHits;
	}
	public void setTopLevelHits(Collection<nuix.Item> topLevelHits) {
		this.topLevelHits = topLevelHits;
	}
	public Collection<nuix.Item> getFamilyHits() {
		return familyHits;
	}
	public void setFamilyHits(Collection<nuix.Item> familyHits) {
		this.familyHits = familyHits;
	}
	public Collection<nuix.Item> getUniqueHits() {
		return uniqueHits;
	}
	public void setUniqueHits(Collection<nuix.Item> uniqueHits) {
		this.uniqueHits = uniqueHits;
	}
	public Collection<nuix.Item> getUniqueTopLevel() {
		return uniqueTopLevel;
	}
	public void setUniqueTopLevel(Collection<nuix.Item> uniqueTopLevel) {
		this.uniqueTopLevel = uniqueTopLevel;
	}
	public Collection<nuix.Item> getUniqueFamily() {
		return uniqueFamily;
	}
	public void setUniqueFamily(Collection<nuix.Item> uniqueFamily) {
		this.uniqueFamily = uniqueFamily;
	}

	public String getTerm() {
		return term;
	}

	public String getQuery() {
		return query;
	}
	
	public boolean getHadError(){ return hadError; }
	public void setHadError(boolean value) { hadError = value; }
	public String getErrorMessage() { return errorMessage; }
	public void setErrorMessage(String value) { errorMessage = value; }

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
