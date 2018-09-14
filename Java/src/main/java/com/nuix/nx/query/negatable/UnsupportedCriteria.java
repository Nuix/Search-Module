package com.nuix.nx.query.negatable;

public class UnsupportedCriteria extends NegatableQueryCriteria {
	public UnsupportedCriteria(){
		this._label = "Is Unsupported";
		this._fragment = "encrypted:0 AND has-embedded-data:0 AND ( ( has-text:0 AND has-image:0 AND NOT flag:not_processed AND NOT kind:multimedia AND NOT mime-type:application/vnd.ms-shortcut AND NOT mime-type:application/vnd.ms-exchange-stm ) OR mime-type:application/vnd.lotus-notes )";
	}
}
