package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class EvidenceCriteria extends CommonQueryCriteria {
	public EvidenceCriteria(){
		this._label = "Evidence";
		this._field = "path-guid";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getEvidenceInfo();
	}
}
