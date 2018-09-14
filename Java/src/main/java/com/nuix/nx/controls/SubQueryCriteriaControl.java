package com.nuix.nx.controls;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.ScrollPaneConstants;

import java.awt.Font;

import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.dialogs.QueryDialog;
import com.nuix.nx.query.QueryGenerator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SubQueryCriteriaControl extends JPanel {
	private JButton btnRemoveCriteria;
	
	private QueryGenerator _generator; 
	private JTextArea txtrSubQueryPreview;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}

	public SubQueryCriteriaControl(QueryGenerator generator) {
		_generator = generator;
		setPreferredSize(new Dimension(300,300));
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblSubQueryCriteria = new JLabel("Sub Query Criteria");
		lblSubQueryCriteria.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblSubQueryCriteria = new GridBagConstraints();
		gbc_lblSubQueryCriteria.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubQueryCriteria.gridx = 0;
		gbc_lblSubQueryCriteria.gridy = 0;
		add(lblSubQueryCriteria, gbc_lblSubQueryCriteria);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(SubQueryCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		btnRemoveCriteria.setToolTipText("Remove Criteria");
		toolBar.add(btnRemoveCriteria);
		
		JButton btnEditSubQuery = new JButton("Edit Sub Query");
		btnEditSubQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Create dialog to edit sub query
				QueryDialog dialog = new QueryDialog();
				dialog.setQueryGenerator(_generator.createClone());
				dialog.setTitle("Edit Sub Query");
				dialog.setVisible(true);
				//If the user hit okay, then set the edited generator
				//as this instances new generator
				if(dialog.getDialogResult()){
					_generator.setCriteria(dialog.getQueryGenerator().getCriteria());
					if(dialog.getQueryGenerator().isJoinedWithAnd())
						_generator.setJoinWithAnd();
					else
						_generator.setJoinWithOr();
					txtrSubQueryPreview.setText(_generator.toQuery());
					notifyChanged();
				}
			}
		});
		GridBagConstraints gbc_btnEditSubQuery = new GridBagConstraints();
		gbc_btnEditSubQuery.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditSubQuery.gridx = 0;
		gbc_btnEditSubQuery.gridy = 1;
		add(btnEditSubQuery, gbc_btnEditSubQuery);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		txtrSubQueryPreview = new JTextArea();
		txtrSubQueryPreview.setWrapStyleWord(true);
		txtrSubQueryPreview.setLineWrap(true);
		txtrSubQueryPreview.setBackground(Color.WHITE);
		txtrSubQueryPreview.setEditable(false);
		scrollPane.setViewportView(txtrSubQueryPreview);
		txtrSubQueryPreview.setText(_generator.toQuery());
	}

	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}
	
	public QueryGenerator getGenerator(){
		return _generator;
	}
}
