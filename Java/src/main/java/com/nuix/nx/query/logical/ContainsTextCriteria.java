package com.nuix.nx.query.logical;

public class ContainsTextCriteria extends LogicalQueryCriteria {
	public ContainsTextCriteria(){
		this._label = "Contains Text";
		this._field = "contains-text";
	}
	
	@Override
	public String toQuery(){
		if(_value == true) return "content:*";
		else return "-content:*";
	}
}
