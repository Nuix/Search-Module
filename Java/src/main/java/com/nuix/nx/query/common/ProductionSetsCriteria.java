package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class ProductionSetsCriteria extends CommonQueryCriteria {
	public ProductionSetsCriteria(){
		this._label = "Production Sets";
		this._field = "production-set";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getProductionSets();
	}
}
