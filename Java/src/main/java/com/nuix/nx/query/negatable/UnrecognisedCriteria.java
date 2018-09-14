package com.nuix.nx.query.negatable;

public class UnrecognisedCriteria extends NegatableQueryCriteria {
	public UnrecognisedCriteria(){
		this._label = "Is Unrecognised";
		this._fragment = "mime-type:application/octet-stream";
	}
}
