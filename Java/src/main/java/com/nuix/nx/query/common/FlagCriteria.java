package com.nuix.nx.query.common;

import java.util.List;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;

public class FlagCriteria extends CommonQueryCriteria {
	public FlagCriteria(){
		this._label = "Flags";
		this._field = "flag";
		this._isSingular = false;
	}
	
	public FlagCriteria(List<String> flags){
		this();
		for(String flag : flags){
			this.addValue(new NameValuePair(flag));
		}
	}
	
	@Override
	public List<NameValuePair> getChoices(){
		return NuixDataBridge.getFlags();
	}
}
