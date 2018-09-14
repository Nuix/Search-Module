package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class OriginalExtensionCriteria extends CommonQueryCriteria {
	public OriginalExtensionCriteria(){
		this._label = "Original Extension";
		this._field = "file-extension";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getOriginalExtensions();
	}
}
