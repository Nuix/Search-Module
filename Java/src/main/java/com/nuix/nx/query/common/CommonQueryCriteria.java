package com.nuix.nx.query.common;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.gson.annotations.SerializedName;
import com.nuix.nx.NameValuePair;
import com.nuix.nx.query.QueryCriteriaBase;

public class CommonQueryCriteria extends QueryCriteriaBase {
	
	protected transient String _field = "";
	protected transient boolean _isSingular = false;
	@SerializedName("values")
	private List<NameValuePair> _values = new ArrayList<NameValuePair>();
	@SerializedName("criteria_type")
	private MultiValueCriteriaType _criteriaType = MultiValueCriteriaType.HAVINGANY;
	
	@Override
	public String toQuery(){
		if(_values == null || _values.size() < 1){
			return "";
		}
		else{
			String joinOperator = " AND ";
			if(_criteriaType == MultiValueCriteriaType.HAVINGANY || _criteriaType == MultiValueCriteriaType.NOTHAVINGANY)
				joinOperator = " OR ";
			
			String requirementOperator = "+";
			if(_criteriaType == MultiValueCriteriaType.NOTHAVINGALL || _criteriaType == MultiValueCriteriaType.NOTHAVINGANY)
				requirementOperator = "-";
			
			List<String> values = new ArrayList<String>();
			for(NameValuePair pair : getValidValues()){
				values.add(QueryCriteriaBase.quoteAndEscape(pair.getValue()));
			}
			
			String valuesJoined = Joiner.on(joinOperator).join(values);
			return requirementOperator + _field + ":(" + valuesJoined + ")";
		}
	}
	
	public List<NameValuePair> getChoices(){
		return new ArrayList<NameValuePair>();
	}
	
	public MultiValueCriteriaType getCriteriaType(){
		return _criteriaType;
	}
	
	public void setCriteriaType(MultiValueCriteriaType type){
		_criteriaType = type;
	}
	
	public List<NameValuePair> getValues(){
		return _values;
	}
	
	/**
	 * Only returns those values which are actually present in the current case.
	 * @return NameValuePair collection containing only those NaveValuePair items
	 * which name are present in the current case.
	 */
	public List<NameValuePair> getValidValues(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		List<String> validNames = NameValuePair.toNameList(getChoices());
		for(NameValuePair p : this._values){
			if(validNames.contains(p.getName())){
				result.add(p);
			}
		}
		return result;
	}
	
	public void setValues(List<NameValuePair> values){
		_values = values;
	}
	
	public void addValue(NameValuePair value){
		_values.add(value);
	}
	
	public boolean isSingular(){
		return _isSingular;
	}
}
