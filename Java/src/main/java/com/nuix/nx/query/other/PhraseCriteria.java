package com.nuix.nx.query.other;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.gson.annotations.SerializedName;
import com.nuix.nx.query.QueryCriteriaBase;

public class PhraseCriteria extends QueryCriteriaBase {
	@SerializedName("fields")
	private List<String> _fields = new ArrayList<String>();
	@SerializedName("phrase")
	private String _phrase = "";
	@SerializedName("is_negated")
	private boolean _negated = false;
	@SerializedName("join_or")
	private boolean _joinOr = true;
	
	@Override
	public String toQuery(){
		String negationOperator = (_negated ? "-" : "+");
		if(_phrase == null || _phrase.trim().isEmpty()){
			return "";
		}
		else if(_fields.size() < 1){
			return negationOperator + "(" + _phrase + ")";
		}
		else{
			List<String> fieldedPhrases = new ArrayList<String>();
			for(String field : _fields){
				String fieldedPhrase = field + ":(" + _phrase + ")";
				fieldedPhrases.add(fieldedPhrase);
			}
			String joinOperator = (_joinOr ? " OR " : " AND ");
			String fieldedPhrasesJoined = Joiner.on(joinOperator).join(fieldedPhrases);
			return negationOperator + "(" + fieldedPhrasesJoined + ")";
		}
	}
	
	public List<String> getFields(){
		return _fields;
	}
	public void setFields(List<String> value){
		_fields = value;
	}
	
	public String getPhrase(){
		return _phrase;
	}
	public void setPhrase(String value){
		_phrase = value;
	}
	
	public boolean getJoinOr(){
		return _joinOr;
	}
	public void setJoinOr(boolean value){
		_joinOr = value;
	}
	
	public boolean isNegated(){
		return _negated;
	}
	public void setNegated(boolean value){
		_negated = value;
	}
}
