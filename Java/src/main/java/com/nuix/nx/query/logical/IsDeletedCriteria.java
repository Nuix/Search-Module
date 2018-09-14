package com.nuix.nx.query.logical;

public class IsDeletedCriteria extends LogicalQueryCriteria {
	public IsDeletedCriteria(){
		this._label = "Is Encrypted";
		this._field = "encrypted";
	}
	
	@Override
	public String toQuery(){
		if(_value == true) return "flag:deleted";
		else return "-flag:deleted";
	}
}
