package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class MimeTypeCriteria extends CommonQueryCriteria {
	public MimeTypeCriteria(){
		this._label = "Mime Types";
		this._field = "mime-type";
		this._isSingular = true;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getMimeTypes();
	}
}
