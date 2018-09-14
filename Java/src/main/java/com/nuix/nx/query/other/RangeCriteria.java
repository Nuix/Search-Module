package com.nuix.nx.query.other;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.QueryCriteriaBase;

public class RangeCriteria extends QueryCriteriaBase {
	@SerializedName("min_value")
	private String _minimum = "";
	@SerializedName("max_value")
	private String _maximum = "";
	@SerializedName("field")
	private String _field = "";
	
	@Override
	public String toQuery(){
		List<NameValuePair> fields = NuixDataBridge.getCommonIntegerFields();
		fields.addAll(NuixDataBridge.getCustomMetadataFields());
		Map<String,String> choiceMap = NameValuePair.toNameValueMap(fields);
		
		
		String resolvedField = choiceMap.get(_field);
		if(resolvedField == null)
			resolvedField = _field;
		
		if(resolvedField == null || resolvedField.trim().isEmpty()){
			return "";
		}
		
		String min = _minimum;
		if(min == null || min.trim().isEmpty()){
			min = "*";
		}
		
		String max = _maximum;
		if(max == null || max.trim().isEmpty()){
			max = "*";
		}
		
		return resolvedField + ":[" + min + " TO " + max + "]";
	}
	
	public String getMinValue(){
		return _minimum;
	}
	public void setMinValue(String value){
		_minimum = value;
	}
	
	public String getMaxValue(){
		return _maximum;
	}
	public void setMaxValue(String value){
		_maximum = value;
	}
	
	public String getField(){
		return _field;
	}
	public void setField(String value){
		_field = value;
	}
}
