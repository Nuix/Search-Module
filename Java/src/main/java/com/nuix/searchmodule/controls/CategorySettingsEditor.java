package com.nuix.searchmodule.controls;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import com.nuix.searchmodule.CategorySettings;

@SuppressWarnings("serial")
public class CategorySettingsEditor extends JPanel {

	private CategorySettings settings;
	private JTextField txtCategoryTagTemplate;
	private JCheckBox chckbxReport;
	private JCheckBox chckbxReportTotalAudited;
	private JRadioButton chckbxApplyCategoryTag;
	private JXPanel panelSubOptions;
	private JRadioButton chckbxApplyTermTag;
	private JTextField txtTermTagTemplate;
	private final ButtonGroup buttonGroupTags = new ButtonGroup();
	private JRadioButton rdbtnNoTagging;
	private JXPanel panel;
	private JXPanel panel_1;
	private JRadioButton rdbtnDontApply;
	private JRadioButton rdbtnAppend;
	private JRadioButton rdbtnOverwrite;
	private final ButtonGroup buttonGroupCustomMetadata = new ButtonGroup();
	private JTextField txtCustomMetadataTemplate;
	private JXLabel lblValue;
	private JXLabel lblField;
	private JXTextField textFieldCustomMetadataField;
	private JCheckBox chckbxReportFileSize;
	
	public CategorySettingsEditor(){
		this(new CategorySettings());
	}
	
	public CategorySettingsEditor(CategorySettings s){
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Category", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0))));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxReport = new JCheckBox("Include");
		chckbxReport.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				panelSubOptions.setVisible(chckbxReport.isSelected());
			}
		});
		GridBagConstraints gbc_chckbxReport = new GridBagConstraints();
		gbc_chckbxReport.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxReport.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxReport.gridx = 0;
		gbc_chckbxReport.gridy = 0;
		add(chckbxReport, gbc_chckbxReport);
		
		panelSubOptions = new JXPanel();
		GridBagConstraints gbc_panelSubOptions = new GridBagConstraints();
		gbc_panelSubOptions.fill = GridBagConstraints.BOTH;
		gbc_panelSubOptions.gridx = 1;
		gbc_panelSubOptions.gridy = 0;
		add(panelSubOptions, gbc_panelSubOptions);
		GridBagLayout gbl_panelSubOptions = new GridBagLayout();
		gbl_panelSubOptions.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panelSubOptions.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelSubOptions.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelSubOptions.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelSubOptions.setLayout(gbl_panelSubOptions);
		
		chckbxReportTotalAudited = new JCheckBox("Report Audited Size");
		GridBagConstraints gbc_chckbxReportTotalAudited = new GridBagConstraints();
		gbc_chckbxReportTotalAudited.anchor = GridBagConstraints.WEST;
		gbc_chckbxReportTotalAudited.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxReportTotalAudited.gridx = 1;
		gbc_chckbxReportTotalAudited.gridy = 0;
		panelSubOptions.add(chckbxReportTotalAudited, gbc_chckbxReportTotalAudited);
		
		chckbxReportFileSize = new JCheckBox("Report File Size");
		GridBagConstraints gbc_chckbxReportFileSize = new GridBagConstraints();
		gbc_chckbxReportFileSize.anchor = GridBagConstraints.WEST;
		gbc_chckbxReportFileSize.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxReportFileSize.gridx = 2;
		gbc_chckbxReportFileSize.gridy = 0;
		panelSubOptions.add(chckbxReportFileSize, gbc_chckbxReportFileSize);
		
		panel_1 = new JXPanel();
		panel_1.setBorder(new TitledBorder(null, "Tags", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		panelSubOptions.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		rdbtnNoTagging = new JRadioButton("No Tagging");
		GridBagConstraints gbc_rdbtnNoTagging = new GridBagConstraints();
		gbc_rdbtnNoTagging.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNoTagging.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNoTagging.gridx = 0;
		gbc_rdbtnNoTagging.gridy = 0;
		panel_1.add(rdbtnNoTagging, gbc_rdbtnNoTagging);
		buttonGroupTags.add(rdbtnNoTagging);
		rdbtnNoTagging.setSelected(true);
		
		chckbxApplyCategoryTag = new JRadioButton("Per Category");
		GridBagConstraints gbc_chckbxApplyCategoryTag = new GridBagConstraints();
		gbc_chckbxApplyCategoryTag.anchor = GridBagConstraints.WEST;
		gbc_chckbxApplyCategoryTag.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxApplyCategoryTag.gridx = 1;
		gbc_chckbxApplyCategoryTag.gridy = 0;
		panel_1.add(chckbxApplyCategoryTag, gbc_chckbxApplyCategoryTag);
		chckbxApplyCategoryTag.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				txtCategoryTagTemplate.setEnabled(chckbxApplyCategoryTag.isSelected());
			}
		});
		buttonGroupTags.add(chckbxApplyCategoryTag);
		
		chckbxApplyTermTag = new JRadioButton("Per Term");
		GridBagConstraints gbc_chckbxApplyTermTag = new GridBagConstraints();
		gbc_chckbxApplyTermTag.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxApplyTermTag.anchor = GridBagConstraints.WEST;
		gbc_chckbxApplyTermTag.gridx = 2;
		gbc_chckbxApplyTermTag.gridy = 0;
		panel_1.add(chckbxApplyTermTag, gbc_chckbxApplyTermTag);
		chckbxApplyTermTag.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				txtTermTagTemplate.setEnabled(chckbxApplyTermTag.isSelected());
			}
		});
		buttonGroupTags.add(chckbxApplyTermTag);
		
		txtCategoryTagTemplate = new JTextField();
		txtCategoryTagTemplate.setEnabled(false);
		GridBagConstraints gbc_txtCategoryTagTemplate = new GridBagConstraints();
		gbc_txtCategoryTagTemplate.gridwidth = 4;
		gbc_txtCategoryTagTemplate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCategoryTagTemplate.insets = new Insets(0, 0, 5, 5);
		gbc_txtCategoryTagTemplate.gridx = 0;
		gbc_txtCategoryTagTemplate.gridy = 1;
		panel_1.add(txtCategoryTagTemplate, gbc_txtCategoryTagTemplate);
		txtCategoryTagTemplate.setText("Tag Template");
		txtCategoryTagTemplate.setColumns(10);
		
		txtTermTagTemplate = new JTextField();
		txtTermTagTemplate.setEnabled(false);
		GridBagConstraints gbc_txtTermTagTemplate = new GridBagConstraints();
		gbc_txtTermTagTemplate.gridwidth = 4;
		gbc_txtTermTagTemplate.insets = new Insets(0, 0, 0, 5);
		gbc_txtTermTagTemplate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTermTagTemplate.gridx = 0;
		gbc_txtTermTagTemplate.gridy = 2;
		panel_1.add(txtTermTagTemplate, gbc_txtTermTagTemplate);
		txtTermTagTemplate.setText("Term Tag Template");
		txtTermTagTemplate.setColumns(10);
		
		panel = new JXPanel();
		panel.setBorder(new TitledBorder(null, "Custom Metdata", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		panelSubOptions.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblField = new JXLabel();
		lblField.setText("Field");
		GridBagConstraints gbc_lblField = new GridBagConstraints();
		gbc_lblField.anchor = GridBagConstraints.EAST;
		gbc_lblField.insets = new Insets(0, 0, 5, 5);
		gbc_lblField.gridx = 0;
		gbc_lblField.gridy = 1;
		panel.add(lblField, gbc_lblField);
		
		textFieldCustomMetadataField = new JXTextField();
		textFieldCustomMetadataField.setEnabled(false);
		GridBagConstraints gbc_textFieldCustomMetadataField = new GridBagConstraints();
		gbc_textFieldCustomMetadataField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCustomMetadataField.gridwidth = 4;
		gbc_textFieldCustomMetadataField.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldCustomMetadataField.gridx = 1;
		gbc_textFieldCustomMetadataField.gridy = 1;
		panel.add(textFieldCustomMetadataField, gbc_textFieldCustomMetadataField);
		
		lblValue = new JXLabel();
		lblValue.setText("Value");
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.insets = new Insets(0, 0, 0, 5);
		gbc_lblValue.anchor = GridBagConstraints.EAST;
		gbc_lblValue.gridx = 0;
		gbc_lblValue.gridy = 2;
		panel.add(lblValue, gbc_lblValue);
		
		rdbtnDontApply = new JRadioButton("Dont Apply");
		
		buttonGroupCustomMetadata.add(rdbtnDontApply);
		rdbtnDontApply.setSelected(true);
		GridBagConstraints gbc_rdbtnDontApply = new GridBagConstraints();
		gbc_rdbtnDontApply.gridwidth = 2;
		gbc_rdbtnDontApply.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnDontApply.gridx = 0;
		gbc_rdbtnDontApply.gridy = 0;
		panel.add(rdbtnDontApply, gbc_rdbtnDontApply);
		
		rdbtnAppend = new JRadioButton("Append");
		buttonGroupCustomMetadata.add(rdbtnAppend);
		GridBagConstraints gbc_rdbtnAppend = new GridBagConstraints();
		gbc_rdbtnAppend.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAppend.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnAppend.gridx = 2;
		gbc_rdbtnAppend.gridy = 0;
		panel.add(rdbtnAppend, gbc_rdbtnAppend);
		
		rdbtnOverwrite = new JRadioButton("Overwrite");
		rdbtnOverwrite.setVisible(false);
		rdbtnOverwrite.setEnabled(false);
		buttonGroupCustomMetadata.add(rdbtnOverwrite);
		GridBagConstraints gbc_rdbtnOverwrite = new GridBagConstraints();
		gbc_rdbtnOverwrite.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOverwrite.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOverwrite.gridx = 3;
		gbc_rdbtnOverwrite.gridy = 0;
		panel.add(rdbtnOverwrite, gbc_rdbtnOverwrite);
		rdbtnOverwrite.setVisible(false);
		
		txtCustomMetadataTemplate = new JXTextField();
		txtCustomMetadataTemplate.setEnabled(false);
		GridBagConstraints gbc_txtCustomMetadataTemplate = new GridBagConstraints();
		gbc_txtCustomMetadataTemplate.gridwidth = 4;
		gbc_txtCustomMetadataTemplate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCustomMetadataTemplate.gridx = 1;
		gbc_txtCustomMetadataTemplate.gridy = 2;
		panel.add(txtCustomMetadataTemplate, gbc_txtCustomMetadataTemplate);
		txtCustomMetadataTemplate.setColumns(10);
		
		rdbtnDontApply.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				txtCustomMetadataTemplate.setEnabled(!rdbtnDontApply.isSelected());
				textFieldCustomMetadataField.setEnabled(!rdbtnDontApply.isSelected());
			}
		});
		
		settings = s;
		updateControls();
	}
	
	protected void updateControls(){
		if(settings != null){
			((TitledBorder)((CompoundBorder)getBorder()).getInsideBorder()).setTitle(settings.getCategoryTitle());
			chckbxReport.setSelected(settings.getReportCount());
			panelSubOptions.setVisible(settings.getReportCount());
			chckbxReportTotalAudited.setSelected(settings.getReportAudited());
			chckbxReportFileSize.setSelected(settings.getReportFileSize());
			chckbxApplyCategoryTag.setSelected(settings.getApplyCategoryTags());
			txtCategoryTagTemplate.setText(settings.getCategoryTagTemplate());
			chckbxApplyTermTag.setSelected(settings.getApplyTermTags());
			txtTermTagTemplate.setText(settings.getTermTagTemplate());
			if(settings.getApplyCustomMetadata()){
				if(settings.getAppendCustomMetadata()){
					rdbtnAppend.setSelected(true);
				}else{
					rdbtnOverwrite.setSelected(true);
				}
			}else{
				rdbtnDontApply.setSelected(true);
			}
			txtCustomMetadataTemplate.setText(settings.getCustomMetadataTemplate());
			textFieldCustomMetadataField.setText(settings.getCustomMetadataField());
		}
	}

	public CategorySettings getSettings() {
		settings.setReportCount(chckbxReport.isSelected());
		settings.setReportAudited(chckbxReportTotalAudited.isSelected());
		settings.setReportFileSize(chckbxReportFileSize.isSelected());
		settings.setApplyCategoryTags(chckbxApplyCategoryTag.isSelected());
		settings.setCategoryTagTemplate(txtCategoryTagTemplate.getText());
		settings.setApplyTermTags(chckbxApplyTermTag.isSelected());
		settings.setTermTagTemplate(txtTermTagTemplate.getText());
		settings.setApplyCustomMetadata(!rdbtnDontApply.isSelected());
		settings.setAppendCustomMetadata(rdbtnAppend.isSelected());
		settings.setCustomMetadataTemplate(txtCustomMetadataTemplate.getText());
		settings.setCustomMetadataField(textFieldCustomMetadataField.getText());
		return settings;
	}

	public void setSettings(CategorySettings settings) {
		this.settings = settings;
		updateControls();
	}
	
	public void lockReported(){
		chckbxReport.setSelected(true);
		chckbxReport.setEnabled(false);
	}

	public JXTextField getTextFieldCustomMetadataField() {
		return textFieldCustomMetadataField;
	}
}
