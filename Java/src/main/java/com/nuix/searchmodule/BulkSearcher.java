package com.nuix.searchmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nuix.Item;
import nuix.ItemUtility;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.QueryGenerator;

public class BulkSearcher {
	private boolean collectTopLevelHits = true;
	private boolean collectFamilyHits = true;
	private boolean collectUniqueHits = true;
	private boolean collectUniqueTopLevel = true;
	private boolean collectUniqueFamily = true;
	private boolean removeExcludedItems = false;
	
	private Period hitsTotalTime = new Period();
	private Period topLevelHitsTotalTime = new Period();
	private Period familyHitsTotalTime = new Period();
	private Period uniqueHitsTotalTime = new Period();
	private Period uniqueTopLevelTotalTime = new Period();
	private Period uniqueFamilyTotalTime = new Period();
	
	private boolean removeFamilyImmaterial = true;
	private boolean removeUniqueFamilyImmaterial = true;
	private DateTime lastProgress = new DateTime();
	
	private ProgressListener progressWatcher;
	private static Logger logger = Logger.getLogger("SM3BulkSearcher");
	
	protected static PeriodFormatter timingsFormatter = new PeriodFormatterBuilder()
		.appendDays()
	    .appendSuffix(" day", " days")
	    .appendSeparator(" ")
	    .printZeroIfSupported()
	    .minimumPrintedDigits(2)
	    .appendHours()
	    .appendSeparator(":")
	    .appendMinutes()
	    .printZeroIfSupported()
	    .minimumPrintedDigits(2)
	    .appendSeparator(":")
	    .appendSeconds()
	    .minimumPrintedDigits(2)
	    .toFormatter();
	
	//This class simplifies unique calculations
	@SuppressWarnings("serial")
	class ItemInstanceCounter extends HashMap<String,Integer>{
		public void increment(String guidKey){
			Integer currentValue = 0;
			if(super.containsKey(guidKey)){
				currentValue = super.get(guidKey);
			}
			currentValue++;
			super.put(guidKey, currentValue);
		}
		
		public void increment(Item item){
			increment(item.getGuid());
		}
		
		public int occurrences(Item item){
			return super.get(item.getGuid()).intValue();
		}
	}
	
	protected void fireProgressUpdated(String mainMessage, String subMessage, int currentProgress, int total){
		if(progressWatcher != null){
			if(new Period(lastProgress,new DateTime()).toStandardDuration().getMillis() > 250){
				progressWatcher.whenProgressUpdated(mainMessage, subMessage, currentProgress, total);
				lastProgress = new DateTime();
			}
		}
	}
	
	public void setRemoveExcluded(boolean value){
		removeExcludedItems = value;
	}
	
	public boolean getRemoveExcluded(){
		return removeExcludedItems;
	}
	
	public void setSettings(List<CategorySettings> settings){
		//TODO: Remove usage of magic strings held over from Ruby code
		for(CategorySettings setting : settings){
			switch(setting.getCategoryKey()){
				case "hits": break;
				case "top_level_hits":
					collectTopLevelHits = setting.getReportCount();
					break;
				case "family_hits":
					collectFamilyHits = setting.getReportCount();
					break;
				case "unique_hits":
					collectUniqueHits = setting.getReportCount();
					break;
				case "unique_top_level":
					collectUniqueTopLevel = setting.getReportCount();
					break;
				case "unique_family":
					collectUniqueFamily = setting.getReportCount();
					break;
			}
		}
	}
	
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms){
		return performSearching(nuixCase,searchTerms,null,null);
	}
	
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms, QueryGenerator scope){
		return performSearching(nuixCase,searchTerms,scope,null);
	}
	
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms, QueryGenerator scope, List<String> fields){
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		generateFinalQueries(searchTerms, scope, results);
		
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		
		int total = results.size();
		
		//Collect Hits (always collected)
		collectHitData(nuixCase, results, fields, total);
		
		//Collect Top Level Hits (if requested)
		collectTopLevelHitData(results, iutil, total);
		
		//Collect Family Hits (if requested)
		collectFamilyHitData(results, iutil, total);
		
		//Collection Unique Hits
		//Note that requesting "Unique Top Level" or "Unique Reviewable" will also invoke this
		//since the hits are needed for the other calculations
		collectUniqueData(results, iutil, total);
		
		return results;
	}
	
	protected Collection<Item> rejectExcludedItems(Collection<Item> items){
		return items.stream().filter(i -> !i.isExcluded()).collect(Collectors.toList());
	}

	protected void generateFinalQueries(List<SearchTermInfo> searchTerms, QueryGenerator scope, ArrayList<SearchResult> results) {
		for(SearchTermInfo info : searchTerms){
			SearchResult r;
			if(scope != null){
				//Query needs to be scoped
				r = new SearchResult(info.getTerm(),scope.scopeTerm(info.getTerm()));
			}else{
				//Term is the query
				r = new SearchResult(info.getTerm());
			}
			r.setShortName(info.getShortName());
			
			results.add(r);
		}
	}

	protected void collectUniqueData(ArrayList<SearchResult> results, ItemUtility iutil, int total) {
		if(collectUniqueHits || collectUniqueTopLevel || collectUniqueFamily){
			collectUniqueHitData(results, total);
			
			//Collect Unique Top Level
			//Note that requesting "Unique Reviewable" will also invoke this
			//since unique top level is needed
			if(collectUniqueTopLevel || collectUniqueFamily){
				//Resolve to top level, temporarily storing in search result
				//Then perform instance counting
				collectUniqueTopLevelData(results, iutil, total);
				
				//Collect Unique Family (if requested)
				collectUniqueFamilyData(results, iutil, total);
			}
		}
	}

	protected void collectUniqueFamilyData(ArrayList<SearchResult> results,	ItemUtility iutil, int total) {
		DateTime collectStart = new DateTime();
		if(collectUniqueFamily){
			for(int current = 0; current < results.size();current++){
				SearchResult r = results.get(current);
				if(r.getHadError()) continue;
				fireProgressUpdated("Resolving Unique Reviewable", current+"/"+total, current, total);
				Collection<Item> items = iutil.findFamilies(r.getUniqueTopLevel());
				if(removeExcludedItems){
					r.setUniqueFamily(rejectExcludedItems(items));
				}else{
					r.setUniqueFamily(items);
				}
			}
			
			//Remove immaterial from unique families if requested
			if(removeUniqueFamilyImmaterial){
				for(int current = 0; current < results.size();current++){
					SearchResult r = results.get(current);
					if(r.getHadError()) continue;
					fireProgressUpdated("Removing Unique Reviewable Immaterial", current+"/"+total, current, total);
					Collection<Item> items = r.getUniqueFamily().stream().filter(i -> i.isAudited() == true).collect(Collectors.toList());
					if(removeExcludedItems){
						r.setUniqueFamily(rejectExcludedItems(items));
					}else{
						r.setUniqueFamily(items);
					}
				}
			}
		}
		DateTime collectFinish = new DateTime();
		uniqueFamilyTotalTime = new Period(collectStart,collectFinish);
	}

	protected void collectUniqueTopLevelData(ArrayList<SearchResult> results, ItemUtility iutil, int total) {
		DateTime collectStart = new DateTime();
		ItemInstanceCounter counter = new ItemInstanceCounter();
		for(int current = 0; current < results.size();current++){
			SearchResult r = results.get(current);
			if(r.getHadError()) continue;
			fireProgressUpdated("Locating Unique Top Level", current+"/"+total, current, total);
			if(r.getHits().size() > 0){
				r.setUniqueTopLevel(iutil.findTopLevelItems(r.getHits()));
			} else {
				r.setUniqueTopLevel(new ArrayList<Item>());
			}
			
			for(Item item : r.getUniqueTopLevel()){
				counter.increment(item);
			}
		}
		
		//Filter on occurrences
		for(int current = 0; current < results.size();current++){
			SearchResult r = results.get(current);
			if(r.getHadError()) continue;
			fireProgressUpdated("Resolving Unique Top Level", current+"/"+total, current, total);
			Collection<Item> items = r.getUniqueTopLevel().stream().filter(i -> counter.occurrences(i) < 2).collect(Collectors.toList()); 
			if(removeExcludedItems){
				r.setUniqueTopLevel(rejectExcludedItems(items));
			}else{
				r.setUniqueTopLevel(items);
			}
		}
		DateTime collectFinish = new DateTime();
		uniqueTopLevelTotalTime = new Period(collectStart,collectFinish);
	}

	protected void collectUniqueHitData(ArrayList<SearchResult> results, int total) {
		DateTime collectStart = new DateTime();
		//We will count occurrences of each item in all hits item collections
		ItemInstanceCounter counter = new ItemInstanceCounter();
		for(int current = 0; current < results.size();current++){
			SearchResult r = results.get(current);
			if(r.getHadError()) continue;
			fireProgressUpdated("Locating Unique Hits", current+"/"+total, current, total);
			for(Item item : r.getHits()){
				counter.increment(item);
			}
		}
		
		//Filter on occurrences
		for(int current = 0; current < results.size();current++){
			SearchResult r = results.get(current);
			if(r.getHadError()) continue;
			fireProgressUpdated("Resolving Unique Hits", current+"/"+total, current, total);
			Collection<Item> items = r.getHits().stream().filter(i -> counter.occurrences(i) < 2).collect(Collectors.toList());
			if(removeExcludedItems){
				r.setUniqueHits(rejectExcludedItems(items));
			}else{
				r.setUniqueHits(items);
			}
		}
		DateTime collectFinish = new DateTime();
		uniqueHitsTotalTime = new Period(collectStart,collectFinish);
	}

	protected void collectFamilyHitData(ArrayList<SearchResult> results, ItemUtility iutil, int total) {
		DateTime collectStart = new DateTime();
		if(collectFamilyHits){
			//If we collected top level hits use them to determine families
			//else use hits
			if(collectTopLevelHits){
				for(int current = 0; current < results.size();current++){
					SearchResult r = results.get(current);
					if(r.getHadError()) continue;
					fireProgressUpdated("Resolving Reviewable Hits", current+"/"+total, current, total);
					Collection<Item> items = iutil.findFamilies(r.getTopLevelHits());
					if(removeExcludedItems){
						r.setFamilyHits(rejectExcludedItems(items));
					}else{
						r.setFamilyHits(items);
					}
					
				}
			}else{
				for(int current = 0; current < results.size();current++){
					SearchResult r = results.get(current);
					if(r.getHadError()) continue;
					fireProgressUpdated("Resolving Reviewable Hits", current+"/"+total, current, total);
					Collection<Item> items = iutil.findFamilies(r.getHits());
					if(removeExcludedItems){
						r.setFamilyHits(rejectExcludedItems(items));
					}else{
						r.setFamilyHits(items);
					}
				}
			}
			
			//Remove immaterial items from family results if requested
			if(removeFamilyImmaterial){
				for(int current = 0; current < results.size();current++){
					SearchResult r = results.get(current);
					if(r.getHadError()) continue;
					fireProgressUpdated("Removing Reviewable Hits Immaterial", current+"/"+total, current, total);
					Collection<Item> items = r.getFamilyHits().stream().filter(i -> i.isAudited() == true).collect(Collectors.toList());
					if(removeExcludedItems){
						r.setFamilyHits(rejectExcludedItems(items));
					}else{
						r.setFamilyHits(items);
					}
				}
			}
		}
		DateTime collectFinish = new DateTime();
		familyHitsTotalTime = new Period(collectStart,collectFinish);
	}

	protected void collectTopLevelHitData(ArrayList<SearchResult> results, ItemUtility iutil, int total) {
		DateTime collectStart = new DateTime();
		if(collectTopLevelHits){
			for(int current = 0; current < results.size();current++){
				SearchResult r = results.get(current);
				if(r.getHadError()) continue;
				fireProgressUpdated("Resolving Top Level Hits", current+"/"+total, current, total);
				try {
					Collection<Item> items = null;
					if(r.getHits().size() > 0){
						items = iutil.findTopLevelItems(r.getHits());	
					} else {
						items = new ArrayList<Item>();
					}
					
					if(removeExcludedItems){
						r.setTopLevelHits(rejectExcludedItems(items));
					}else{
						r.setTopLevelHits(items);
					}
				} catch (Exception e) {
					logger.error("Error while resolving top level items");
					logger.error("Query: "+r.getQuery());
					logger.error("Term: "+r.getTerm());
					logger.error("Hits: "+r.getHits().size());
					logger.error("StackTrace Follows",e);
					e.printStackTrace();
					throw e;
				}
			}
		}
		DateTime collectFinish = new DateTime();
		topLevelHitsTotalTime = new Period(collectStart,collectFinish);
	}

	protected void collectHitData(nuix.Case nuixCase, List<SearchResult> results, List<String> defaultFields, int total) {
		DateTime collectStart = new DateTime();
		for(int current = 0; current < results.size();current++){
			SearchResult r = results.get(current);
			fireProgressUpdated("Searching", current+"/"+total, current, total);
			Collection<Item> items;
			try {
				if(defaultFields != null){
					Map<String,Object> options = new HashMap<String,Object>();
					options.put("defaultFields",defaultFields);
					items = nuixCase.searchUnsorted(r.getQuery(),options);
				} else {
					items = nuixCase.searchUnsorted(r.getQuery());
				}
				if(removeExcludedItems){
					r.setHits(rejectExcludedItems(items));
				}else{
					r.setHits(items);
				}
				
			} catch (Exception e) {
				r.setHadError(true);
				r.setErrorMessage(e.getMessage());
				//e.printStackTrace();
			}
		}
		DateTime collectFinish = new DateTime();
		hitsTotalTime = new Period(collectStart,collectFinish);
	}

	public boolean isCollectTopLevelHits() {
		return collectTopLevelHits;
	}

	public void setCollectTopLevelHits(boolean collectTopLevelHits) {
		this.collectTopLevelHits = collectTopLevelHits;
	}

	public boolean isCollectFamilyHits() {
		return collectFamilyHits;
	}

	public void setCollectFamilyHits(boolean collectFamilyHits) {
		this.collectFamilyHits = collectFamilyHits;
	}

	public boolean isCollectUniqueHits() {
		return collectUniqueHits;
	}

	public void setCollectUniqueHits(boolean collectUniqueHits) {
		this.collectUniqueHits = collectUniqueHits;
	}

	public boolean isCollectUniqueTopLevel() {
		return collectUniqueTopLevel;
	}

	public void setCollectUniqueTopLevel(boolean collectUniqueTopLevel) {
		this.collectUniqueTopLevel = collectUniqueTopLevel;
	}

	public boolean isCollectUniqueFamily() {
		return collectUniqueFamily;
	}

	public void setCollectUniqueFamily(boolean collectUniqueFamily) {
		this.collectUniqueFamily = collectUniqueFamily;
	}

	public boolean isRemoveFamilyImmaterial() {
		return removeFamilyImmaterial;
	}

	public void setRemoveFamilyImmaterial(boolean removeFamilyImmaterial) {
		this.removeFamilyImmaterial = removeFamilyImmaterial;
	}

	public boolean isRemoveUniqueFamilyImmaterial() {
		return removeUniqueFamilyImmaterial;
	}

	public void setRemoveUniqueFamilyImmaterial(boolean removeUniqueFamilyImmaterial) {
		this.removeUniqueFamilyImmaterial = removeUniqueFamilyImmaterial;
	}
	
	public void whenProgressUpdated(ProgressListener watcher){
		progressWatcher = watcher;
	}
	
	public String getTimingsDump(){
		StringBuilder result = new StringBuilder();
		for(Map.Entry<String,String> entry : getTimings().entrySet()){
			result.append(entry.getKey()+": "+entry.getValue()+"\n");
		}
		return result.toString();
	}
	
	public LinkedHashMap<String,String> getTimings(){
		LinkedHashMap<String,String> results = new LinkedHashMap<String,String>();
		results.put("Hits", timingsFormatter.print(hitsTotalTime));
		results.put("Top Level Hits", timingsFormatter.print(topLevelHitsTotalTime));
		results.put("Reviewable Hits", timingsFormatter.print(familyHitsTotalTime));
		results.put("Unique Hits", timingsFormatter.print(uniqueHitsTotalTime));
		results.put("Unique Top Level", timingsFormatter.print(uniqueTopLevelTotalTime));
		results.put("Unique Reviewable", timingsFormatter.print(uniqueFamilyTotalTime));
		return results;
	}
}
