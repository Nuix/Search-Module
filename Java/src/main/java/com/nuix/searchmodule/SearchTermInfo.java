package com.nuix.searchmodule;

import java.util.ArrayList;
import java.util.List;

import com.nuix.searchmodule.query.QueryValidationInfo;
import com.nuix.searchmodule.query.QueryValidator;

public class SearchTermInfo {
	public static List<String> toStrings(List<SearchTermInfo> terms){
		List<String> stringTerms = new ArrayList<String>();
		for(SearchTermInfo term : terms){
			stringTerms.add(term.getTerm());
		}
		return stringTerms;
	}
	
	private String term;
	private String shortName;
	private List<QueryValidationInfo> validations = new ArrayList<QueryValidationInfo>();
	
	public SearchTermInfo(){ this("",""); }
	public SearchTermInfo(String t){ this(t,""); }
	public SearchTermInfo(String t,String sn){
		term = t;
		shortName = sn;
		revalidateTerm();
	}
	
	protected void revalidateTerm(){
		validations = QueryValidator.validate(term);
	}
	
	public String getTerm(){ return term; }
	public void setTerm(String t){
		term = t;
		revalidateTerm();
	}

	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public List<QueryValidationInfo> getValidations() {
		return validations;
	}
}
