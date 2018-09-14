package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class ItemSetOriginalsCriteria extends CommonQueryCriteria {
	public ItemSetOriginalsCriteria(){
		this._label = "Item Set Originals";
		this._field = "item-set-originals";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getItemSets();
	}
}
