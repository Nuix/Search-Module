package com.nuix.nx.query.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.nuix.nx.query.QueryCriteriaBase;

public class ItemSetBatchCriteria extends QueryCriteriaBase {
	public ItemSetBatchCriteria(){
		this._label = "Item Set Batch";
	}
	
	@SerializedName("itemSet")
	private String itemSet = "";
	
	@SerializedName("batch")
	private String batch = "";
	
	@SerializedName("originals")
	private Boolean originals = null;

	@Override
	public String toQuery() {
		List<String> pieces = new ArrayList<String>();
		pieces.add(itemSet);
		if(originals != null){
			if(originals == true){
				pieces.add("originals");
			} else {
				pieces.add("duplicates");
			}
		}
		pieces.add(batch);
		return "item-set-batch:\""+String.join(";",pieces)+"\"";
	}

	public String getItemSet() {
		return itemSet;
	}

	public void setItemSet(String itemSet) {
		this.itemSet = itemSet;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Boolean getOriginals() {
		return originals;
	}

	public void setOriginals(Boolean originals) {
		this.originals = originals;
	}
	
	
}
