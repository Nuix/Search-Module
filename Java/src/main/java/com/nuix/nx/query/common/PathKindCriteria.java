package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class PathKindCriteria extends CommonQueryCriteria {
	public PathKindCriteria(){
		this._label = "Path Kind";
		this._field = "path-kind";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getKinds();
	}
}
