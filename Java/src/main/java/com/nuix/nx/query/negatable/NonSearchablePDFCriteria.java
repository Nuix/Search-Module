package com.nuix.nx.query.negatable;

public class NonSearchablePDFCriteria extends NegatableQueryCriteria {
	public NonSearchablePDFCriteria(){
		this._label = "Is Non-Searchable PDF";
		this._fragment = "mime-type:application/pdf AND contains-text:0";
	}
}
