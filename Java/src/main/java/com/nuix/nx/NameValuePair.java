package com.nuix.nx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used in various places to track a Name/Value pair.
 * Also contains some static helper methods for working with 
 * List<NameValuePair>
 * @author JasonWells
 *
 */
public class NameValuePair implements Comparable<NameValuePair> {
	@SerializedName("name")
	private String _name;
	@SerializedName("value")
	private String _value;
	private transient boolean _checked = false;
	
	/**
	 * Default constructor, name and value will be set to empty strings
	 */
	public NameValuePair(){
		this("","");
	}
	
	/**
	 * Constructor which takes a single value param
	 * and will set name and value to the supplied value.
	 * @param value Value to set name and value to.
	 */
	public NameValuePair(String value){
		this(value,value);
	}
	
	/**
	 * Constructor which takes a name and a value.
	 * @param name The name to assign
	 * @param value The value to assign
	 */
	public NameValuePair(String name,String value){
		_name = name;
		_value = value;
	}
	
	/**
	 * @return This instance's name.
	 */
	public String toString(){
		return _name;
	}
	
	/**
	 * Get the name
	 * @return The name
	 */
	public String getName(){ return _name; }
	/**
	 * Set the name
	 * @param name
	 */
	public void setName(String name){ _name = name; }
	
	/**
	 * Get the value
	 * @return The value
	 */
	public String getValue(){ return _value; }
	/**
	 * Get the value
	 * @param value
	 */
	public void setValue(String value){ _value = value; }
	
	public boolean isChecked(){ return _checked; }
	public void setChecked(boolean value){ _checked = value; }
	
	/**
	 * Given a <code>List<NameValuePair></code> will return a <code>List<String></code>
	 * containing all the names. 
	 * @param pairs Input <code>List<NameValuePair></code>
	 * @return <code>List<String></code> containing all the names.
	 */
	public static List<String> toValueList(List<NameValuePair> pairs){
		List<String> result = new ArrayList<String>();
		for(NameValuePair pair : pairs){
			result.add(pair.getValue());
		}
		return result;
	}
	
	/**
	 * Given a <code>List<NameValuePair></code> will return a <code>List<String></code>
	 * containing all the values. 
	 * @param pairs Input <code>List<NameValuePair></code>
	 * @return <code>List<String></code> containing all the values.
	 */
	public static List<String> toNameList(List<NameValuePair> pairs){
		List<String> result = new ArrayList<String>();
		for(NameValuePair pair : pairs){
			result.add(pair.getName());
		}
		return result;
	}
	
	/**
	 * Given a <code>List<NameValuePair></code> will return a <code>Map<String,String></code>
	 * containing the names and values, with the name being the key.
	 * @param pairs
	 * @return <code>Map<String,String></code> containing the names and values, with the name being the key
	 */
	public static Map<String,String> toNameValueMap(List<NameValuePair> pairs){
		Map<String,String> result = new HashMap<String,String>();
		for(NameValuePair pair : pairs){
			result.put(pair.getName(),pair.getValue());
		}
		return result;
	}
	
	/**
	 * Given a <code>List<NameValuePair></code> will return a <code>Map<String,String></code>
	 * containing the names and values, with the value being the key.
	 * @param pairs
	 * @return <code>Map<String,String></code> containing the names and values, with the value being the key
	 */
	public static Map<String,String> toValueNameMap(List<NameValuePair> pairs){
		Map<String,String> result = new HashMap<String,String>();
		for(NameValuePair pair : pairs){
			result.put(pair.getValue(),pair.getName());
		}
		return result;
	}

	@Override
	public int compareTo(NameValuePair o) {
		return this.getName().compareTo(o.getName());
	}
}
