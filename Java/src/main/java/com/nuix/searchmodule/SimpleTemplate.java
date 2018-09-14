package com.nuix.searchmodule;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a very basic template engine.  Values such as "{name}" in the template string
 * will be replaced with the place holder key of "name" (case insensitive).
 * @author JasonWells
 *
 */
public class SimpleTemplate {
	private String templateString;
	private HashMap<String,Object> placeholders = new HashMap<String,Object>();
	
	//Constructors
	/**
	 * Basic constructor.  Creates instance with empty template string and no default
	 * place holders.
	 */
	public SimpleTemplate(){
		this("",null);
	}
	
	/**
	 * Creates an instance initialized with the provided template string.
	 * @param template The template string to use.
	 */
	public SimpleTemplate(String template){
		this(template,null);
	}
	
	/**
	 * Creates an instance initialized with the provided template string and place holders.
	 * @param template The template string to use.
	 * @param initialPlaceholders A Map of key/value pairs which will be the initial set of
	 * place holders.
	 */
	public SimpleTemplate(String template, Map<String,Object> initialPlaceholders){
		if(initialPlaceholders != null){
			for (Entry<String, Object> entry : initialPlaceholders.entrySet())
			{
				placeholders.put(entry.getKey().toLowerCase(),entry.getValue());
			}
		}
	}

	//Get/Set
	/**
	 * The current template string used by this instance.
	 * @return The template string in use.
	 */
	public String getTemplateString() {
		return templateString;
	}

	/**
	 * Set the template string used by this instance.
	 * @param templateString The new template string for this instance to use.
	 */
	public void setTemplateString(String templateString) {
		this.templateString = templateString;
	}
	
	/**
	 * Resolves a template string using the provided place holders.
	 * @param input The template string.
	 * @param placeholderData A Map of key/value pairs representing the place holders.
	 * @return A string with the place holders substituted.
	 */
	public static String resolvePlaceholders(String input, Map<String,Object> placeholderData){
		String result = input;
		for (Entry<String, Object> entry : placeholderData.entrySet())
		{
			Object value = entry.getValue();
			Pattern p = Pattern.compile("\\{"+entry.getKey()+"\\}", Pattern.CASE_INSENSITIVE);
			result = p.matcher(result).replaceAll(Matcher.quoteReplacement((String)value));
		}
		return result;
	}
	
	//Public Methods
	/**
	 * Add a key/value pair to the place holders stored by this instance.
	 * @param key The key or place holder name to store.  { and } should NOT be included.
	 * @param value The value this place holder should resolve to.
	 */
	public void put(String key, Object value){
		placeholders.put(key.toLowerCase(), value);
	}
	
	/**
	 * Get the value of a place holder stored in this instance for a given key.
	 * @param key The key of interest.
	 * @return The value of the provided key, or null if there is no value.
	 */
	public Object get(String key){
		return placeholders.get(key.toLowerCase());
	}
	
	/**
	 * Resolve this template to a final value.  This overload allows you to specify an
	 * additional Map of place holders which will only be used this once.  The provided
	 * additional place holders are not stored in this instance.
	 * @param additionalPlaceholders Place holder key value pairs to use in addition when resolving template this one time.
	 * @return A string with the place holders substituted.
	 */
	public String resolveTemplate(Map<String,Object> additionalPlaceholders){
		String result = templateString;
		result = resolvePlaceholders(result, placeholders);
		if(additionalPlaceholders != null){
			result = resolvePlaceholders(result, additionalPlaceholders);
		}
		return result;
	}
	
	/**
	 * Resolved this instance's template with any place holders this instance has stored.
	 * @return A string with the place holders substituted.
	 */
	public String resolveTemplate(){
		return resolveTemplate(null);
	}
}
