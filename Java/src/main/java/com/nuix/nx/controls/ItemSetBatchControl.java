package com.nuix.nx.controls;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.common.ItemSetBatchCriteria;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ItemSetBatchControl extends JPanel implements ChangeListener {
	private JButton btnRemoveCriteria;
	private ItemSetBatchCriteria _criteria = new ItemSetBatchCriteria();
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JComboBox<String> comboBatch;
	private JComboBox<String> comboOriginals;
	private JComboBox<String> comboItemSet;
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}
	
	public ItemSetBatchControl(ItemSetBatchCriteria criteria) {
		setPreferredSize(new Dimension(300,300));
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblItemSetBatch = new JLabel("Item Set Batch");
		lblItemSetBatch.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblItemSetBatch = new GridBagConstraints();
		gbc_lblItemSetBatch.gridwidth = 2;
		gbc_lblItemSetBatch.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemSetBatch.gridx = 0;
		gbc_lblItemSetBatch.gridy = 0;
		add(lblItemSetBatch, gbc_lblItemSetBatch);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 2;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(ItemSetBatchControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		toolBar.add(btnRemoveCriteria);
		
		JLabel lblItemSet = new JLabel("Item Set:");
		GridBagConstraints gbc_lblItemSet = new GridBagConstraints();
		gbc_lblItemSet.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemSet.anchor = GridBagConstraints.EAST;
		gbc_lblItemSet.gridx = 0;
		gbc_lblItemSet.gridy = 1;
		add(lblItemSet, gbc_lblItemSet);
		
		comboItemSet = new JComboBox<String>();
		comboItemSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBatch.removeAllItems();
				List<String> batches = NuixDataBridge.getItemSetBatches((String)comboItemSet.getSelectedItem());
				for(String batch : batches){
					comboBatch.addItem(batch);
				}
				comboBatch.setSelectedIndex(0);
				changed(comboItemSet);
			}
		});
		GridBagConstraints gbc_comboItemSet = new GridBagConstraints();
		gbc_comboItemSet.gridwidth = 2;
		gbc_comboItemSet.insets = new Insets(0, 0, 5, 0);
		gbc_comboItemSet.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboItemSet.gridx = 1;
		gbc_comboItemSet.gridy = 1;
		add(comboItemSet, gbc_comboItemSet);
		
		JLabel lblBatch = new JLabel("Batch:");
		GridBagConstraints gbc_lblBatch = new GridBagConstraints();
		gbc_lblBatch.insets = new Insets(0, 0, 5, 5);
		gbc_lblBatch.anchor = GridBagConstraints.EAST;
		gbc_lblBatch.gridx = 0;
		gbc_lblBatch.gridy = 2;
		add(lblBatch, gbc_lblBatch);
		
		comboBatch = new JComboBox<String>();
		comboBatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changed(comboBatch);
			}
		});
		GridBagConstraints gbc_comboBatch = new GridBagConstraints();
		gbc_comboBatch.gridwidth = 2;
		gbc_comboBatch.insets = new Insets(0, 0, 5, 0);
		gbc_comboBatch.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBatch.gridx = 1;
		gbc_comboBatch.gridy = 2;
		add(comboBatch, gbc_comboBatch);
		
		comboOriginals = new JComboBox<String>();
		comboOriginals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changed(comboOriginals);
			}
		});
		comboOriginals.setModel(new DefaultComboBoxModel<String>(new String[] {"Originals and Duplicates", "Originals", "Duplicates"}));
		GridBagConstraints gbc_comboOriginals = new GridBagConstraints();
		gbc_comboOriginals.gridwidth = 2;
		gbc_comboOriginals.insets = new Insets(0, 0, 5, 0);
		gbc_comboOriginals.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboOriginals.gridx = 1;
		gbc_comboOriginals.gridy = 3;
		add(comboOriginals, gbc_comboOriginals);
		
		List<String> itemSetNames = NameValuePair.toNameList(NuixDataBridge.getItemSets());
		for(String name : itemSetNames){
			comboItemSet.addItem(name);	
		}
		comboItemSet.setSelectedIndex(0);
		
		setCriteria(criteria);
	}
	
	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	
	@Override
	public void changed(Component comp) {
		_criteria.setItemSet((String)comboItemSet.getSelectedItem());
		_criteria.setBatch((String)comboBatch.getSelectedItem());
		switch (comboOriginals.getSelectedIndex()) {
		case 0:
			_criteria.setOriginals(null);
			break;
		case 1:
			_criteria.setOriginals(true);
			break;
		default:
			_criteria.setOriginals(false);
			break;
		}
		notifyChanged();
	}
	
	public ItemSetBatchCriteria getCriteria() {
		return _criteria;
	}
	
	public void setCriteria(ItemSetBatchCriteria criteria) {
		comboItemSet.setSelectedItem(criteria.getItemSet());
		comboBatch.setSelectedItem(criteria.getBatch());
		if(criteria.getOriginals() == null){
			comboOriginals.setSelectedIndex(0);
		} else if(criteria.getOriginals() == true){
			comboOriginals.setSelectedIndex(1);
		} else {
			comboOriginals.setSelectedIndex(2);
		}
		
		this._criteria = criteria;
		changed(null);
	}
}
