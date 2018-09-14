package com.nuix.nx.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.query.common.CommonQueryCriteria;
import com.nuix.nx.query.common.MultiValueCriteriaType;

import java.awt.Font;

/**
 * This control provides display and editing of {@link CommonQueryCriteria} in
 * the {@link QueryBuilderControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class CommonCriteriaControl extends JPanel implements ChangeListener {
	class ComboValue{
		public String name = "";
		public MultiValueCriteriaType value;
		
		public ComboValue(String n, MultiValueCriteriaType v){
			name = n;
			value = v;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
	
	private JLabel lblCriteriaLabel;
	private JButton btnRemoveCriteria;
	private FilterableListControl filterableListControl;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}
	
	private CommonQueryCriteria _criteria;
	private JComboBox<ComboValue> comboCriteriaType;

	public CommonCriteriaControl(CommonQueryCriteria criteria) {
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		_criteria = criteria;
		
		setPreferredSize(new Dimension(300,300));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblCriteriaLabel = new JLabel(criteria.getLabel());
		lblCriteriaLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblCriteriaLabel = new GridBagConstraints();
		gbc_lblCriteriaLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblCriteriaLabel.gridx = 0;
		gbc_lblCriteriaLabel.gridy = 0;
		add(lblCriteriaLabel, gbc_lblCriteriaLabel);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(CommonCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
		toolBar.add(btnRemoveCriteria);
		
		comboCriteriaType = new JComboBox<ComboValue>();
		comboCriteriaType.addItem(new ComboValue("Having Any",MultiValueCriteriaType.HAVINGANY));
		comboCriteriaType.addItem(new ComboValue("Not Having Any",MultiValueCriteriaType.NOTHAVINGANY));
		if(!criteria.isSingular()){
			comboCriteriaType.addItem(new ComboValue("Having All",MultiValueCriteriaType.HAVINGALL));
			comboCriteriaType.addItem(new ComboValue("Not Having All",MultiValueCriteriaType.NOTHAVINGALL));	
		}
		
		for(int i=0;i<comboCriteriaType.getItemCount();i++){
			if(comboCriteriaType.getItemAt(i).value == _criteria.getCriteriaType()){
				comboCriteriaType.setSelectedIndex(i);
				break;
			}
		}
		comboCriteriaType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_criteria.setCriteriaType(comboCriteriaType.getItemAt(comboCriteriaType.getSelectedIndex()).value);
				notifyChanged();
			}
		});
		GridBagConstraints gbc_comboCriteriaType = new GridBagConstraints();
		gbc_comboCriteriaType.gridwidth = 2;
		gbc_comboCriteriaType.insets = new Insets(0, 0, 5, 5);
		gbc_comboCriteriaType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboCriteriaType.gridx = 0;
		gbc_comboCriteriaType.gridy = 1;
		add(comboCriteriaType, gbc_comboCriteriaType);
		
		filterableListControl = new FilterableListControl();
		GridBagConstraints gbc_filterableListControl = new GridBagConstraints();
		gbc_filterableListControl.gridwidth = 2;
		gbc_filterableListControl.fill = GridBagConstraints.BOTH;
		gbc_filterableListControl.gridx = 0;
		gbc_filterableListControl.gridy = 2;
		add(filterableListControl, gbc_filterableListControl);
		filterableListControl.getModel().add(criteria.getChoices());
		filterableListControl.getModel().setCheckedNames(NameValuePair.toNameList(_criteria.getValues()));
		_criteria.setValues(filterableListControl.getModel().getCheckedValues());
		filterableListControl.addChangeListener(this);
	}
	
	/**
	 * Get the criteria associated with this control
	 * @return The {@link com.nuix.nx.query.common CommonQueryCriteria} edited by this control.
	 */
	public CommonQueryCriteria getCriteria(){
		return _criteria;
	}

	/**
	 * Get the remove criteria button
	 * @return The "Remove Critieria" JButton attached to this control.
	 */
	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	
	/**
	 * Get the underlying {@link FilterableListControl}
	 * @return The underlying {@link FilterableListControl}
	 */
	public FilterableListControl getFilterableListControl() {
		return filterableListControl;
	}
	
	/**
	 * Called by underlying components to signal their state has changed.
	 * @param comp The component generating the change event.
	 */
	@Override
	public void changed(Component comp) {
		_criteria.setValues(filterableListControl.getModel().getCheckedValues());
		notifyChanged();
	}
}
