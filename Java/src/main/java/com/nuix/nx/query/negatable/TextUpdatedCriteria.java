package com.nuix.nx.query.negatable;

public class TextUpdatedCriteria extends NegatableQueryCriteria {
	public TextUpdatedCriteria(){
		this._label = "Has Updated Text";
		this._fragment = "previous-version-docid:*";
	}
	
	@Override
	public String toQuery(){
		if(_value == true) return "modifications:text_updated";
		else return "-modifications:text_updated";
	}
}
