package com.nuix.nx.controls;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JButton;

import com.nuix.nx.dialogs.Prompt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * A swing control which provides a label, textbox and button to simplify
 * asking the user to specify a file to save to or load from.
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class FileSelectionControl extends JPanel {
	private JTextField txtFilePath;
	private JLabel lblLabel;
	private boolean useSave = true;
	
	private String fileDescription = "Text File";
	private String fileExtension = "txt";

	public FileSelectionControl() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblLabel = new JLabel("Label");
		GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblLabel.anchor = GridBagConstraints.EAST;
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 0;
		add(lblLabel, gbc_lblLabel);
		
		txtFilePath = new JTextField();
		txtFilePath.setToolTipText("File Path");
		GridBagConstraints gbc_txtFilePath = new GridBagConstraints();
		gbc_txtFilePath.insets = new Insets(0, 0, 0, 5);
		gbc_txtFilePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFilePath.gridx = 1;
		gbc_txtFilePath.gridy = 0;
		add(txtFilePath, gbc_txtFilePath);
		txtFilePath.setColumns(10);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = null;
				if(useSave)
					selectedFile = Prompt.saveFile(fileDescription, fileExtension,FileSelectionControl.this);
				else
					selectedFile = Prompt.openFile(fileDescription, fileExtension,FileSelectionControl.this);
				
				if(selectedFile!=null)
					txtFilePath.setText(selectedFile.getAbsolutePath());
			}
		});
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.gridx = 2;
		gbc_btnSelect.gridy = 0;
		add(btnSelect, gbc_btnSelect);

	}
	
	@Override
	public void setEnabled(boolean value){
		super.setEnabled(value);
		for(Component c : this.getComponents()){
			c.setEnabled(value);
		}
	}

	public void setLabel(String label){
		lblLabel.setText(label);
	}
	
	public void setFilePath(String path){
		txtFilePath.setText(path);
	}
	
	public String getFilePath(){
		return txtFilePath.getText();
	}
	
	public File getFile(){
		return new File(txtFilePath.getText());
	}
	
	public void setFile(File file){
		txtFilePath.setText(file.getAbsolutePath());
	}
	
	public void setUseSave(boolean value){
		useSave = value;
	}
	
	public boolean isUseSave(){
		return useSave;
	}
	
	public String getFileDescription(){
		return fileDescription;
	}
	
	public void setFileDescription(String description){
		fileDescription = description;
	}
	
	public String getFileExtension(){
		return fileExtension;
	}
	
	public void setFileExtension(String extension){
		fileExtension = extension;
	}
}
