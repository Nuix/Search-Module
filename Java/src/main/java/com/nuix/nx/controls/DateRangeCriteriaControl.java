package com.nuix.nx.controls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.JXDatePicker;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.other.DateRangeCriteria;

/**
 * This control provides display and editing of {@link DateRangeCriteria} in
 * the {@link QueryBuilderControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class DateRangeCriteriaControl extends JPanel implements DocumentListener {

	private DateRangeCriteria _criteria;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JButton btnRemoveCriteria;
	private JXDatePicker minDatePicker;
	private JXDatePicker maxDatePicker;
	private JComboBox<String> comboField;
	private boolean notifyEnabled = false;
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		if(!notifyEnabled) return;
		
		_criteria.setMinDate(minDatePicker.getDate());
		_criteria.setMaxDate(maxDatePicker.getDate());
		_criteria.setField(((JTextField)comboField.getEditor().getEditorComponent()).getText());
		
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}
	
	public DateRangeCriteriaControl(DateRangeCriteria criteria) {
		_criteria = criteria;
		setPreferredSize(new Dimension(300,300));
		
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblDateRange = new JLabel("Date Range");
		lblDateRange.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblDateRange = new GridBagConstraints();
		gbc_lblDateRange.gridwidth = 2;
		gbc_lblDateRange.insets = new Insets(0, 0, 5, 5);
		gbc_lblDateRange.gridx = 0;
		gbc_lblDateRange.gridy = 0;
		add(lblDateRange, gbc_lblDateRange);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 2;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(DateRangeCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
		toolBar.add(btnRemoveCriteria);
		
		comboField = new JComboBox<String>();
		comboField.setToolTipText("Field");
		JTextComponent tc = (JTextComponent) comboField.getEditor().getEditorComponent();
		tc.getDocument().addDocumentListener(this);
		
		JLabel lblField = new JLabel("Field");
		GridBagConstraints gbc_lblField = new GridBagConstraints();
		gbc_lblField.insets = new Insets(0, 0, 5, 5);
		gbc_lblField.anchor = GridBagConstraints.EAST;
		gbc_lblField.gridx = 0;
		gbc_lblField.gridy = 1;
		add(lblField, gbc_lblField);
		
		comboField.setEditable(true);
		GridBagConstraints gbc_comboField = new GridBagConstraints();
		gbc_comboField.insets = new Insets(0, 0, 5, 5);
		gbc_comboField.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboField.gridx = 1;
		gbc_comboField.gridy = 1;
		add(comboField, gbc_comboField);
		
		List<NameValuePair> fieldChoices = NuixDataBridge.getCommonDateFields();
		fieldChoices.addAll(NuixDataBridge.getCustomMetadataFields());
		for(int i = 0;i < fieldChoices.size();i++){
			comboField.addItem(fieldChoices.get(i).getName());
		}
		
		comboField.setSelectedItem(_criteria.getField());
		
		JLabel lblMinimum = new JLabel("Minimum");
		GridBagConstraints gbc_lblMinimum = new GridBagConstraints();
		gbc_lblMinimum.anchor = GridBagConstraints.EAST;
		gbc_lblMinimum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinimum.gridx = 0;
		gbc_lblMinimum.gridy = 3;
		add(lblMinimum, gbc_lblMinimum);
		
		minDatePicker = new JXDatePicker();
		minDatePicker.setFormats(new String[] {"yyyyMMdd"});
		minDatePicker.getEditor().getDocument().addDocumentListener(this);
		GridBagConstraints gbc_minDatePicker = new GridBagConstraints();
		gbc_minDatePicker.fill = GridBagConstraints.HORIZONTAL;
		gbc_minDatePicker.insets = new Insets(0, 0, 5, 5);
		gbc_minDatePicker.gridx = 1;
		gbc_minDatePicker.gridy = 3;
		add(minDatePicker, gbc_minDatePicker);
		
		JLabel lblMaximum = new JLabel("Maximum");
		GridBagConstraints gbc_lblMaximum = new GridBagConstraints();
		gbc_lblMaximum.anchor = GridBagConstraints.EAST;
		gbc_lblMaximum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximum.gridx = 0;
		gbc_lblMaximum.gridy = 4;
		add(lblMaximum, gbc_lblMaximum);
		
		maxDatePicker = new JXDatePicker();
		maxDatePicker.setFormats(new String[] {"yyyyMMdd"});
		maxDatePicker.getEditor().getDocument().addDocumentListener(this);
		GridBagConstraints gbc_maxDatePicker = new GridBagConstraints();
		gbc_maxDatePicker.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxDatePicker.insets = new Insets(0, 0, 5, 5);
		gbc_maxDatePicker.gridx = 1;
		gbc_maxDatePicker.gridy = 4;
		add(maxDatePicker, gbc_maxDatePicker);
		
		if(_criteria.getMinDate() != null)
			minDatePicker.setDate(_criteria.getMinDate());
		if(_criteria.getMaxDate() != null)
			maxDatePicker.setDate(_criteria.getMaxDate());
		notifyEnabled = true;
	}
	public DateRangeCriteria getCriteria() {
		return _criteria;
	}

	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		notifyChanged();	
	}
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		notifyChanged();
	}
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		notifyChanged();
	}
	
}
