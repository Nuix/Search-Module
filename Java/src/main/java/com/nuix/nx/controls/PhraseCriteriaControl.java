package com.nuix.nx.controls;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JToolBar;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.other.PhraseCriteria;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 * This control provides display and editing of {@link PhraseCriteria} in
 * the {@link QueryBuilderControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class PhraseCriteriaControl extends JPanel implements DocumentListener, ChangeListener {
	private PhraseCriteria _criteria;
	private JButton btnRemoveCriteria;

	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JTextArea txtrPhrase;
	private FilterableListControl filterableListControl;
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}
	
	public PhraseCriteriaControl(PhraseCriteria criteria) {
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		_criteria = criteria;
		setPreferredSize(new Dimension(350,300));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{175, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblPhrase = new JLabel("Phrase");
		lblPhrase.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblPhrase = new GridBagConstraints();
		gbc_lblPhrase.gridwidth = 3;
		gbc_lblPhrase.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhrase.gridx = 0;
		gbc_lblPhrase.gridy = 0;
		add(lblPhrase, gbc_lblPhrase);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 3;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(PhraseCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
		toolBar.add(btnRemoveCriteria);
		
		filterableListControl = new FilterableListControl();
		filterableListControl.setFilteringControlsVisible(false);
		GridBagConstraints gbc_filterableListControl = new GridBagConstraints();
		gbc_filterableListControl.gridheight = 2;
		gbc_filterableListControl.insets = new Insets(0, 0, 0, 5);
		gbc_filterableListControl.fill = GridBagConstraints.BOTH;
		gbc_filterableListControl.gridx = 0;
		gbc_filterableListControl.gridy = 1;
		add(filterableListControl, gbc_filterableListControl);
		filterableListControl.getModel().add(NuixDataBridge.getCommonTextFields());
		filterableListControl.getModel().add(NuixDataBridge.getCustomMetadataFields());
		filterableListControl.getModel().setCheckedValues(_criteria.getFields());
		filterableListControl.addChangeListener(this);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedIndex()==0)
					_criteria.setJoinOr(true);
				else
					_criteria.setJoinOr(false);
				notifyChanged();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"OR", "AND"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);
		if(_criteria.getJoinOr())
			comboBox.setSelectedIndex(0);
		else
			comboBox.setSelectedIndex(1);
		
		final JCheckBox chckbxNegated = new JCheckBox("Negated");
		chckbxNegated.setSelected(_criteria.isNegated());
		chckbxNegated.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_criteria.setNegated(chckbxNegated.isSelected());
				notifyChanged();
			}
		});
		GridBagConstraints gbc_chckbxNegated = new GridBagConstraints();
		gbc_chckbxNegated.anchor = GridBagConstraints.WEST;
		gbc_chckbxNegated.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNegated.gridx = 2;
		gbc_chckbxNegated.gridy = 1;
		add(chckbxNegated, gbc_chckbxNegated);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		txtrPhrase = new JTextArea();
		txtrPhrase.setToolTipText("Phrase");
		scrollPane.setViewportView(txtrPhrase);
		txtrPhrase.setText(_criteria.getPhrase());
		txtrPhrase.getDocument().addDocumentListener(this);
	}
	
	public PhraseCriteria getCriteria(){
		return _criteria;
	}

	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		_criteria.setPhrase(txtrPhrase.getText());
		notifyChanged();
	}
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		_criteria.setPhrase(txtrPhrase.getText());
		notifyChanged();
	}
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		_criteria.setPhrase(txtrPhrase.getText());
		notifyChanged();
	}
	@Override
	public void changed(Component comp) {
		List<NameValuePair> checkedValues = filterableListControl.getModel().getCheckedValues(); 
		_criteria.setFields(NameValuePair.toValueList(checkedValues));
		notifyChanged();
	}
	
}
