package com.nuix.nx.query.other;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.QueryCriteriaBase;

public class DateRangeCriteria extends QueryCriteriaBase {
	@SerializedName("min_date")
	private Date _minimum = null;
	@SerializedName("max_date")
	private Date _maximum = null;
	@SerializedName("field")
	private String _field = "";
	
	@Override
	public String toQuery(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		List<NameValuePair> fields = NuixDataBridge.getCommonDateFields();
		fields.addAll(NuixDataBridge.getCustomMetadataFields());
		Map<String,String> choiceMap = NameValuePair.toNameValueMap(fields);
		
		String resolvedField = choiceMap.get(_field);
		if(resolvedField == null)
			resolvedField = _field;
		
		if(resolvedField == null || resolvedField.trim().isEmpty()){
			return "";
		}
		
		if(_minimum == null && _maximum == null){
			return "";
		}
		
		String min = "*";
		String max = "*";
		
		if(_minimum != null){
			min = dateFormat.format(_minimum);
		}
		
		if(_maximum != null){
			max = dateFormat.format(_maximum);
		}
		
		return resolvedField + ":[" + min + " TO " + max + "]";
	}
	
	public void setMinDate(Date value){ _minimum = value; }
	public Date getMinDate(){ return _minimum; }
	
	public void setMaxDate(Date value){ _maximum = value; }
	public Date getMaxDate(){ return _maximum; }
	
	public void setField(String value){ _field = value; }
	public String getField(){ return _field; }
}
