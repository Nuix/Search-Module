package com.nuix.searchmodule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nuix.nx.GenericSerializationAdapter;
import com.nuix.nx.NuixVersion;
import com.nuix.nx.query.QueryCriteria;
import com.nuix.nx.query.QueryGenerator;
import com.nuix.searchmodule.dialogs.MainDialog;

public class SearchModuleProject {
	private QueryGenerator scopeQuery = new QueryGenerator();
	private List<SearchTermInfo> searchTerms = new ArrayList<SearchTermInfo>();
	private List<String> termFields = new ArrayList<String>();
	private List<CategorySettings> categoriesSettings = new ArrayList<CategorySettings>();
	private boolean handleExclusions = false;
	private boolean includeCoverSheet = false;
	private boolean includeLogo = false;
	private String coverSheetInformation = "Date: {date}\nCase: {casename}";
	private boolean tagsRequireHits = false;
	private boolean reportReviewableByCustodian = false;
	@SuppressWarnings("unused")
	private String savedByNuixVersion = NuixVersion.getCurrent().toString();
	@SuppressWarnings("unused")
	private String savedByVersion = MainDialog.getVersion();
	
	public SearchModuleProject(){
		categoriesSettings.add(new CategorySettings("hits","Hits"));
		categoriesSettings.add(new CategorySettings("top_level_hits","Top Level Hits"));
		categoriesSettings.add(new CategorySettings("family_hits","Reviewable Hits"));
		categoriesSettings.add(new CategorySettings("unique_hits","Unique Hits",false));
		categoriesSettings.add(new CategorySettings("unique_top_level","Unique Top Level",false));
		categoriesSettings.add(new CategorySettings("unique_family","Unique Family",false));
		termFields.add("content");
		termFields.add("name");
		termFields.add("properties");
	}
	
	public String toJson(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(QueryCriteria.class, new GenericSerializationAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}
	
	public static SearchModuleProject fromJson(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(QueryCriteria.class, new GenericSerializationAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, SearchModuleProject.class);
	}
	
	public void saveToFile(String file) throws IOException{
		saveToFile(new File(file));
	}
	
	public void saveToFile(File file) throws IOException{
		file.getParentFile().mkdirs();
		Files.write(this.toJson(), file, Charset.forName("UTF-8"));
	}
	
	public static SearchModuleProject loadFromFile(File file) throws IOException{
		 List<String> lines = Files.readLines(file, Charset.forName("UTF-8"));
		 String json = Joiner.on("\n").join(lines);
		 return fromJson(json);
	}
	
	public CategorySettings getCategorySettingsByKey(String key){
		for(CategorySettings category : categoriesSettings){
			if(category.getCategoryKey().equals(key)){
				return category;
			}
		}
		return null;
	}
	
	public Map<String,CategorySettings> getCategorySettingsMap(){
		Map<String,CategorySettings> result = new HashMap<String,CategorySettings>();
		for(CategorySettings category : categoriesSettings){
			result.put(category.getCategoryKey(), category);
		}
		return result;
	}
	
	public CategorySettings getHitsCategory(){ return getCategorySettingsByKey("hits"); }
	public CategorySettings getTopLevelHitsCategory(){ return getCategorySettingsByKey("top_level_hits"); }
	public CategorySettings getFamilyHitsCategory(){ return getCategorySettingsByKey("family_hits"); }
	public CategorySettings getUniqueHitsCategory(){ return getCategorySettingsByKey("unique_hits"); }
	public CategorySettings getUniqueTopLevelCategory(){ return getCategorySettingsByKey("unique_top_level"); }
	public CategorySettings getUniqueFamilyCategory(){ return getCategorySettingsByKey("unique_family"); }

	public QueryGenerator getScopeQuery() {
		return scopeQuery;
	}

	public void setScopeQuery(QueryGenerator scopeQuery) {
		this.scopeQuery = scopeQuery;
	}

	public List<SearchTermInfo> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(List<SearchTermInfo> searchTerms) {
		this.searchTerms = searchTerms;
	}

	public List<String> getTermFields() {
		return termFields;
	}

	public void setTermFields(List<String> termFields) {
		this.termFields = termFields;
	}

	public List<CategorySettings> getCategoriesSettings() {
		return categoriesSettings;
	}

	public void setCategoriesSettings(List<CategorySettings> categoriesSettings) {
		this.categoriesSettings = categoriesSettings;
	}
	
	public boolean getHandleExclusions() {
		return handleExclusions;
	}

	public void setHandleExclusions(boolean handleExclusions) {
		this.handleExclusions = handleExclusions;
	}

	public void revalidateAllTerms(){
		for(SearchTermInfo term : searchTerms){
			term.revalidateTerm();
		}
	}
	
	public boolean getIncludeCoverSheet(){
		return includeCoverSheet;
	}
	
	public void setIncludeCoverSheet(boolean value){
		includeCoverSheet = value;
	}
	
	public boolean getIncludeLogo(){
		return includeLogo;
	}
	
	public void setIncludeLogo(boolean value){
		includeLogo = value;
	}
	
	public String getCoverSheetInformation(){
		return coverSheetInformation;
	}
	
	public void setCoverSheetInformation(String value){
		coverSheetInformation = value;
	}
	
	public void setTagsRequireHits(boolean value){
		tagsRequireHits = value;
	}
	
	public boolean getTagsRequireHits(){
		return tagsRequireHits;
	}
	
	public boolean getReportReviewableByCustodian(){
		return reportReviewableByCustodian;
	}
	
	public void setReportReviewableByCustodian(boolean value){
		reportReviewableByCustodian = value;
	}
	
	
}
