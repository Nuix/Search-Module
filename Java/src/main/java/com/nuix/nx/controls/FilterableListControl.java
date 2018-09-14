package com.nuix.nx.controls;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;

/**
 * A control which allows a user to select one or more choices.  Additionally the control
 * allows filtering the visible choices to make checking choices in a large list easier.
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class FilterableListControl extends JPanel implements ChangeListener {
	private JTextField txtFilter;
	private JTable valuesTable;
	private FilterableListModel tableModel;
	private JLabel lblCheckedCount;
	private Timer filterTimer;
	private GridBagConstraints gbc_scrollPane;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JLabel lblFilter;
	private JToolBar toolBar_1;
	private JPanel panel;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}

	public FilterableListControl() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblFilter = new JLabel("Filter");
		GridBagConstraints gbc_lblFilter = new GridBagConstraints();
		gbc_lblFilter.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilter.anchor = GridBagConstraints.EAST;
		gbc_lblFilter.gridx = 0;
		gbc_lblFilter.gridy = 0;
		add(lblFilter, gbc_lblFilter);
		
		txtFilter = new JTextField();
		GridBagConstraints gbc_txtFilter = new GridBagConstraints();
		gbc_txtFilter.gridwidth = 2;
		gbc_txtFilter.insets = new Insets(0, 0, 5, 5);
		gbc_txtFilter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFilter.gridx = 1;
		gbc_txtFilter.gridy = 0;
		add(txtFilter, gbc_txtFilter);
		txtFilter.setColumns(10);
		txtFilter.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					startApplyFilterTimer();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					startApplyFilterTimer();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					startApplyFilterTimer();
				}
			});
		
		toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		GridBagConstraints gbc_toolBar_1 = new GridBagConstraints();
		gbc_toolBar_1.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar_1.gridx = 3;
		gbc_toolBar_1.gridy = 0;
		add(toolBar_1, gbc_toolBar_1);
		
		JButton btnClearFilter = new JButton("");
		btnClearFilter.setIcon(new ImageIcon(FilterableListControl.class.getResource("/com/nuix/nx/cancel.png")));
		btnClearFilter.setToolTipText("Clear Filter");
		toolBar_1.add(btnClearFilter);
		btnClearFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFilter.setText("");
				getModel().clearFilter();
			}
		});
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		JLabel lblChecked = new JLabel("Checked:");
		panel.add(lblChecked);
		
		lblCheckedCount = new JLabel("0");
		panel.add(lblCheckedCount);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.EAST;
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		panel_1.add(toolBar);
		
		JButton btnCheckVisible = new JButton("");
		toolBar.add(btnCheckVisible);
		btnCheckVisible.setIcon(new ImageIcon(FilterableListControl.class.getResource("/com/nuix/nx/accept.png")));
		btnCheckVisible.setToolTipText("Check Visible");
		
		JButton btnUncheckVisible = new JButton("");
		toolBar.add(btnUncheckVisible);
		btnUncheckVisible.setToolTipText("Uncheck Visible");
		btnUncheckVisible.setIcon(new ImageIcon(FilterableListControl.class.getResource("/com/nuix/nx/unaccept.png")));
		btnUncheckVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.uncheckVisible();
			}
		});
		btnCheckVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.checkVisible();
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		valuesTable = new JTable();
		scrollPane.setViewportView(valuesTable);
		tableModel = new FilterableListModel();
		tableModel.addChangeListener(this);
		valuesTable.setModel(tableModel);
		valuesTable.getColumnModel().getColumn(0).setMaxWidth(32);
	}
	
	private void startApplyFilterTimer(){
		if(filterTimer != null){
			filterTimer.cancel();
		}
		filterTimer = new Timer();
		TimerTask action = new TimerTask() {
	        public void run() {
	        	getModel().setFilter(txtFilter.getText());
	        	filterTimer.cancel();
	        }
	    };
		filterTimer.schedule(action,500);
	}
	
	/**
	 * Allows you to hide the filtering portion of the control.
	 * @param visible When true, filtering portion is visible.
	 */
	public void setFilteringControlsVisible(boolean visible){
		lblFilter.setVisible(visible);
		txtFilter.setVisible(visible);
		toolBar_1.setVisible(visible);
		panel.setVisible(visible);
		panel_1.setVisible(visible);
		remove(scrollPane);
		if(visible){
			gbc_scrollPane.gridy = 2;
			gbc_scrollPane.gridheight = 1;
		}
		else{
			gbc_scrollPane.gridy = 0;
			gbc_scrollPane.gridheight = 3;
		}
		add(scrollPane, gbc_scrollPane);
		this.revalidate();
		this.repaint();
	}

	/**
	 * Get the backing {@link FilterableListModel}
	 * @return The backing model
	 */
	public FilterableListModel getModel(){
		return tableModel;
	}
	
	private void updateCheckedCount(){
		int checked = tableModel.getCheckedCount();
		int total = tableModel.getTotalCount();
		lblCheckedCount.setText(Integer.toString(checked) + " / " + Integer.toString(total));
	}
	
	/**
	 * Used by underlying components to signal generic changes.
	 */
	@Override
	public void changed(Component comp) {
		updateCheckedCount();
		notifyChanged();
	}
	
}
