package com.nuix.nx.query.negatable;

public class CorruptedContainerCriteria extends NegatableQueryCriteria {
	public CorruptedContainerCriteria(){
		this._label = "Is Corrupted Container";
		this._fragment = "properties:FailureDetail AND encrypted:0 AND has-text:0 AND ( has-embedded-data:1 OR kind:container OR kind:database )";
	}
}
