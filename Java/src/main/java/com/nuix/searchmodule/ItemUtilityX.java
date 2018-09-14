package com.nuix.searchmodule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nuix.Item;

import com.google.common.collect.Iterables;
import com.nuix.nx.NuixDataBridge;

public class ItemUtilityX {
	private static double gbInBytes = Math.pow(1000.0d, 3.0d);
	
	public static long getTotalAuditedSize(Collection<nuix.Item> items){
		long result = 0;
		long size = 0;
		for(nuix.Item item : items){
			size = item.getAuditedSize();
			if(size > 0){ result += size; }
		}
		return result;
	}
	
	public static long getTotalFileSize(Collection<nuix.Item> items){
		long result = 0;
		Long size = 0L;
		for(nuix.Item item : items){
			size = item.getFileSize();
			if(size != null && size > 0){ result += size; }
		}
		return result;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static double getTotalAuditedSizeGB(Collection<nuix.Item> items){
		double sizegb = ((double)getTotalAuditedSize(items)) / gbInBytes;
		double result = round(sizegb,4);
		return result;
	}
	
	public static double getTotalFileSizeGB(Collection<nuix.Item> items){
		double sizegb = ((double)getTotalFileSize(items)) / gbInBytes;
		double result = round(sizegb,4);
		return result;
	}
	
	//Leverages Guava Iterables.concat to get Nuix to union many collections at once
	public static Set<nuix.Item> unionMany(final List<Collection<nuix.Item>> itemSets){
		nuix.Utilities utils = NuixDataBridge.getUtilities();
		return utils.getItemUtility().union(new ArrayList<nuix.Item>(), new Collection<nuix.Item>(){

			@Override
			public boolean add(Item e) { return false; }

			@Override
			public boolean addAll(Collection<? extends Item> c) { return false; }

			@Override
			public void clear() { }

			@Override
			public boolean contains(Object o) {	return false; }

			@Override
			public boolean containsAll(Collection<?> c) { return false; }

			@Override
			public boolean isEmpty() { return itemSets.isEmpty(); }

			@Override
			public Iterator<Item> iterator() {
				return Iterables.concat(itemSets).iterator();
			}

			@Override
			public boolean remove(Object o) { return false;	}

			@Override
			public boolean removeAll(Collection<?> c) { return false; }

			@Override
			public boolean retainAll(Collection<?> c) {	return false; }

			@Override
			public int size() { 
				int result = 0;
				for(Collection<nuix.Item> itemSet : itemSets){
					result += itemSet.size();
				}
				return result;
			}

			@Override
			public Object[] toArray() {	return null; }

			@Override
			public <T> T[] toArray(T[] a) { return null; }
			
		});
	}
}
