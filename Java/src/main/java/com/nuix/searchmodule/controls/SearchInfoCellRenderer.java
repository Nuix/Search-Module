package com.nuix.searchmodule.controls;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;

import com.nuix.searchmodule.SearchTermInfo;
import com.nuix.searchmodule.query.QueryValidationInfo;

@SuppressWarnings("serial")
public class SearchInfoCellRenderer extends DefaultTableRenderer {
	protected Color errorColor = new Color(252,202,202);
	protected Color warningColor = new Color(255,247,158);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj,
	        boolean isSelected, boolean hasFocus, int row, int column) {
		int actualRow = table.convertRowIndexToModel(row);
		int actualCol = table.convertColumnIndexToModel(column);
	    Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, actualRow, actualCol);
	    SearchTermInfo info = ((SearchTermInfoTableModel)table.getModel()).getAtRow(actualRow);
	    if (actualCol == 1 && info.getValidations().size() > 0){
	    	if(isSelected) cell.setForeground(Color.BLACK);
	    	
	    	if(QueryValidationInfo.hasAnyErrors(info.getValidations()))
	    		cell.setBackground(errorColor);
	    	else
	    		cell.setBackground(warningColor);
	    }

	    return cell;
	}
}
