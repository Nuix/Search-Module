package com.nuix.nx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nuix.nx.query.QueryCriteria;

public class NxSerializationHelper {
	public static Gson buildGsonForQueryCriteria(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(QueryCriteria.class, new GenericSerializationAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
