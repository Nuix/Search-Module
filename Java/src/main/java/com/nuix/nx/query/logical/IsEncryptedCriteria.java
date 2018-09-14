package com.nuix.nx.query.logical;

public class IsEncryptedCriteria extends LogicalQueryCriteria {
	public IsEncryptedCriteria(){
		this._label = "Is Encrypted";
		this._field = "encrypted";
	}
	
	@Override
	public String toQuery(){
		if(_value == true) return "flag:encrypted";
		else return "-flag:encrypted";
	}
}
