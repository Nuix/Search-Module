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

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.query.other.RangeCriteria;

/**
 * This control provides display and editing of {@link RangeCriteria} in
 * the {@link QueryBuilderControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class RangeCriteriaControl extends JPanel implements DocumentListener {
	private JTextField txtMinimumValue;
	private JTextField txtMaximumValue;
	private JComboBox<String> comboField;
	
	private RangeCriteria _criteria;
	
	private boolean notifyEnabled = false;
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JButton btnRemoveCriteria;
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		if(!notifyEnabled)return;
		_criteria.setMinValue(txtMinimumValue.getText());
		_criteria.setMaxValue(txtMaximumValue.getText());
		_criteria.setField(((JTextField)comboField.getEditor().getEditorComponent()).getText());
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}

	public RangeCriteriaControl(RangeCriteria criteria) {
		_criteria = criteria;
		
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		
		setPreferredSize(new Dimension(300,300));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRangeCrtieria = new JLabel("Range Crtieria");
		lblRangeCrtieria.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblRangeCrtieria = new GridBagConstraints();
		gbc_lblRangeCrtieria.gridwidth = 2;
		gbc_lblRangeCrtieria.insets = new Insets(0, 0, 5, 5);
		gbc_lblRangeCrtieria.gridx = 0;
		gbc_lblRangeCrtieria.gridy = 0;
		add(lblRangeCrtieria, gbc_lblRangeCrtieria);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 2;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(RangeCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
		toolBar.add(btnRemoveCriteria);
		
		JLabel lblField = new JLabel("Field");
		GridBagConstraints gbc_lblField = new GridBagConstraints();
		gbc_lblField.insets = new Insets(0, 0, 5, 5);
		gbc_lblField.anchor = GridBagConstraints.EAST;
		gbc_lblField.gridx = 0;
		gbc_lblField.gridy = 1;
		add(lblField, gbc_lblField);
		
		comboField = new JComboBox<String>();
		comboField.setToolTipText("Field");
		comboField.setEditable(true);
		GridBagConstraints gbc_comboField = new GridBagConstraints();
		gbc_comboField.insets = new Insets(0, 0, 5, 5);
		gbc_comboField.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboField.gridx = 1;
		gbc_comboField.gridy = 1;
		add(comboField, gbc_comboField);
		
		JTextComponent tc = (JTextComponent) comboField.getEditor().getEditorComponent();
		tc.getDocument().addDocumentListener(this);
		
		List<NameValuePair> fieldChoices = NuixDataBridge.getCommonIntegerFields();
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
		
		txtMinimumValue = new JTextField();
		txtMinimumValue.setToolTipText("Minimum Value");
		GridBagConstraints gbc_txtMinimumValue = new GridBagConstraints();
		gbc_txtMinimumValue.insets = new Insets(0, 0, 5, 5);
		gbc_txtMinimumValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMinimumValue.gridx = 1;
		gbc_txtMinimumValue.gridy = 3;
		add(txtMinimumValue, gbc_txtMinimumValue);
		txtMinimumValue.setColumns(10);
		
		JLabel lblMaximum = new JLabel("Maximum");
		GridBagConstraints gbc_lblMaximum = new GridBagConstraints();
		gbc_lblMaximum.anchor = GridBagConstraints.EAST;
		gbc_lblMaximum.insets = new Insets(0, 0, 0, 5);
		gbc_lblMaximum.gridx = 0;
		gbc_lblMaximum.gridy = 4;
		add(lblMaximum, gbc_lblMaximum);
		
		txtMaximumValue = new JTextField();
		txtMaximumValue.setToolTipText("Maximum Value");
		GridBagConstraints gbc_txtMaximumValue = new GridBagConstraints();
		gbc_txtMaximumValue.insets = new Insets(0, 0, 0, 5);
		gbc_txtMaximumValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMaximumValue.gridx = 1;
		gbc_txtMaximumValue.gridy = 4;
		add(txtMaximumValue, gbc_txtMaximumValue);
		txtMaximumValue.setColumns(10);
		
		txtMinimumValue.getDocument().addDocumentListener(this);
		txtMaximumValue.getDocument().addDocumentListener(this);

		txtMinimumValue.setText(_criteria.getMinValue());
		txtMaximumValue.setText(_criteria.getMaxValue());
		
		notifyEnabled = true;
	}
	
	public RangeCriteria getCriteria(){
		return _criteria;
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
	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
}
