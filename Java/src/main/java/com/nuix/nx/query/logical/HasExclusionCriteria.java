package com.nuix.nx.query.logical;

public class HasExclusionCriteria extends LogicalQueryCriteria {
	public HasExclusionCriteria(){
		this._label = "Has Exclusion";
		this._field = "has-exclusion";
	}
	
	public HasExclusionCriteria(boolean value){
		this();
		this.setValue(value);
	}
}
