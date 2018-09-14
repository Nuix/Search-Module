package com.nuix.nx.query.negatable;

public class CorruptedCriteria extends NegatableQueryCriteria {
	public CorruptedCriteria(){
		this._label = "Is Corrupted";
		this._fragment = "properties:FailureDetail AND NOT encrypted:1";
	}
}
