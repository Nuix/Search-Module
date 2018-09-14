package com.nuix.nx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import nuix.BatchLoadDetails;
import nuix.ItemSet;
import nuix.ItemSetBatch;
import nuix.ItemType;
import nuix.Window;

/**
 * Provides indirect access to certain Nuix runtime values.  Before usage of the various {@link com.nuix.nx.query.QueryCriteriaBase} objects and the
 * {@link com.nuix.nx.controls.QueryBuilderControl}, code should submit a {@link DataProvider} for each of the "when...Requested" methods, allowing
 * this class to server of the appropriate Nuix runtime values for the current class.
 * 
 * This allows the required Ruby code to be at a minimum for Ruby scripts and should allow code developed for use with the Nuix engine
 * to also use these classes as long as they supply the appropriate providers.
 * @author JasonWells
 *
 */
public class NuixDataBridge {
	private static Map<String,DataProvider<List<NameValuePair>>> registeredProviders =
			new HashMap<String,DataProvider<List<NameValuePair>>>();
	
	public static void registerProvider(String name, DataProvider<List<NameValuePair>> provider){
		registeredProviders.put(name.toLowerCase(), provider);
	}
	
	public static List<NameValuePair> getRegisteredProviderValue(String name){
		if(registeredProviders.keySet().contains(name.toLowerCase())){
			return registeredProviders.get(name.toLowerCase()).getData();
		}
		else{
			System.out.println("Script:NuixDateProvider: Supplied name not present: "+name);
			return new ArrayList<NameValuePair>();
		}
	}
	
	private static List<NameValuePair> fetchDataFrom(DataProvider<List<NameValuePair>> provider){
		if(provider!=null)
			return provider.getData();
		else
			return new ArrayList<NameValuePair>();
	}
	
	//getCurrentCase
	private static DataProvider<nuix.Case> currentCaseProvider;
	public static void whenCurrentCaseRequested(DataProvider<nuix.Case> provider){
		currentCaseProvider = provider;
	}
	
	/**
	 * Gets the current Nuix case
	 * @return The current case
	 */
	public static nuix.Case getCurrentCase(){
		nuix.Case currentCase = currentCaseProvider.getData();
		if(currentCase == null){
			System.out.println("Current case is null, expect an exception to occur shortly...");
		}
		return currentCase;
	}
	
	//getCurrentCase
	private static DataProvider<nuix.Utilities> utilitiesProvider;
	public static void whenUtilitiesRequested(DataProvider<nuix.Utilities> provider){
		utilitiesProvider = provider;
	}
	
	/**
	 * Gets the current Nuix utilities object
	 * @return The Nuix utilities object
	 */
	public static nuix.Utilities getUtilities(){
		return utilitiesProvider.getData();
	}
	
	//getTags
	/**
	 * Provides a List of {@link NameValuePair} with information about the tags in the current case.
	 * @return A list of the tags in the current case
	 */
	public static List<NameValuePair> getTags(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getCurrentCase().getAllTags()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getCustodians
	/**
	 * Provides a List of {@link NameValuePair} with information about the custodians in the current case.
	 * @return A list of the custodians in the current case
	 */
	public static List<NameValuePair> getCustodians(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getCurrentCase().getAllCustodians()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getItemSets
	/**
	 * Provides a List of {@link NameValuePair} with information about the item sets in the current case.
	 * @return A list of the item sets in the current case
	 */
	public static List<NameValuePair> getItemSets(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(nuix.ItemSet entry : getCurrentCase().getAllItemSets()){
				result.add(new NameValuePair(entry.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	public static List<String> getItemSetBatches(String itemSetName){
		List<String> result = new ArrayList<String>();
		try {
			ItemSet set = getCurrentCase().findItemSetByName(itemSetName);
			for(ItemSetBatch batch : set.getBatches()){
				result.add(batch.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getProductionSets
	/**
	 * Provides a List of {@link NameValuePair} with information about the production sets in the current case.
	 * @return A list of the production sets in the current case
	 */
	public static List<NameValuePair> getProductionSets(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(nuix.ProductionSet entry : getCurrentCase().getProductionSets()){
				result.add(new NameValuePair(entry.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getMimeTypes
	/**
	 * Provides a List of {@link NameValuePair} with information about the types in the current case.
	 * @return A list of the types in the current case
	 */
	public static List<NameValuePair> getMimeTypes(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(ItemType entry : getCurrentCase().getItemTypes()){
				result.add(new NameValuePair(entry.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getLanguages
	/**
	 * Provides a List of {@link NameValuePair} with information about the languages in the current case.
	 * @return A list of the languages in the current case
	 */
	public static List<NameValuePair> getLanguages(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getCurrentCase().getLanguages()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getOriginalExtensions
	/**
	 * Provides a List of {@link NameValuePair} with information about the original extensions in the current case.
	 * @return A list of the original extensions in the current case
	 */
	public static List<NameValuePair> getOriginalExtensions(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getCurrentCase().getAllOriginalExtensions()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getDigestLists
	/**
	 * Provides a List of {@link NameValuePair} with information about the digest lists in the current case.
	 * @return A list of the digest lists in the current case
	 */
	public static List<NameValuePair> getDigestLists(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getUtilities().getDigestListStore().getDigestListNames()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getBatchLoads
	/**
	 * Provides a List of {@link NameValuePair} with information about the batch loads in the current case.
	 * @return A list of the batch loads in the current case
	 */
	public static List<NameValuePair> getBatchLoads(){
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(BatchLoadDetails entry : getCurrentCase().getBatchLoads()){
				if(entry.getItems().size() > 0){
					String name = format.print(entry.getLoaded());
					String value = entry.getBatchId();
					result.add(new NameValuePair(name,value));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getEvidenceInfo
		/**
		 * Provides a List of {@link NameValuePair} with information about the evidence items in the current case.
		 * @return A list of the evidence items in the current case
		 * @throws IOException 
		 */
		public static List<NameValuePair> getEvidenceInfo(){
			List<NameValuePair> result = new ArrayList<NameValuePair>();
			try{
				List<nuix.Item> evidenceItems = getCurrentCase().getRootItems();
				for(nuix.Item item : evidenceItems){
					result.add(new NameValuePair(item.getName(),item.getGuid()));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Collections.sort(result);
			return result;
		}
	
	//getExclusions
	/**
	 * Provides a List of {@link NameValuePair} with information about the exclusions in the current case.
	 * @return A list of the exclusions in the current case
	 */
	public static List<NameValuePair> getExclusions(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		try {
			for(String entry : getCurrentCase().getAllExclusions()){
				result.add(new NameValuePair(entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	//getFlags
	private static DataProvider<List<NameValuePair>> flagsProvider;
	public static void whenFlagsRequested(DataProvider<List<NameValuePair>> provider){
		flagsProvider = provider;
	}
	/**
	 * Provides a List of {@link NameValuePair} with information about the possible flags
	 * @return A list of the possible flags
	 */
	public static List<NameValuePair> getFlags(){
		return fetchDataFrom(flagsProvider);
	}
	
	/**
	 * Provides a List of {@link NameValuePair} with information about the possible kinds
	 * @return A list of the possible kinds
	 */
	public static List<NameValuePair> getKinds(){
		return getUtilities().getItemTypeUtility().getAllKinds().stream()
			.map(kind -> new NameValuePair(kind.getName())).collect(Collectors.toList());
	}
	
	//getCommonDateFields
	private static DataProvider<List<NameValuePair>> dateFieldProvider;
	public static void whenDateFieldsRequested(DataProvider<List<NameValuePair>> provider){
		dateFieldProvider = provider;
	}
	/**
	 * Provides a List of {@link NameValuePair} with information about the common date fields
	 * @return A list of the common date fields
	 */
	private static List<NameValuePair> commonDateFields = null;
	public static List<NameValuePair> getCommonDateFields(){
		if(commonDateFields == null){
			commonDateFields = fetchDataFrom(dateFieldProvider);
		}
		return commonDateFields;
	}
	
	//getCommonIntegerFields
	private static DataProvider<List<NameValuePair>> integerFieldProvider;
	public static void whenIntegerFieldsRequested(DataProvider<List<NameValuePair>> provider){
		integerFieldProvider = provider;
	}
	/**
	 * Provides a List of {@link NameValuePair} with information about the common integer fields
	 * @return A list of the common integer fields
	 */
	public static List<NameValuePair> getCommonIntegerFields(){
		return fetchDataFrom(integerFieldProvider);
	}
	
	//getCommonTextFields
	private static DataProvider<List<NameValuePair>> textFieldProvider;
	public static void whenTextFieldsRequested(DataProvider<List<NameValuePair>> provider){
		textFieldProvider = provider;
	}
	/**
	 * Provides a List of {@link NameValuePair} with information about the common text fields
	 * @return A list of the common text fields
	 */
	public static List<NameValuePair> getCommonTextFields(){
		if(textFieldProvider!=null){
			return fetchDataFrom(textFieldProvider);
		}
		else{
			return new ArrayList<NameValuePair>();
		}
	}
	
	/**
	 * Provides a List of {@link NameValuePair} with information about the custom metadata fields.
	 * @return A list of the custom metadata fields present in the case
	 */
	private static List<NameValuePair> customMetadataFields = null;
	public static List<NameValuePair> getCustomMetadataFields(boolean refresh){
		if(customMetadataFields == null || refresh == true){
			List<NameValuePair> result = new ArrayList<NameValuePair>();
			if (NuixVersion.getCurrent().isAtLeast(new NuixVersion(5,2))){
				try {
					for(String entry : getCurrentCase().getCustomMetadataFields(new String[]{})){
						result.add(new NameValuePair(entry,"custom-metadata:\""+entry+"\""));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			customMetadataFields = result;
			return result;
		} else {
			return customMetadataFields;
		}
	}
	
	public static List<NameValuePair> getCustomMetadataFields(){
		return getCustomMetadataFields(false);
	}
	
	private static Window workbenchWindow = null;
	public static Window getWindow(){
		return workbenchWindow;
	}
	
	public static void setWindow(Window instance){
		workbenchWindow = instance;
	}
}