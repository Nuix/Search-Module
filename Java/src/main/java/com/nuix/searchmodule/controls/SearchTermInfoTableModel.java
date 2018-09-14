package com.nuix.searchmodule.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.nuix.searchmodule.SearchTermInfo;

@SuppressWarnings("serial")
public class SearchTermInfoTableModel extends AbstractTableModel {

	private String[] columnNames = {
			"#",
			"Term",
			"Short Name",
	};
	private List<SearchTermInfo> searchTerms = new ArrayList<SearchTermInfo>();
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return searchTerms.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		//Not sure why, but when notifying that a row has been deleted something (superclass?)
		//immediately tries to fetch the missing row, this is a kludge fix for that to prevent
		//index out of bounds exceptions
		if(row == searchTerms.size()){
			return "";
		}
		
		switch(col){
			case 0:
				return row+1;
			case 1:
				return searchTerms.get(row).getTerm();
			case 2:
				return searchTerms.get(row).getShortName();
			default:
					return "";
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1 || columnIndex == 2;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch(columnIndex){
			case 1:
				searchTerms.get(rowIndex).setTerm((String)aValue);
				this.fireTableCellUpdated(rowIndex, columnIndex);
				break;
			case 2:
				searchTerms.get(rowIndex).setShortName((String)aValue);
				this.fireTableCellUpdated(rowIndex, columnIndex);
				break;
		}
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		if(arg0 == 0)
			return Integer.class;
		else return String.class;
	}

	public List<SearchTermInfo> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(List<SearchTermInfo> searchTerms) {
		this.searchTerms = searchTerms;
		this.fireTableDataChanged();
	}
	
	public void addSearchTerm(){
		addSearchTerm(new SearchTermInfo());
	}
	
	public void addSearchTerm(String term){
		addSearchTerm(new SearchTermInfo(term));
	}
	
	public void addSearchTerm(String term, String shortName){
		addSearchTerm(new SearchTermInfo(term,shortName));
	}
	
	public void addSearchTerm(SearchTermInfo info){
		searchTerms.add(info);
		int lastRow = searchTerms.size()-1;
		this.fireTableRowsInserted(lastRow, lastRow);
	}
	
	public void addAllSearchTerms(List<String> terms){
		List<SearchTermInfo> convertedTerms = new ArrayList<SearchTermInfo>();
		for(String term : terms){
			convertedTerms.add(new SearchTermInfo(term));
		}
		addAllSearchTermInfos(convertedTerms);
	}
	
	public void addAllSearchTermInfos(List<SearchTermInfo> terms) {
		searchTerms.addAll(terms);
		this.fireTableDataChanged();
	}
	
	public SearchTermInfo getIndex(int index){
		return searchTerms.get(index);
	}
	
	public void removeIndices(int[] indices){
		Arrays.sort(indices);
		for(int i=indices.length-1;i>=0;i--){
			int rowIndex = indices[i];
			searchTerms.remove(rowIndex);
		}
		this.fireTableDataChanged();
	}
	
	public void clear(){
		if(searchTerms.size() > 0){
			int oldEndIndex = searchTerms.size() - 1;
			searchTerms.clear();
			this.fireTableRowsDeleted(0, oldEndIndex);
		}
	}
	
	public SearchTermInfo getAtRow(int rowIndex){
		return searchTerms.get(rowIndex);
	}
}
