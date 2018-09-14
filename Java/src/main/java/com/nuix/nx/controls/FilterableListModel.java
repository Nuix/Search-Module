package com.nuix.nx.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;

import com.nuix.nx.NameValuePair;

/**
 * The backing model for {@link FilterableListControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class FilterableListModel extends AbstractTableModel {

	private final String columnNames[] = { "", "Name"};
	private List<NameValuePair> _allPairs;
	private List<NameValuePair> _filteredPairs;
	private String filter;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(null);
		}
	}
	
	public FilterableListModel(){
		_allPairs = new ArrayList<NameValuePair>();
		_filteredPairs = new ArrayList<NameValuePair>();
		filter = "";
	}
	
	@Override
	public String getColumnName(int column){
		return columnNames[column];
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return _filteredPairs.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		switch(arg1)
		{
		case 0:
			return _filteredPairs.get(arg0).isChecked();
		case 1:
			return _filteredPairs.get(arg0).getName();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int arg0, int arg1) {
		switch(arg1)
		{
		case 0:
			_filteredPairs.get(arg0).setChecked((boolean)aValue);
			notifyChanged();
			break;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column){
		return column == 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int column) {
		switch(column)
		{
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		}
		return String.class;
	}
	
	private void applyFiltering(){
		_filteredPairs.clear();
		if(filter.equals("")){
			for(NameValuePair pair : _allPairs){
				_filteredPairs.add(pair);
			}
		}
		else {
			Pattern filterPattern = Pattern.compile(filter,Pattern.CASE_INSENSITIVE);
			for(NameValuePair pair : _allPairs){
				if(filterPattern.matcher(pair.getName().trim()).find()){
					_filteredPairs.add(pair);
				}
			}
		}
		fireTableDataChanged();
	}
	
	/**
	 * Removes all choices
	 */
	public void clear(){
		_allPairs.clear();
		_filteredPairs.clear();
	}
	
	/**
	 * Add a {@link NameValuePair} choice
	 * @param pair The new choice to add
	 */
	public void add(NameValuePair pair){
		_allPairs.add(pair);
		applyFiltering();
	}
	
	/**
	 * Add multiple {@link NameValuePair} choices.  More efficient than adding one
	 * by one.
	 * @param pairs List of choices to add
	 */
	public void add(List<NameValuePair> pairs){
		for(NameValuePair pair : pairs){
			_allPairs.add(pair);
		}
		applyFiltering();
	}
	
	/**
	 * Get the filter string currently being applied to the model.
	 * @return The filter string
	 */
	public String getFilter(){
		return filter;
	}
	/**
	 * Set the filter string currently being applied to the model.
	 * @param value The filter string
	 */
	public void setFilter(String value){
		filter = value;
		applyFiltering();
	}
	
	/**
	 * Clear the current filter string
	 */
	public void clearFilter(){
		setFilter("");
	}
	
	/**
	 * Check all visible choices, visible choices being those current matching the filter.
	 */
	public void checkVisible(){
		for(NameValuePair info : _filteredPairs){
			info.setChecked(true);
		}
		notifyChanged();
		fireTableDataChanged();
	}
	
	/**
	 * Uncheck all visible choices, visible choices being those current matching the filter.
	 */
	public void uncheckVisible(){
		for(NameValuePair info : _filteredPairs){
			info.setChecked(false);
		}
		notifyChanged();
		fireTableDataChanged();
	}
	
	/**
	 * Get all the checked choices
	 * @return A list containing {@link com.nuix.nx.NameValuePair} objects for the
	 * currently checked choices.
	 */
	public List<NameValuePair> getCheckedValues(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		for(NameValuePair pair : _allPairs){
			if(pair.isChecked())
				result.add(pair);
		}
		return result;
	}
	
	/**
	 * Check choices based on their value
	 * @param values The string values to check
	 */
	public void setCheckedValues(List<String> values){
		for(NameValuePair pair : _allPairs){
			if(values.contains(pair.getValue()))
				pair.setChecked(true);
		}
		notifyChanged();
		fireTableDataChanged();
	}
	
	/**
	 * Check choices based on their names (the displayed value)
	 * @param names The string names to check
	 */
	public void setCheckedNames(List<String> names){
		for(NameValuePair pair : _allPairs){
			if(names.contains(pair.getName()))
				pair.setChecked(true);
		}
		notifyChanged();
		fireTableDataChanged();
	}
	
	/**
	 * Get the number of choices which are checked
	 * @return The count of checked choices
	 */
	public int getCheckedCount(){
		int result = 0;
		for(NameValuePair pair : _allPairs){
			if(pair.isChecked())
				result++;
		}
		return result;
	}
	
	/**
	 * Get the total number of choices
	 * @return The count of choices, regardless of their checked state
	 */
	public int getTotalCount(){
		return _allPairs.size();
	}
}
