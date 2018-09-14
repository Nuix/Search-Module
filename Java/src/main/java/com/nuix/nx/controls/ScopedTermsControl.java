package com.nuix.nx.controls;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.google.common.base.Joiner;
import com.nuix.nx.MenuHelper;
import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.dialogs.Prompt;
import com.nuix.nx.query.QueryGenerator;

/**
 * Similar to a {@link QueryBuilderControl}, this control also presents a text area
 * for the user to provide a list of terms (one per line).
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class ScopedTermsControl extends JPanel {
	private QueryBuilderControl queryBuilderControl;
	private JTextArea txtrTermsList;
	private JPanel panelTermFields;
	
	private List<JCheckBox> fieldCheckBoxes = new ArrayList<JCheckBox>();

	public ScopedTermsControl() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{275, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 2.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		panelTermFields = new JPanel();
		panelTermFields.setBorder(new TitledBorder(null, "Term Fields", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelTermFields = new GridBagConstraints();
		gbc_panelTermFields.anchor = GridBagConstraints.WEST;
		gbc_panelTermFields.gridwidth = 2;
		gbc_panelTermFields.insets = new Insets(0, 0, 5, 5);
		gbc_panelTermFields.fill = GridBagConstraints.VERTICAL;
		gbc_panelTermFields.gridx = 0;
		gbc_panelTermFields.gridy = 0;
		add(panelTermFields, gbc_panelTermFields);
		
		JPanel panelTermsList = new JPanel();
		panelTermsList.setBorder(new TitledBorder(null, "Terms", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelTermsList = new GridBagConstraints();
		gbc_panelTermsList.insets = new Insets(0, 0, 0, 5);
		gbc_panelTermsList.fill = GridBagConstraints.BOTH;
		gbc_panelTermsList.gridx = 0;
		gbc_panelTermsList.gridy = 1;
		add(panelTermsList, gbc_panelTermsList);
		panelTermsList.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelTermsList.add(scrollPane);
		
		txtrTermsList = new JTextArea();
		scrollPane.setViewportView(txtrTermsList);
		
		JPanel panelScopeQuery = new JPanel();
		panelScopeQuery.setBorder(new TitledBorder(null, "Scope Query", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelScopeQuery = new GridBagConstraints();
		gbc_panelScopeQuery.fill = GridBagConstraints.BOTH;
		gbc_panelScopeQuery.gridx = 1;
		gbc_panelScopeQuery.gridy = 1;
		add(panelScopeQuery, gbc_panelScopeQuery);
		panelScopeQuery.setLayout(new BorderLayout(0, 0));
		
		queryBuilderControl = new QueryBuilderControl();
		panelScopeQuery.add(queryBuilderControl);
		
		for(NameValuePair textField : NuixDataBridge.getCommonTextFields()){
			JCheckBox chkBox = new JCheckBox(textField.getName());
			fieldCheckBoxes.add(chkBox);
			panelTermFields.add(chkBox);
		}
	}
	
	public QueryGenerator getQueryGenerator(){
		return queryBuilderControl.getQueryGenerator();
	}
	
	public void setQueryGenerator(QueryGenerator generator){
		queryBuilderControl.setQueryGenerator(generator);
	}
	
	public List<String> getTermFields(){
		List<String> result = new ArrayList<String>();
		for(JCheckBox chkBox : fieldCheckBoxes){
			if(chkBox.isSelected())
				result.add(chkBox.getText());
		}
		return result;
	}
	
	public void setTermFields(List<String> fields){
		for(JCheckBox chkBox : fieldCheckBoxes){
			if(fields.contains(chkBox.getText()))
				chkBox.setSelected(true);
			else
				chkBox.setSelected(false);
		}
	}
	
	public List<String> getTerms(){
		String s[] = txtrTermsList.getText().split("\\r?\\n");
		for(int i=0;i<s.length;i++){
			s[i] = s[i].trim();
		}
	    List<String> terms = new ArrayList<String>();
	    for(int i=0;i<s.length;i++){
			if(!s[i].isEmpty() && !terms.contains(s[i]) && !s[i].toLowerCase().equals("not") &&
					!s[i].toLowerCase().equals("and") && !s[i].toLowerCase().equals("or")){
				terms.add(s[i]);
			}
		}
	    return terms;
	}
	
	public void setTerms(List<String> terms){
		String termsJoined = Joiner.on("\n").join(terms);
		txtrTermsList.setText(termsJoined);
	}

	public void populateMenuBar(JMenuBar menuBar){
		queryBuilderControl.populateMenuBar(menuBar);
		
		JMenu fileMenu = MenuHelper.findOrCreateTopLevelMenu(menuBar, "File");
		
		JMenu termsSubMenu = new JMenu("Terms");
		
		JMenuItem termsMenuItem = new JMenuItem("Save Terms");
		termsMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File selectedFile = Prompt.saveTxt(ScopedTermsControl.this);
				if(selectedFile != null){
					PrintWriter termsFile = null;
					try {
						termsFile = new PrintWriter(selectedFile.getAbsolutePath(), "UTF8");
						termsFile.println(Joiner.on("\n").join(getTerms()));
						
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace();
					} finally {
						if(termsFile != null){
							termsFile.close();
						}
					}
					
				}
			}
		});
		termsSubMenu.add(termsMenuItem);
		
		termsMenuItem = new JMenuItem("Open Terms");
		termsMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File selectedFile = Prompt.openTxt(ScopedTermsControl.this);
				if(selectedFile != null){
					List<String> lines;
					try {
						lines = Files.readAllLines(Paths.get(selectedFile.getAbsolutePath()), StandardCharsets.UTF_8);
						setTerms(lines);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		termsSubMenu.add(termsMenuItem);
		
		termsMenuItem = new JMenuItem("Clear Terms");
		termsMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtrTermsList.setText("");
			}
		});
		termsSubMenu.add(termsMenuItem);
		
		fileMenu.add(termsSubMenu);
	}

}
