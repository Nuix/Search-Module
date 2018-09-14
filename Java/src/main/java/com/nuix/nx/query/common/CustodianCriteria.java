package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class CustodianCriteria extends CommonQueryCriteria {
	public CustodianCriteria(){
		this._label = "Custodians";
		this._field = "custodian";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getCustodians();
	}
}
