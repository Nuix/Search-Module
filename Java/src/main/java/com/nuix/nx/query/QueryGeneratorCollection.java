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

public class QueryGeneratorCollection {
	@SerializedName("generators")
	private List<QueryGenerator> _generators = new ArrayList<QueryGenerator>();
	
	/**
	 * Get a list of the {@link QueryGenerator} instances held by this instance.
	 * @return A list of {@link QueryGenerator} instances
	 */
	public List<QueryGenerator> getGenerators(){
		return _generators;
	}
	
	/**
	 * Set the list of {@link QueryGenerator} instances held by this instance.
	 * @param generators The new list of {@link QueryGenerator} instances.
	 */
	public void setGenerators(List<QueryGenerator> generators){
		_generators = generators;
	}
	
	/**
	 * Convert this instance to a JSON string.
	 * @return JSON String
	 */
	public String toJson(){
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
		jsonFile.println(this.toJson());
		jsonFile.close();
	}
	
	/**
	 * Create a new instance based upon the JSON contents of a file.
	 * @param file The JSON file to load from.
	 * @return A new QueryGenerator instance, assuming the JSON parses correctly, etc.
	 * @throws IOException
	 */
	public static QueryGeneratorCollection fromFile(String file) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
		String json = Joiner.on("\n").join(lines);
		Gson gson = NxSerializationHelper.buildGsonForQueryCriteria();
		return gson.fromJson(json, QueryGeneratorCollection.class);
	}
}
