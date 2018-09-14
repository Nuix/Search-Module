package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class DigestListCriteria extends CommonQueryCriteria {
	public DigestListCriteria(){
		this._label = "Digest Lists";
		this._field = "digest-list";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getDigestLists();
	}
}
