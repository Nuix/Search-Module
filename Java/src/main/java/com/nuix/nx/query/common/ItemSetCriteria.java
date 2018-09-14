package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class ItemSetCriteria extends CommonQueryCriteria {
	public ItemSetCriteria(){
		this._label = "Item Sets";
		this._field = "item-set";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getItemSets();
	}
}
