package com.nuix.nx.query.logical;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.query.QueryCriteriaBase;

public class LogicalQueryCriteria extends QueryCriteriaBase {
	@SerializedName("value")
	protected boolean _value = true;
	@SerializedName("field")
	protected transient String _field = "";
	
	@Override
	public String toQuery(){
		return _field + ":" + (_value ? "1" : "0");
	}
	
	public void setValue(boolean value){ _value = value; }
	public boolean getValue(){ return _value; }
}
