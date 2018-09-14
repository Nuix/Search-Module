package com.nuix.nx.query.other;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.query.QueryCriteriaBase;

public class LiteralCriteria extends QueryCriteriaBase {
	@SerializedName("query")
	private String query = "";

	@Override
	public String toQuery() {
		return query;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
