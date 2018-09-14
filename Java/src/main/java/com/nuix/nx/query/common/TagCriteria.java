package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class TagCriteria extends CommonQueryCriteria {
	public TagCriteria(){
		this._label = "Tags";
		this._field = "tag";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getTags();
	}
}
