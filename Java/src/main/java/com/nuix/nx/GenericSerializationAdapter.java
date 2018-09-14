package com.nuix.nx;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nuix.nx.query.QueryCriteria;

/**
 * This class provides a customized JSON serialization adapter for GSON.  It enables serialization to
 * include information about the specific derived class of a given object so that
 * deserialization will create the appropriate types. 
 * @author JasonWells
 *
 */
public class GenericSerializationAdapter implements JsonSerializer<QueryCriteria>, JsonDeserializer<QueryCriteria> {
	private static final String CLASSNAME = "CLASSNAME";
	private static final String INSTANCE  = "INSTANCE";
	
	@Override
	public QueryCriteria deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject =  json.getAsJsonObject();
	    JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
	    String className = prim.getAsString();

	    Class<?> klass = null;
	    try {
	        klass = Class.forName(className);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        throw new JsonParseException(e.getMessage());
	    }
	    return context.deserialize(jsonObject.get(INSTANCE), klass);
	}

	@Override
	public JsonElement serialize(QueryCriteria src, Type type,
			JsonSerializationContext context) {
		JsonObject retValue = new JsonObject();
	    String className = src.getClass().getCanonicalName();
	    retValue.addProperty(CLASSNAME, className);
	    JsonElement elem = context.serialize(src); 
	    retValue.add(INSTANCE, elem);
	    return retValue;
	}
}
