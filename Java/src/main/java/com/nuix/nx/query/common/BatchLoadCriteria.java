package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class BatchLoadCriteria extends CommonQueryCriteria {
	public BatchLoadCriteria(){
		this._label = "Batch Loads";
		this._field = "batch-load-guid";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getBatchLoads();
	}
}
