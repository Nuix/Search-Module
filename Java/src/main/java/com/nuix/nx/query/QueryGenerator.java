package com.nuix.nx.query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.nuix.nx.NxSerializationHelper;
import com.nuix.searchmodule.SearchTermInfo;

/**
 * This class represents a Query as a collection of {@link QueryCriteriaBase} objects.  This class is essentially a
 * Nuix Lucene query in object form, allowing for easy saving/loading/manipulation of a query before realizing it
 * as a query string.  This class also contains convenience methods for generating queries containing a scope (the {@link QueryGenerator} instance)
 * and terms and fielded terms.  Since this class implements the {@link QueryCriteria} interface, it too could be
 * used as criteria in another {@link QueryGenerator}.
 * @author JasonWells
 * @see com.nuix.nx.controls.QueryBuilderControl
 */
public class QueryGenerator implements QueryCriteria {
	@SerializedName("query_name")
	private String _queryName = "Query";
	
	@SerializedName("all_criteria")
	private List<QueryCriteria> _allCriteria = new ArrayList<QueryCriteria>();
	
	@SerializedName("join_with_and")
	private boolean _joinWithAnd = true;
	
	public QueryGenerator(){
	}
	
	public QueryGenerator(List<QueryCriteria> initialCriteria){
		_allCriteria = initialCriteria;
	}
	
	/**
	 * Returns the string equivalent query for this instance.
	 */
	@Override
	public String toQuery() {
		ArrayList<String> queries = new ArrayList<String>();
		for(QueryCriteria crit : _allCriteria){
			if(crit.hasQuery()){
				if(crit instanceof QueryGenerator){
					queries.add("("+crit.toQuery()+")");
				}else{
					queries.add(crit.toQuery());
				}
			}
		}
		String joinOperator = (_joinWithAnd ? " AND " : " OR ");
		String queriesJoined = Joiner.on(joinOperator).join(queries);
		return queriesJoined;
	}
	
	public boolean hasQuery(){ return toQuery().trim().length() > 0; }
	
	/**
	 * Removes null, and whitespace elements, trims whitespace from each remaining element.
	 * @param input
	 * @return List with "cleaned" values and empty values removed.
	 */
	public List<String> cleanList(List<String> input){
		List<String> result = new ArrayList<String>();
		for(String value : input){
			if(value == null) continue;
			String trimmed = value.trim();
			if(!trimmed.isEmpty())
				result.add(trimmed);
		}
		return result;
	}
	
	/**
	 * Generates a string query using this instance as a scoping query member.
	 * @param term Input term
	 * @return A query with this instance scoping the term
	 * <p>
	 * <pre><code>
	 * String term = "cat";
	 * QueryGenerator qgen = new QueryGenerator();
	 * qgen.addCriteria(new HasBinaryCriteria());
	 * String query = qgen.scopeTerm(term);
	 * // query => "(cat) AND (has-binary:1)"
	 * </code></pre>
	 */
	public String scopeTerm(String term){
		String scopeQuery = this.toQuery();
		if(scopeQuery.trim().isEmpty())
			return term;
		else if(term.trim().isEmpty())
			return scopeQuery;
		else
			return "("+term+") AND ("+scopeQuery+")";
	}
	
	public String scopeSearchTermInfo(SearchTermInfo term){
		return scopeTerm(term.getTerm());
	}
	
	/**
	 * Generates a string query using this instance as a scoping query member for the
	 * provided terms.
	 * @param terms Input terms
	 * @return A query with this instance scoping the terms
	 * <p>
	 * <pre><code>
	 * List&ltString&gt; terms = new ArrayList<String>();
	 * terms.add("cat");
	 * terms.add("dog");
	 * QueryGenerator qgen = new QueryGenerator();
	 * qgen.addCriteria(new HasBinaryCriteria());
	 * String query = qgen.scopeTerms(terms);
	 * // query => "(cat OR dog) AND (has-binary:1)"
	 * </code></pre>
	 */
	public String scopeTerms(List<String> terms){
		String joinedTerms = Joiner.on(" OR ").join(cleanList(terms));
		return scopeTerm(joinedTerms);
	}
	
	public String scopeSearchTermInfos(List<SearchTermInfo> terms){
		return scopeTerms(SearchTermInfo.toStrings(terms));
	}
	
	/**
	 * Get the list of criteria comprising this instance.
	 * @return All the criteria
	 */
	public List<QueryCriteria> getCriteria(){ return _allCriteria; }
	
	/**
	 * Set the list of criteria comprising this instance.
	 * @param criteria A list of criteria 
	 */
	public void setCriteria(List<QueryCriteria> criteria){ _allCriteria = criteria; }
	
	/**
	 * Clear the criteria comprising this instance.
	 */
	public void clearCriteria(){ _allCriteria = new ArrayList<QueryCriteria>(); }
	
	/**
	 * Add a criteria to this instance.
	 * @param criteria A criteria to be added 
	 */
	public void addCriteria(QueryCriteria criteria){ _allCriteria.add(criteria); }
	
	/**
	 * Remove a criteria from this instance.
	 * @param criteria A criteria to be removed
	 */
	public void removeCriteria(QueryCriteria criteria){ _allCriteria.remove(criteria); }
	
	/**
	 * Convert this instance to a JSON string.
	 * @return JSON String
	 */
	public String toJsonString(){
		Gson gson = NxSerializationHelper.buildGsonForQueryCriteria();
		return gson.toJson(this);
	}
	
	/**
	 * Save this instance to a text file containing this instance as JSON text.
	 * @param file The file to save this instance to
	 * @throws FileNotFoundException
	 */
	public void saveToFile(String file) throws FileNotFoundException{
		PrintWriter jsonFile = new PrintWriter(file);
		jsonFile.println(this.toJsonString());
		jsonFile.close();
	}
	
	/**
	 * Create a new instance based upon the JSON contents of a file.
	 * @param file The JSON file to load from.
	 * @return A new QueryGenerator instance, assuming the JSON parses correctly, etc.
	 * @throws IOException
	 */
	public static QueryGenerator fromFile(String file) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
		String json = Joiner.on("\n").join(lines);
		Gson gson = NxSerializationHelper.buildGsonForQueryCriteria();
		QueryGenerator result = gson.fromJson(json, QueryGenerator.class);	
		return result;
	}
	
	/**
	 * Produces a deep copy of this instance.
	 * @return Copy of this instance
	 */
	public QueryGenerator createClone(){
		Gson gson = NxSerializationHelper.buildGsonForQueryCriteria();
		return gson.fromJson(this.toJsonString(), QueryGenerator.class); 
	}
	
	/**
	 * Gets the name assigned to this query
	 * @return The name assigned to this query.
	 */
	public String getName(){
		return _queryName;
	}
	
	/**
	 * Sets the name assigned to this query
	 * @param name The name to assign to this query.
	 */
	public void setName(String name){
		_queryName = name;
	}
	
	/**
	 * Query criteria will be joined using " AND "
	 */
	public void setJoinWithAnd(){
		_joinWithAnd = true;
	}
	
	/**
	 * Query criteria will be joined using " OR "
	 */
	public void setJoinWithOr(){
		_joinWithAnd = false;
	}
	
	/**
	 * Are the criteria joined with AND?
	 * @return True if " AND " is the join operator
	 */
	public boolean isJoinedWithAnd(){
		return _joinWithAnd;
	}
	
	/**
	 * Are the criteria joined with OR?
	 * @return True if " OR " is the join operator
	 */
	public boolean isJoinedWithOr(){
		return !_joinWithAnd;
	}
}
