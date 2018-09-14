package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class KindCriteria extends CommonQueryCriteria {
	public KindCriteria(){
		this._label = "Kinds";
		this._field = "kind";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getKinds();
	}
}
