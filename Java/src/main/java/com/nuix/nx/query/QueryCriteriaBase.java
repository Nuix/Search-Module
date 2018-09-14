package com.nuix.nx.query;

/**
 * Provides a common basic implementation used by the various query criteria classes.
 * @author JasonWells
 *
 */
public class QueryCriteriaBase implements QueryCriteria{
	protected transient String _label = "Criteria";
	
	public String toQuery(){ return ""; }
	public boolean hasQuery(){ return toQuery().trim().length() > 0; }
	public static String quoteAndEscape(String input){
		return "\"" + input.replaceAll("\"", "\\\"") + "\"";
	}
	
	public String getLabel(){ return _label; }
}
