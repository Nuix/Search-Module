package com.nuix.nx.query.negatable;

public class UnsupportedContainerCriteria extends NegatableQueryCriteria {
	public UnsupportedContainerCriteria(){
		this._label = "Is Unsupported Container";
		this._fragment = "kind:( container OR database ) AND encrypted:0 AND has-embedded-data:0 AND NOT flag:partially_processed AND NOT flag:not_processed AND NOT properties:FailureDetail";
	}
}
