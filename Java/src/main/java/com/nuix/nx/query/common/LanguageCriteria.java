package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class LanguageCriteria extends CommonQueryCriteria {
	public LanguageCriteria(){
		this._label = "Languages";
		this._field = "lang";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getLanguages();
	}
}
