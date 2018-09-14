package com.nuix.nx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import nuix.BulkAnnotater;
import nuix.Item;
import nuix.ItemEventCallback;
import nuix.ItemSorter;
import nuix.ItemUtility;
import nuix.MetadataItem;
import nuix.MetadataProfile;
import nuix.MultipleItemExporter;

public class ItemGroup implements java.util.List<Item>{

	private java.util.List<Item> wrappedList;
	
	private ItemGroup(java.util.List<Item> items){
		wrappedList = items;
	}
	
	private ItemGroup(){
		wrappedList = new ArrayList<Item>();
	}
	
	/**
	 * Creates a new ItemGroup instance based upon a provided query.
	 * Internally uses Case.search
	 * @param query A Lucene query which can be accepted by Nuix
	 * @return A new ItemGroup instance which represents the hits of the provided query.
	 * @throws IOException
	 */
	public static ItemGroup fromSearch(String query) throws IOException{
		return new ItemGroup(NuixDataBridge.getCurrentCase().search(query));
	}
	
	/**
	 * Creates a new ItemGroup instance based upon a provided query.
	 * Internally uses Case.searchUnsorted
	 * @param query A Lucene query which can be accepted by Nuix
	 * @return A new ItemGroup instance which represents the hits of the provided query.
	 * @throws IOException
	 */
	public static ItemGroup fromSearchUnsorted(String query) throws IOException{
		return new ItemGroup(new ArrayList<Item>(NuixDataBridge.getCurrentCase().searchUnsorted(query)));
	}
	
	/**
	 * Unions the hits from the provided search into the items of this instance.
	 * Internally uses ItemUtility.union
	 * @param query A Lucene query which can be accepted by Nuix
	 * @throws IOException
	 */
	public void addSearch(String query) throws IOException{
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.union(wrappedList, NuixDataBridge.getCurrentCase().search(query)));
	}
	
	/**
	 * Removes the hits from the provided search from this instance.
	 * Internally uses ItemUtility.difference
	 * @param query A Lucene query which can be accepted by Nuix
	 * @throws IOException
	 */
	public void removeSearch(String query) throws IOException{
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.difference(wrappedList, NuixDataBridge.getCurrentCase().search(query)));
	}
	
	/**
	 * Intersects the hits of the provided search with this instance, such that this instance will afterwards
	 * contain the intersection result.  Internally uses ItemUtility.intersection
	 * @param query A Lucene query which can be accepted by Nuix
	 * @throws IOException
	 */
	public void intersectSearch(String query) throws IOException{
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.intersection(wrappedList, NuixDataBridge.getCurrentCase().search(query)));
	}
	
	public void toFamilies(){
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.findFamilies(wrappedList));
	}
	
	public void includeDescendants(){
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.findItemsAndDescendants(wrappedList));
	}
	
	public void toTopLevelItems(){
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.findTopLevelItems(wrappedList));
	}
	
	public void deduplicate(){
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.deduplicate(wrappedList));
	}
	
	public void deduplicatePerCustodian(){
		ItemUtility iutil = NuixDataBridge.getUtilities().getItemUtility();
		wrappedList = new ArrayList<Item>(iutil.deduplicatePerCustodian(wrappedList));
	}
	
	public void addTag(String tag) throws Exception{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.addTag(tag, wrappedList);
	}
	
	public void removeTag(String tag) throws Exception{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.removeTag(tag, wrappedList);
	}
	
	public void putCustomMetadata(String fieldName, Object value, ItemEventCallback callback) throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.putCustomMetadata(fieldName, value, wrappedList, callback);
	}
	
	public void removeCustomMetadata(String fieldName, ItemEventCallback callback) throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.removeCustomMetadata(fieldName, wrappedList, callback);
	}
	
	public void exclude(String name) throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.exclude(name, wrappedList);
	}
	
	public void include() throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.include(wrappedList);
	}
	
	public void assignCustodian(String custodian) throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.assignCustodian(custodian, wrappedList);
	}
	
	public void unassignCustodian() throws IOException{
		BulkAnnotater annotater = NuixDataBridge.getUtilities().getBulkAnnotater();
		annotater.unassignCustodian(wrappedList);
	}
	
	public void exportAsCaseSubset(String location, java.util.Map<?,?> options) throws IOException{
		MultipleItemExporter exporter = NuixDataBridge.getUtilities().getCaseSubsetExporter();
		exporter.exportItems(wrappedList,location,options);
	}
	
	public long getTotalAuditedSizeBytes(){
		long result = 0;
		for(Item i : wrappedList){
			result += i.getAuditedSize();
		}
		return result;
	}
	
	public long getTotalFileSizeBytes(){
		long result = 0;
		for(Item i : wrappedList){
			result += i.getFileSize();
		}
		return result;
	}
	
	public void sortItemsByPosition(){
		ItemSorter sorter = NuixDataBridge.getUtilities().getItemSorter();
		wrappedList = sorter.sortItemsByPosition(new ArrayList<Item>(wrappedList));
	}
	
	public void sortItemsByTopLevelItemDate(){
		ItemSorter sorter = NuixDataBridge.getUtilities().getItemSorter();
		wrappedList = sorter.sortItemsByPosition(new ArrayList<Item>(wrappedList));
	}
	
	public Iterator<List<String>> metadataProfileRecords(String metadataProfileName){
		return metadataProfileRecords(NuixDataBridge.getUtilities().getMetadataProfileStore().getMetadataProfile(metadataProfileName));
	}
	
	public Iterator<List<String>> metadataProfileRecords(final MetadataProfile profile){
		return new Iterator<List<String>>(){
			private int index = 0;
			Item[] items = wrappedList.toArray(new Item[]{});
			List<MetadataItem> fields = profile.getMetadata();
			@Override
			public boolean hasNext() {
				return index < wrappedList.size();
			}

			@Override
			public List<String> next() {
				List<String> result = new ArrayList<String>();
				for(MetadataItem field : fields){
					try {
						result.add(field.evaluate(items[index]));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				index++;
				return result;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	public Map<String,ItemGroup> divideIntoCategories(ItemCategorizer categorizer){
		HashMap<String,ItemGroup> result = new HashMap<String, ItemGroup>();
		for(Item item : wrappedList){
			for(String category : categorizer.categorizeItem(item)){
				if(!result.containsKey(category)){
					result.put(category, new ItemGroup());
				}
				result.get(category).add(item);
			}
		}
		return result;
	}
	
	public Map<String,ItemGroup> divideByCustodian(){
		return divideIntoCategories(new ItemCategorizer(){

			@Override
			public String[] categorizeItem(Item item) {
				String custodian = item.getCustodian();
				if(custodian == null)
					custodian = "NO CUSTODIAN";
				return new String[]{custodian};
			}
			
		});
	}

	@Override
	public boolean add(Item e) { return wrappedList.add(e); }

	@Override
	public boolean addAll(Collection<? extends Item> c) { return wrappedList.addAll(c); }

	@Override
	public void clear() { wrappedList.clear(); }

	@Override
	public boolean contains(Object o) { return wrappedList.contains(o); }

	@Override
	public boolean containsAll(Collection<?> c) { return wrappedList.containsAll(c); }

	@Override
	public boolean isEmpty() { return wrappedList.isEmpty(); }

	@Override
	public Iterator<Item> iterator() { return wrappedList.iterator(); }

	@Override
	public boolean remove(Object o) { return wrappedList.remove(o); }

	@Override
	public boolean removeAll(Collection<?> c) { return wrappedList.removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return wrappedList.retainAll(c); }

	@Override
	public int size() { return wrappedList.size(); }

	@Override
	public Object[] toArray() { return wrappedList.toArray(); }

	@Override
	public <T> T[] toArray(T[] a) { return wrappedList.toArray(a); }

	@Override
	public void add(int index, Item element) { wrappedList.add(index, element); }

	@Override
	public boolean addAll(int index, Collection<? extends Item> c) { return wrappedList.addAll(c); }

	@Override
	public Item get(int index) { return wrappedList.get(index); }

	@Override
	public int indexOf(Object o) { return wrappedList.indexOf(o); }

	@Override
	public int lastIndexOf(Object o) { return wrappedList.lastIndexOf(o); }

	@Override
	public ListIterator<Item> listIterator() { return wrappedList.listIterator(); }

	@Override
	public ListIterator<Item> listIterator(int index) { return wrappedList.listIterator(index); }

	@Override
	public Item remove(int index) { return wrappedList.remove(index); }

	@Override
	public Item set(int index, Item element) { return wrappedList.set(index, element); }

	@Override
	public List<Item> subList(int fromIndex, int toIndex) { return wrappedList.subList(fromIndex, toIndex); }
}
