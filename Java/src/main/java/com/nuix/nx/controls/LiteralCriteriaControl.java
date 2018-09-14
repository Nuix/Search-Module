package com.nuix.nx.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nuix.nx.query.other.LiteralCriteria;
import com.nuix.searchmodule.query.QueryValidationInfo;
import com.nuix.searchmodule.query.QueryValidator;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class LiteralCriteriaControl extends JPanel implements DocumentListener, ChangeListener {
	private LiteralCriteria _criteria;
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private JTextArea txtrQuery;
	private JButton btnRemoveCriteria;
	private Timer validationRefreshTimer;
	private JButton btnErrorsButton;
	private String validationSummaryMessage = "No issues detected";
	private JLabel lblLiteralCriteria;
	
	public LiteralCriteriaControl(LiteralCriteria criteria) {
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		_criteria = criteria;
		setPreferredSize(new Dimension(700,300));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		btnErrorsButton = new JButton("");
		btnErrorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(LiteralCriteriaControl.this, LiteralCriteriaControl.this.validationSummaryMessage);
			}
		});
		btnErrorsButton.setToolTipText("No issues detected");
		btnErrorsButton.setEnabled(false);
		btnErrorsButton.setIcon(new ImageIcon(LiteralCriteriaControl.class.getResource("/com/nuix/nx/controls/error.png")));
		GridBagConstraints gbc_btnErrorsButton = new GridBagConstraints();
		gbc_btnErrorsButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnErrorsButton.gridx = 0;
		gbc_btnErrorsButton.gridy = 0;
		add(btnErrorsButton, gbc_btnErrorsButton);
		
		lblLiteralCriteria = new JLabel("Literal Criteria");
		lblLiteralCriteria.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblLiteralCriteria = new GridBagConstraints();
		gbc_lblLiteralCriteria.insets = new Insets(0, 0, 5, 5);
		gbc_lblLiteralCriteria.gridx = 1;
		gbc_lblLiteralCriteria.gridy = 0;
		add(lblLiteralCriteria, gbc_lblLiteralCriteria);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 2;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		btnRemoveCriteria = new JButton("");
		btnRemoveCriteria.setIcon(new ImageIcon(LiteralCriteriaControl.class.getResource("/com/nuix/nx/bin_closed.png")));
		toolBar.add(btnRemoveCriteria);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		validationRefreshTimer = new Timer(500, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				List<QueryValidationInfo> validations = QueryValidator.validate(txtrQuery.getText());
				if(validations.size() > 0){
					btnErrorsButton.setEnabled(true);
					LiteralCriteriaControl.this.validationSummaryMessage = QueryValidationInfo.toMessage(validations);
					btnErrorsButton.setToolTipText("Detected "+validations.size()+" issues");
					lblLiteralCriteria.setBackground(Color.RED);
					lblLiteralCriteria.setForeground(Color.WHITE);
					lblLiteralCriteria.setOpaque(true);
				} else {
					btnErrorsButton.setEnabled(false);
					btnErrorsButton.setToolTipText("No issues detected");
					LiteralCriteriaControl.this.validationSummaryMessage = "No issues detected";
					lblLiteralCriteria.setBackground(new Color(240,240,240));
					lblLiteralCriteria.setForeground(Color.BLACK);
					lblLiteralCriteria.setOpaque(false);
				}
			}
		});
		validationRefreshTimer.setRepeats(false);
		
		txtrQuery = new JTextArea();
		txtrQuery.setFont(new Font("Verdana", Font.PLAIN, 13));
		txtrQuery.setWrapStyleWord(true);
		txtrQuery.setLineWrap(true);
		txtrQuery.getDocument().addDocumentListener(this);
		scrollPane.setViewportView(txtrQuery);
		txtrQuery.setText(_criteria.getQuery());
	}
	
	public void addChangeListener(ChangeListener listener){ changeListeners.add(listener); }
	private void notifyChanged(){
		validationRefreshTimer.restart();
		_criteria.setQuery(txtrQuery.getText());
		for(ChangeListener l : changeListeners){
			l.changed(this);
		}
	}

	@Override
	public void changed(Component comp) {
		notifyChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		notifyChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		notifyChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		notifyChanged();
	}
	
	public JButton getBtnRemoveCriteria() {
		return btnRemoveCriteria;
	}

	public LiteralCriteria getCriteria() {
		return _criteria;
	}
}
