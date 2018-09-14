package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class ExclusionCriteria extends CommonQueryCriteria {
	public ExclusionCriteria(){
		this._label = "Exclusions";
		this._field = "exclusion";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getExclusions();
	}
}
