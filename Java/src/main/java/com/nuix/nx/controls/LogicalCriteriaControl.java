package com.nuix.nx.controls;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.query.logical.LogicalQueryCriteria;
import java.awt.Font;

/**
 * This control provides display and editing of {@link LogicalQueryCriteria} in
 * the {@link QueryBuilderControl}
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class LogicalCriteriaControl extends JPanel {
	private LogicalQueryCriteria _criteria;
	private JButton btnRemoveCriteria;
	private JCheckBox chckbxCriteriaLabel;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}
	
	public LogicalCriteriaControl(LogicalQueryCriteria criteria) {
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(2, 2, 2, 2)));
		_criteria = criteria;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxCriteriaLabel = new JCheckBox(criteria.getLabel());
		chckbxCriteriaLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		chckbxCriteriaLabel.setSelected(criteria.getValue());
		chckbxCriteriaLabel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_criteria.setValue(chckbxCriteriaLabel.isSelected());
				notifyChanged();
			}
		});
		GridBagConstraints gbc_chckbxCriteriaLabel = new GridBagConstraints();
		gbc_chckbxCriteriaLabel.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxCriteriaLabel.gridx = 0;
		gbc_chckbxCriteriaLabel.gridy = 0;
		add(chckbxCriteriaLabel, gbc_chckbxCriteriaLabel);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		toolBar.add(btnRemoveCriteria);
		btnRemoveCriteria.setIcon(new ImageIcon(LogicalCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
	}
	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	public LogicalQueryCriteria getCriteria(){
		return _criteria;
	}
}
