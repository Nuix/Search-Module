package com.nuix.nx.query.negatable;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.query.logical.LogicalQueryCriteria;

public class NegatableQueryCriteria extends LogicalQueryCriteria {
	@SerializedName("fragment")
	protected transient String _fragment = "";
	
	@Override
	public String toQuery(){
		return (_value ? "+" : "-") + "(" + _fragment + ")";
	}
	
	public void setValue(boolean value){ _value = value; }
	public boolean getValue(){ return _value; }
}
