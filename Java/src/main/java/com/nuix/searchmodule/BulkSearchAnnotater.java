package com.nuix.searchmodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import nuix.BulkAnnotater;
import nuix.Item;
import nuix.ItemCustomMetadataMap;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.QueryGenerator;


public class BulkSearchAnnotater extends BulkSearcher {
	private  List<CategorySettings> allSettings = new ArrayList<CategorySettings>();
	
	private Period categoryTagTotalTime = new Period();
	private Period termTagTotalTime = new Period();
	private Period customMetadataTotalTime = new Period();
	
	private boolean tagsRequireHits = false;

	public List<CategorySettings> getSettings() {
		return allSettings;
	}

	@Override
	public void setSettings(List<CategorySettings> settings) {
		super.setSettings(settings);
		this.allSettings = settings;
	}
	
	@Override
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms){
		return performSearching(nuixCase,searchTerms,null,null);
	}
	
	@Override
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms, QueryGenerator scope){
		return performSearching(nuixCase,searchTerms,scope,null);
	}
	
	@Override
	public List<SearchResult> performSearching(nuix.Case nuixCase, List<SearchTermInfo> searchTerms, QueryGenerator scope, List<String> fields){
		List<SearchResult> results = super.performSearching(nuixCase, searchTerms, scope, fields);
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		
		SimpleTemplate template = new SimpleTemplate();
		String dateString = new DateTime().toString("YYYYMMdd");
		String timeString = new DateTime().toString("HHmmss");
		template.put("date", dateString);
		template.put("time", timeString);
		template.put("datetime", dateString+"_"+timeString);
		
		//Iterate all the category settings
		for(CategorySettings currentCategory : allSettings){
			//Is this category even being reported?
			if(currentCategory.getReportCount()){
				template.put("category", currentCategory.getCategoryTitle());

				//Apply category tags if requested
				if(currentCategory.getApplyCategoryTags()){
					DateTime annotationStart = new DateTime();
					template.setTemplateString(currentCategory.getCategoryTagTemplate());
					String tag = template.resolveTemplate();
					if(tag.length() > 256){
						tag = tag.substring(0, 256);
					}
					fireProgressUpdated("Tagging Category: "+currentCategory.getCategoryTitle(), tag, 1, 1);
					List<Collection<Item>> resultItemsByCategory = new ArrayList<Collection<Item>>();
					for(SearchResult result : results){
						if(result.getHadError()) continue;
						resultItemsByCategory.add(getResultCategoryItems(currentCategory,result));
					}
					Set<Item> allInCategory = ItemUtilityX.unionMany(resultItemsByCategory);
					try {
						nuixCase.createTag(tag);
						annotater.addTag(tag, allInCategory);
					} catch (IOException e) {
						System.err.println("Error while applying tag for category: "+currentCategory.getCategoryTitle());
						e.printStackTrace();
					}
					DateTime annotationFinish = new DateTime();
					categoryTagTotalTime = new Period(annotationStart,annotationFinish);
				}
				
				//Apply term tags if requested
				if(currentCategory.getApplyTermTags()){
					DateTime annotationStart = new DateTime();
					int sequence = 0;
					template.setTemplateString(currentCategory.getTermTagTemplate());
					String categoryTermTagKey = currentCategory.getCategoryKey()+"_term_tag";
					for(SearchResult result : results){
						sequence++;
						if(result.getHadError()) continue;
						template.put("term", result.getTerm());
						String sequenceString = String.format("%04d", sequence);
						template.put("sequence", sequenceString);
						String shortName = result.getShortName();
						if(shortName == null || shortName.isEmpty()){
							template.put("shortname", sequenceString);
						}else{
							template.put("shortname", shortName);
						}
						String tag = template.resolveTemplate();
						if(tag.length() > 256){
							tag = tag.substring(0, 256);
						}
						Collection<Item> categoryItems = getResultCategoryItems(currentCategory,result);
						fireProgressUpdated("Tagging Per Term, Category: "+currentCategory.getCategoryTitle(), tag, sequence, results.size());
						try {
							if(!tagsRequireHits)
								nuixCase.createTag(tag);
							annotater.addTag(tag, categoryItems);
							result.put(categoryTermTagKey, tag);
						} catch (IOException e) {
							System.err.println("Error while applying tag for term/category: "+result.getTerm()+" / "+currentCategory.getCategoryTitle());
							e.printStackTrace();
							result.put(categoryTermTagKey, "Error while tagging");
						}
					}
					DateTime annotationFinish = new DateTime();
					termTagTotalTime = new Period(annotationStart,annotationFinish);
				}
				
				//Apply custom meta data if requested
				if(currentCategory.getApplyCustomMetadata()){
					DateTime annotationStart = new DateTime();
					int sequence = 0;
					template.setTemplateString(currentCategory.getCustomMetadataTemplate());
					for(SearchResult result : results){
						sequence++;
						if(result.getHadError()) continue;
						template.put("term", result.getTerm());
						String sequenceString = String.format("%04d", sequence);
						template.put("sequence", sequenceString);
						String shortName = result.getShortName();
						if(shortName == null || shortName.isEmpty()){
							template.put("shortname", sequenceString);
						}else{
							template.put("shortname", shortName);
						}
						Collection<Item> categoryItems = getResultCategoryItems(currentCategory,result);
						fireProgressUpdated("Applying Custom Metadata, Category:"+currentCategory.getCategoryTitle(),
								 "Term "+sequence+"/"+results.size(), sequence, results.size());
						int itemIndex = 0;
						DateTime lastProgressUpdate = new DateTime();
						for(Item item : categoryItems){
							itemIndex++;
							Duration sinceLastProgressUpdate = new Duration(lastProgressUpdate,new DateTime());
							//If takes longer than one second begin showing item progress at 1 second intervals
							if(sinceLastProgressUpdate.getStandardSeconds() > 1){
								fireProgressUpdated("Applying Custom Metadata, Category:"+currentCategory.getCategoryTitle(),
										 "Term "+sequence+"/"+results.size()+", Item "+itemIndex+"/"+categoryItems.size(), sequence, results.size());
								lastProgressUpdate = new DateTime();
							}
							ItemCustomMetadataMap customMetadata = item.getCustomMetadata();
							String fieldName = currentCategory.getCustomMetadataField();
							Object previousValueObject = customMetadata.get(fieldName);
							String previousValue = "";
							if(previousValueObject != null){
								previousValue = previousValueObject.toString();
								customMetadata.putText(fieldName, previousValue + "; " + template.resolveTemplate());
							}else{
								customMetadata.putText(fieldName, template.resolveTemplate());
							}
						}
					}
					DateTime annotationFinish = new DateTime();
					customMetadataTotalTime = new Period(annotationStart,annotationFinish);
				}
			}
		}
		return results;
	}
	
	protected Collection<Item> getResultCategoryItems(CategorySettings category, SearchResult result){
		//TODO: Remove usage of magic strings held over from Ruby code
		String categoryKey = category.getCategoryKey();
		switch(categoryKey){
			case "hits":
				return result.getHits();
			case "top_level_hits":
				return result.getTopLevelHits();
			case "family_hits":
				return result.getFamilyHits();
			case "unique_hits":
				return result.getUniqueHits();
			case "unique_top_level":
				return result.getUniqueTopLevel();
			case "unique_family":
				return result.getUniqueFamily();
			default:
					System.err.println("Unrecognized category key: "+categoryKey);
					return null;
		}
	}
	
	@Override
	public LinkedHashMap<String,String> getTimings(){
		LinkedHashMap<String,String> results = super.getTimings();
		
		if(allSettings.stream().anyMatch(s -> s.getApplyCategoryTags()))
			results.put("Tag Categories", timingsFormatter.print(categoryTagTotalTime));
		
		if(allSettings.stream().anyMatch(s -> s.getApplyTermTags()))
			results.put("Tag Terms", timingsFormatter.print(termTagTotalTime));
		
		if(allSettings.stream().anyMatch(s -> s.getAppendCustomMetadata()))
			results.put("Apply Custom Metadata", timingsFormatter.print(customMetadataTotalTime));
		
		return results;
	}
	
	public void setTagsRequireHits(boolean value){
		tagsRequireHits = value;
	}
	
	public boolean getTagsRequireHits(){
		return tagsRequireHits;
	}
}
