package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class ItemSetDuplicatesCriteria extends CommonQueryCriteria {
	public ItemSetDuplicatesCriteria(){
		this._label = "Item Set Duplicates";
		this._field = "item-set-duplicates";
		this._isSingular = false;
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getItemSets();
	}
}
