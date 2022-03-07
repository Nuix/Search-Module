package com.nuix.searchmodule.dialogs;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;

import com.generationjava.io.CsvReader;
import com.nuix.nx.MenuHelper;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.SearchModuleVersion;
import com.nuix.nx.controls.QueryBuilderControl;
import com.nuix.nx.dialogs.Prompt;
import com.nuix.nx.dialogs.QueryDialog;
import com.nuix.nx.query.QueryCriteria;
import com.nuix.nx.query.QueryGenerator;
import com.nuix.nx.query.other.LiteralCriteria;
import com.nuix.searchmodule.CategorySettings;
import com.nuix.searchmodule.SearchModulePersistedSettings;
import com.nuix.searchmodule.SearchModuleProject;
import com.nuix.searchmodule.SearchTermInfo;
import com.nuix.searchmodule.controls.CategorySettingsEditor;
import com.nuix.searchmodule.controls.SearchInfoCellRenderer;
import com.nuix.searchmodule.controls.SearchTermInfoTableModel;
import com.nuix.searchmodule.query.QueryValidationInfo;
import com.nuix.searchmodule.query.QueryValidator;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.ImageIcon;
import com.jidesoft.combobox.CheckBoxListExComboBox;
import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "serial", "unused" })
public class MainDialog extends JDialog {

	private SearchTermInfoTableModel searchTermModel = new SearchTermInfoTableModel();
	private final JPanel contentPanel = new JPanel();
	private boolean dialogResult = false;
	private QueryBuilderControl queryBuilderControl;
	private JMenuBar menuBar;
	private CategorySettingsEditor familySettingsEditor;
	private CategorySettingsEditor topLevelSettingsEditor;
	private CategorySettingsEditor uniqueTopLevelSettingsEditor;
	private CategorySettingsEditor uniqueHitsSettingsEditor;
	private CategorySettingsEditor uniqueFamilySettingsEditor;
	private CategorySettingsEditor hitsSettingsEditor;
	private JScrollPane scrollPane;
	private JXTable searchTermsTable;
	private JXButton btnAddTerm;
	private JXButton btnImportTextFile;
	private JXButton btnRemoveSelected;
	private JXLabel lblTermsCount;
	private JXButton btnRemoveAll;
	private JPanel panel;
	private JXButton btnReportFile;
	private JXTextField txtfldReportFilePath;
	private JXButton btnPasteTerms;
	private JXButton btnImportCsv;
	private JXTextField txtfldAddTermText;
	private JPanel panel_1;
	private JButton btnCheckAllFields;
	private JButton btnUncheckAllFields;
	private JButton btnBuildTerm;
	private JLabel lblErrorsCount;
	private JLabel lblWarningsCount;
	private JCheckBox chckbxOpenReportOnCompletion;
	private JCheckBox chckbxHandleExcludedItems;
	private String rootDirectory;
	private JPanel panel_2;
	private JCheckBox chckbxIncludeCoverSheet;
	private JCheckBox chckbxIncludeLogo;
	private JLabel lblCoverSheetInformation;
	private JScrollPane scrollPane_2;
	private JTextArea txtrCoverSheetInformation;
	private JPanel panel_3;
	private JCheckBox chckbxTagsRequireHits;
	private JCheckBox chckbxReportReviewableBy;
	private JTabbedPane tabbedPane;
	private JButton btnPlaceHolderReference;
	private CheckBoxListExComboBox defaultSearchFields;
	private JLabel lblDefaultSearchFields;
	
	private String[] defaultSearchFieldChoices = new String[] {"content", "properties", "name", "path-name", "evidence-metadata", "from", "to", "cc", "bcc"};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MainDialog(String rootDirectory) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainDialog.class.getResource("/com/nuix/nx/NuixIcon.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.rootDirectory = rootDirectory;
		setTitle("Search Module v"+SearchModuleVersion.getVersion());
		setSize(new Dimension(1440,900));
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			tabbedPane = new JTabbedPane(SwingConstants.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panelScopeQuery = new JPanel();
				tabbedPane.addTab("Scope Query", null, panelScopeQuery, null);
				panelScopeQuery.setLayout(new BorderLayout(0, 0));
				{
					queryBuilderControl = new QueryBuilderControl();
					panelScopeQuery.add(queryBuilderControl, BorderLayout.CENTER);
				}
			}
			{
				JPanel panelTerms = new JPanel();
				tabbedPane.addTab("Terms", null, panelTerms, null);
				GridBagLayout gbl_panelTerms = new GridBagLayout();
				gbl_panelTerms.columnWidths = new int[]{100, 100, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
				gbl_panelTerms.rowHeights = new int[]{0, 0, 0, 0};
				gbl_panelTerms.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panelTerms.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
				panelTerms.setLayout(gbl_panelTerms);
				{
					btnAddTerm = new JXButton();
					btnAddTerm.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTermFromTextField();
						}
					});
					{
						lblTermsCount = new JXLabel();
						lblTermsCount.setText("Terms: 0");
						GridBagConstraints gbc_lblTermsCount = new GridBagConstraints();
						gbc_lblTermsCount.anchor = GridBagConstraints.WEST;
						gbc_lblTermsCount.insets = new Insets(0, 0, 5, 5);
						gbc_lblTermsCount.gridx = 0;
						gbc_lblTermsCount.gridy = 0;
						panelTerms.add(lblTermsCount, gbc_lblTermsCount);
					}
					{
						btnBuildTerm = new JButton("Build Term");
						btnBuildTerm.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								QueryDialog dialog = new QueryDialog();
								dialog.setVisible(true);
								if(dialog.getDialogResult() == true){
									searchTermModel.addSearchTerm(dialog.getQueryGenerator().toQuery());
								}
							}
						});
						{
							lblErrorsCount = new JLabel("Errors: 0");
							GridBagConstraints gbc_lblErrorsCount = new GridBagConstraints();
							gbc_lblErrorsCount.anchor = GridBagConstraints.WEST;
							gbc_lblErrorsCount.insets = new Insets(0, 0, 5, 5);
							gbc_lblErrorsCount.gridx = 1;
							gbc_lblErrorsCount.gridy = 0;
							panelTerms.add(lblErrorsCount, gbc_lblErrorsCount);
						}
						{
							lblWarningsCount = new JLabel("Warnings: 0");
							GridBagConstraints gbc_lblWarningsCount = new GridBagConstraints();
							gbc_lblWarningsCount.anchor = GridBagConstraints.WEST;
							gbc_lblWarningsCount.insets = new Insets(0, 0, 5, 5);
							gbc_lblWarningsCount.gridx = 2;
							gbc_lblWarningsCount.gridy = 0;
							panelTerms.add(lblWarningsCount, gbc_lblWarningsCount);
						}
						GridBagConstraints gbc_btnBuildTerm = new GridBagConstraints();
						gbc_btnBuildTerm.insets = new Insets(0, 0, 5, 5);
						gbc_btnBuildTerm.gridx = 4;
						gbc_btnBuildTerm.gridy = 0;
						panelTerms.add(btnBuildTerm, gbc_btnBuildTerm);
					}
					btnAddTerm.setText("Add Term");
					GridBagConstraints gbc_btnAddTerm = new GridBagConstraints();
					gbc_btnAddTerm.insets = new Insets(0, 0, 5, 5);
					gbc_btnAddTerm.gridx = 6;
					gbc_btnAddTerm.gridy = 0;
					panelTerms.add(btnAddTerm, gbc_btnAddTerm);
				}
				{
					btnImportTextFile = new JXButton();
					btnImportTextFile.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							File importFile = Prompt.openTxt(MainDialog.this);
							if(importFile != null){
								try( BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(importFile), "UTF-8"))) {
									List<String> terms = new ArrayList<String>();
								    for(String line; (line = br.readLine()) != null; ) {
								    	if(!line.isEmpty()){
								    		terms.add(line);
								    	}
								    }
								    searchTermModel.addAllSearchTerms(terms);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					{
						txtfldAddTermText = new JXTextField();
						txtfldAddTermText.addKeyListener(new KeyAdapter() {
							@Override
							public void keyReleased(KeyEvent arg0) {
								if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
									addTermFromTextField();
									arg0.consume();
								}
							}
						});
						GridBagConstraints gbc_txtfldAddTermText = new GridBagConstraints();
						gbc_txtfldAddTermText.insets = new Insets(0, 0, 5, 5);
						gbc_txtfldAddTermText.fill = GridBagConstraints.HORIZONTAL;
						gbc_txtfldAddTermText.gridx = 7;
						gbc_txtfldAddTermText.gridy = 0;
						panelTerms.add(txtfldAddTermText, gbc_txtfldAddTermText);
					}
					{
						btnPasteTerms = new JXButton();
						btnPasteTerms.setText("Paste Terms");
						GridBagConstraints gbc_btnPasteTerms = new GridBagConstraints();
						gbc_btnPasteTerms.insets = new Insets(0, 0, 5, 5);
						gbc_btnPasteTerms.gridx = 9;
						gbc_btnPasteTerms.gridy = 0;
						panelTerms.add(btnPasteTerms, gbc_btnPasteTerms);
						btnPasteTerms.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								TextAreaInputDialog dialog = new TextAreaInputDialog(MainDialog.this);
								//TextAreaInputDialog dialog = new TextAreaInputDialog(null);
								dialog.setTitle("Paste Terms (One per Line)");
								dialog.setVisible(true);
								if(dialog.getDialogResult() == true){
									boolean isTabDelimited = dialog.isTabDelimited();
									String[] cols = null;
									for(String term : dialog.getText().split("\\s*\r?\n\\s*")){
										cols = null;
										if(isTabDelimited){
											cols = term.split("\t");
										}else{
											cols = term.split(",");
										}
										
										if(cols.length > 1){
											searchTermModel.addSearchTerm(cols[0],cols[1]);
										}else{
											searchTermModel.addSearchTerm(term);
										}
									}
								}
							}
						});
					}
					btnImportTextFile.setText("Import Text File");
					GridBagConstraints gbc_btnImportTextFile = new GridBagConstraints();
					gbc_btnImportTextFile.insets = new Insets(0, 0, 5, 5);
					gbc_btnImportTextFile.gridx = 10;
					gbc_btnImportTextFile.gridy = 0;
					panelTerms.add(btnImportTextFile, gbc_btnImportTextFile);
				}
				{
					btnRemoveSelected = new JXButton();
					btnRemoveSelected.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							int[] selectedRowIndices = searchTermsTable.getSelectedRows();
							searchTermModel.removeIndices(selectedRowIndices);
						}
					});
					{
						btnImportCsv = new JXButton();
						btnImportCsv.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								File importFile = Prompt.openCsv(MainDialog.this);
								if(importFile != null){
									importCsv(importFile);
								}
							}
						});
						btnImportCsv.setText("Import CSV");
						GridBagConstraints gbc_btnImportCsv = new GridBagConstraints();
						gbc_btnImportCsv.insets = new Insets(0, 0, 5, 5);
						gbc_btnImportCsv.gridx = 11;
						gbc_btnImportCsv.gridy = 0;
						panelTerms.add(btnImportCsv, gbc_btnImportCsv);
					}
					btnRemoveSelected.setText("Remove Selected");
					GridBagConstraints gbc_btnRemoveSelected = new GridBagConstraints();
					gbc_btnRemoveSelected.insets = new Insets(0, 0, 5, 5);
					gbc_btnRemoveSelected.gridx = 12;
					gbc_btnRemoveSelected.gridy = 0;
					panelTerms.add(btnRemoveSelected, gbc_btnRemoveSelected);
				}
				{
					btnRemoveAll = new JXButton();
					btnRemoveAll.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							searchTermModel.clear();
						}
					});
					btnRemoveAll.setText("Remove All");
					GridBagConstraints gbc_btnRemoveAll = new GridBagConstraints();
					gbc_btnRemoveAll.insets = new Insets(0, 0, 5, 5);
					gbc_btnRemoveAll.gridx = 13;
					gbc_btnRemoveAll.gridy = 0;
					panelTerms.add(btnRemoveAll, gbc_btnRemoveAll);
				}
				{
					panel_1 = new JPanel();
					panel_1.setBorder(new TitledBorder(null, "Fields", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					GridBagConstraints gbc_panel_1 = new GridBagConstraints();
					gbc_panel_1.gridwidth = 15;
					gbc_panel_1.insets = new Insets(0, 0, 5, 0);
					gbc_panel_1.fill = GridBagConstraints.BOTH;
					gbc_panel_1.gridx = 0;
					gbc_panel_1.gridy = 1;
					panelTerms.add(panel_1, gbc_panel_1);
					GridBagLayout gbl_panel_1 = new GridBagLayout();
					gbl_panel_1.columnWidths = new int[]{0, 600, 0, 0, 0, 0, 0};
					gbl_panel_1.rowHeights = new int[]{0, 0};
					gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					panel_1.setLayout(gbl_panel_1);
					{
						btnCheckAllFields = new JButton("Check All Fields");
						btnCheckAllFields.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								setAllFields(true);
							}
						});
						{
							lblDefaultSearchFields = new JLabel("Default Search Fields");
							GridBagConstraints gbc_lblDefaultSearchFields = new GridBagConstraints();
							gbc_lblDefaultSearchFields.fill = GridBagConstraints.HORIZONTAL;
							gbc_lblDefaultSearchFields.insets = new Insets(0, 0, 0, 5);
							gbc_lblDefaultSearchFields.gridx = 0;
							gbc_lblDefaultSearchFields.gridy = 0;
							panel_1.add(lblDefaultSearchFields, gbc_lblDefaultSearchFields);
						}
						{
							defaultSearchFields = new CheckBoxListExComboBox();
							defaultSearchFields.setModel(new DefaultComboBoxModel(defaultSearchFieldChoices));
							defaultSearchFields.setSelectedItem(new String[] {});
							GridBagConstraints gbc_defaultSearchFields = new GridBagConstraints();
							gbc_defaultSearchFields.insets = new Insets(0, 0, 0, 5);
							gbc_defaultSearchFields.fill = GridBagConstraints.HORIZONTAL;
							gbc_defaultSearchFields.gridx = 1;
							gbc_defaultSearchFields.gridy = 0;
							panel_1.add(defaultSearchFields, gbc_defaultSearchFields);
						}
						GridBagConstraints gbc_btnCheckAllFields = new GridBagConstraints();
						gbc_btnCheckAllFields.insets = new Insets(0, 0, 0, 5);
						gbc_btnCheckAllFields.gridx = 2;
						gbc_btnCheckAllFields.gridy = 0;
						panel_1.add(btnCheckAllFields, gbc_btnCheckAllFields);
					}
					{
						btnUncheckAllFields = new JButton("Uncheck All Fields");
						btnUncheckAllFields.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								setAllFields(false);
							}
						});
						GridBagConstraints gbc_btnUncheckAllFields = new GridBagConstraints();
						gbc_btnUncheckAllFields.insets = new Insets(0, 0, 0, 5);
						gbc_btnUncheckAllFields.gridx = 3;
						gbc_btnUncheckAllFields.gridy = 0;
						panel_1.add(btnUncheckAllFields, gbc_btnUncheckAllFields);
					}
					{
						chckbxHandleExcludedItems = new JCheckBox("Remove Excluded Items");
						chckbxHandleExcludedItems.setToolTipText("<html>\r\nWhen checked, items which are excluded (ie: has-exclusion:1 or Item.isExcluded equals true) <br>will be filtered out of every category before those items are used for any purpose by search module.\r\n</html>");
						chckbxHandleExcludedItems.setSelected(true);
						GridBagConstraints gbc_chckbxHandleExcludedItems = new GridBagConstraints();
						gbc_chckbxHandleExcludedItems.gridx = 5;
						gbc_chckbxHandleExcludedItems.gridy = 0;
						panel_1.add(chckbxHandleExcludedItems, gbc_chckbxHandleExcludedItems);
					}
				}
				{
					scrollPane = new JScrollPane();
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.gridwidth = 15;
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 2;
					panelTerms.add(scrollPane, gbc_scrollPane);
					{
						searchTermsTable = new JXTable(searchTermModel){
							@Override
							public String getToolTipText(MouseEvent arg0) {
				                java.awt.Point p = arg0.getPoint();
				                int rowIndex = rowAtPoint(p);
				                //Possible for this to get row index that is out of bounds somehow so
				                //need to put in a defensive check for this and ignore
				                if(rowIndex < 0 || rowIndex > searchTermModel.getRowCount()-1) return ""; 
				                int colIndex = columnAtPoint(p);
				                int realColumnIndex = convertColumnIndexToModel(colIndex);
				                int realRowIndex = this.convertRowIndexToModel(rowIndex);
				                
				                StringBuilder message = new StringBuilder();
				        		message.append("<html>");
				        		message.append("&#35;"+(realRowIndex+1)+"<br/>");
				        		for(QueryValidationInfo info : searchTermModel.getSearchTerms().get(realRowIndex).getValidations()){
				        			if (info.isError()) {
				        				message.append("<b>Error:</b> "+info.getMessage()+"<br/>");
				        			}else if(info.isWarning()){
				        				message.append("<b>Warning:</b> "+info.getMessage()+"<br/>");
				        			}
				        		}
				        		message.append("</html>");
				        		return message.toString();
							}
							
						};
						scrollPane.setViewportView(searchTermsTable);
						searchTermsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
						searchTermsTable.getColumn(0).setMinWidth(50);
						searchTermsTable.getColumn(0).setMaxWidth(50);
						searchTermsTable.getColumn(0).setPreferredWidth(50);
						searchTermModel.addTableModelListener(new TableModelListener(){
							@Override
							public void tableChanged(TableModelEvent event) {
								lblTermsCount.setText("Terms: "+Integer.toString(searchTermModel.getRowCount()));
								int errors = 0;
								int warnings = 0;
								for(SearchTermInfo info : searchTermModel.getSearchTerms()){
									errors += info.getValidations().stream().filter(v -> v.isError()).count();
									warnings += info.getValidations().stream().filter(v -> v.isWarning()).count();
								}
								lblErrorsCount.setText("Errors: "+errors);
								lblWarningsCount.setText("Warnings: "+warnings);
							}
						});
						SearchInfoCellRenderer highlightingRenderer = new SearchInfoCellRenderer();
						searchTermsTable.getColumn(0).setCellRenderer(highlightingRenderer);
						searchTermsTable.getColumn(1).setCellRenderer(highlightingRenderer);
					}
				}
			}
			{
				JPanel panelReporting = new JPanel();
				tabbedPane.addTab("Reporting", null, panelReporting, null);
				GridBagLayout gbl_panelReporting = new GridBagLayout();
				gbl_panelReporting.columnWidths = new int[]{0, 0, 0, 0};
				gbl_panelReporting.rowHeights = new int[]{0, 100, 0, 0, 0, 0, 0};
				gbl_panelReporting.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
				gbl_panelReporting.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
				panelReporting.setLayout(gbl_panelReporting);
				{
					panel = new JPanel();
					GridBagConstraints gbc_panel = new GridBagConstraints();
					gbc_panel.gridwidth = 3;
					gbc_panel.insets = new Insets(0, 0, 5, 0);
					gbc_panel.fill = GridBagConstraints.BOTH;
					gbc_panel.gridx = 0;
					gbc_panel.gridy = 0;
					panelReporting.add(panel, gbc_panel);
					GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[]{0, 0, 0};
					gbl_panel.rowHeights = new int[]{0, 0};
					gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					panel.setLayout(gbl_panel);
					{
						btnReportFile = new JXButton();
						btnReportFile.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								File reportFile = Prompt.saveXlsx(MainDialog.this);
								if(reportFile != null){
									txtfldReportFilePath.setText(reportFile.getAbsolutePath());
								}
							}
						});
						btnReportFile.setText("Report File");
						GridBagConstraints gbc_btnReportFile = new GridBagConstraints();
						gbc_btnReportFile.insets = new Insets(0, 0, 0, 5);
						gbc_btnReportFile.gridx = 0;
						gbc_btnReportFile.gridy = 0;
						panel.add(btnReportFile, gbc_btnReportFile);
					}
					{
						txtfldReportFilePath = new JXTextField();
						GridBagConstraints gbc_txtfldReportFilePath = new GridBagConstraints();
						gbc_txtfldReportFilePath.fill = GridBagConstraints.HORIZONTAL;
						gbc_txtfldReportFilePath.gridx = 1;
						gbc_txtfldReportFilePath.gridy = 0;
						panel.add(txtfldReportFilePath, gbc_txtfldReportFilePath);
					}
				}
				{
					panel_2 = new JPanel();
					GridBagConstraints gbc_panel_2 = new GridBagConstraints();
					gbc_panel_2.gridwidth = 3;
					gbc_panel_2.insets = new Insets(0, 0, 5, 0);
					gbc_panel_2.fill = GridBagConstraints.BOTH;
					gbc_panel_2.gridx = 0;
					gbc_panel_2.gridy = 1;
					panelReporting.add(panel_2, gbc_panel_2);
					GridBagLayout gbl_panel_2 = new GridBagLayout();
					gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
					gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0};
					gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
					gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
					panel_2.setLayout(gbl_panel_2);
					{
						chckbxIncludeCoverSheet = new JCheckBox("Include Cover Sheet");
						chckbxIncludeCoverSheet.addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent arg0) {
								txtrCoverSheetInformation.setEnabled(chckbxIncludeCoverSheet.isSelected());
							}
						});
						chckbxIncludeCoverSheet.setToolTipText("This checkbox determines whether a cover sheet will be included in the generated report.");
						GridBagConstraints gbc_chckbxIncludeCoverSheet = new GridBagConstraints();
						gbc_chckbxIncludeCoverSheet.insets = new Insets(0, 0, 5, 5);
						gbc_chckbxIncludeCoverSheet.gridx = 0;
						gbc_chckbxIncludeCoverSheet.gridy = 0;
						panel_2.add(chckbxIncludeCoverSheet, gbc_chckbxIncludeCoverSheet);
					}
					{
						lblCoverSheetInformation = new JLabel("Cover Sheet Information");
						GridBagConstraints gbc_lblCoverSheetInformation = new GridBagConstraints();
						gbc_lblCoverSheetInformation.anchor = GridBagConstraints.WEST;
						gbc_lblCoverSheetInformation.insets = new Insets(0, 0, 5, 0);
						gbc_lblCoverSheetInformation.gridx = 2;
						gbc_lblCoverSheetInformation.gridy = 0;
						panel_2.add(lblCoverSheetInformation, gbc_lblCoverSheetInformation);
					}
					{
						chckbxIncludeLogo = new JCheckBox("Include Logos");
						chckbxIncludeLogo.setToolTipText("This checkbox determines whether a logo will be included in the report.  If checked a file named \"logo.png\" is expected to be present in the same directory as the script.");
						GridBagConstraints gbc_chckbxIncludeLogo = new GridBagConstraints();
						gbc_chckbxIncludeLogo.anchor = GridBagConstraints.WEST;
						gbc_chckbxIncludeLogo.insets = new Insets(0, 0, 5, 5);
						gbc_chckbxIncludeLogo.gridx = 0;
						gbc_chckbxIncludeLogo.gridy = 1;
						panel_2.add(chckbxIncludeLogo, gbc_chckbxIncludeLogo);
					}
					{
						scrollPane_2 = new JScrollPane();
						GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
						gbc_scrollPane_2.gridheight = 2;
						gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
						gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
						gbc_scrollPane_2.gridx = 2;
						gbc_scrollPane_2.gridy = 1;
						panel_2.add(scrollPane_2, gbc_scrollPane_2);
						{
							txtrCoverSheetInformation = new JTextArea();
							txtrCoverSheetInformation.setEnabled(false);
							scrollPane_2.setViewportView(txtrCoverSheetInformation);
						}
					}
				}
				{
					panel_3 = new JPanel();
					GridBagConstraints gbc_panel_3 = new GridBagConstraints();
					gbc_panel_3.anchor = GridBagConstraints.WEST;
					gbc_panel_3.gridwidth = 3;
					gbc_panel_3.insets = new Insets(0, 0, 5, 5);
					gbc_panel_3.fill = GridBagConstraints.VERTICAL;
					gbc_panel_3.gridx = 0;
					gbc_panel_3.gridy = 2;
					panelReporting.add(panel_3, gbc_panel_3);
					{
						chckbxTagsRequireHits = new JCheckBox("Only Create Tags if There are Hits");
						chckbxTagsRequireHits.setToolTipText("This setting determines whether tags are created if there are zero hits for a given term/category.\r\nWhen checked tags are not created if there are no hits to apply them to.");
						panel_3.add(chckbxTagsRequireHits);
					}
					{
						chckbxReportReviewableBy = new JCheckBox("Report reviewable by custodian");
						panel_3.add(chckbxReportReviewableBy);
					}
					{
						btnPlaceHolderReference = new JButton("Place Holder Reference");
						btnPlaceHolderReference.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								try {
									Desktop.getDesktop().open(new File(MainDialog.this.getRootDirectory(),"PlaceholderReference.html"));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						btnPlaceHolderReference.setIcon(new ImageIcon(MainDialog.class.getResource("/com/nuix/nx/help.png")));
						panel_3.add(btnPlaceHolderReference);
					}
				}
				{
					hitsSettingsEditor = new CategorySettingsEditor(new CategorySettings("hits","Hits"));
					GridBagConstraints gbc_hitsSettingsEditor = new GridBagConstraints();
					gbc_hitsSettingsEditor.insets = new Insets(0, 0, 5, 5);
					gbc_hitsSettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_hitsSettingsEditor.gridx = 0;
					gbc_hitsSettingsEditor.gridy = 3;
					panelReporting.add(hitsSettingsEditor, gbc_hitsSettingsEditor);
				}
				{
					topLevelSettingsEditor = new CategorySettingsEditor(new CategorySettings("top_level_hits","Top Level Hits"));
					GridBagConstraints gbc_topLevelSettingsEditor = new GridBagConstraints();
					gbc_topLevelSettingsEditor.insets = new Insets(0, 0, 5, 5);
					gbc_topLevelSettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_topLevelSettingsEditor.gridx = 1;
					gbc_topLevelSettingsEditor.gridy = 3;
					panelReporting.add(topLevelSettingsEditor, gbc_topLevelSettingsEditor);
				}
				{
					familySettingsEditor = new CategorySettingsEditor(new CategorySettings("family_hits","Reviewable Hits"));
					GridBagConstraints gbc_familySettingsEditor = new GridBagConstraints();
					gbc_familySettingsEditor.insets = new Insets(0, 0, 5, 0);
					gbc_familySettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_familySettingsEditor.gridx = 2;
					gbc_familySettingsEditor.gridy = 3;
					panelReporting.add(familySettingsEditor, gbc_familySettingsEditor);
				}
				{
					uniqueHitsSettingsEditor = new CategorySettingsEditor(new CategorySettings("unique_hits","Unique Hits",false));
					GridBagConstraints gbc_uniqueHitsSettingsEditor = new GridBagConstraints();
					gbc_uniqueHitsSettingsEditor.insets = new Insets(0, 0, 5, 5);
					gbc_uniqueHitsSettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_uniqueHitsSettingsEditor.gridx = 0;
					gbc_uniqueHitsSettingsEditor.gridy = 4;
					panelReporting.add(uniqueHitsSettingsEditor, gbc_uniqueHitsSettingsEditor);
				}
				{
					uniqueTopLevelSettingsEditor = new CategorySettingsEditor(new CategorySettings("unique_top_level","Unique Top Level",false));
					GridBagConstraints gbc_uniqueTopLevelSettingsEditor = new GridBagConstraints();
					gbc_uniqueTopLevelSettingsEditor.insets = new Insets(0, 0, 5, 5);
					gbc_uniqueTopLevelSettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_uniqueTopLevelSettingsEditor.gridx = 1;
					gbc_uniqueTopLevelSettingsEditor.gridy = 4;
					panelReporting.add(uniqueTopLevelSettingsEditor, gbc_uniqueTopLevelSettingsEditor);
				}
				{
					uniqueFamilySettingsEditor = new CategorySettingsEditor(new CategorySettings("unique_family","Unique Reviewable",false));
					GridBagConstraints gbc_uniqueFamilySettingsEditor = new GridBagConstraints();
					gbc_uniqueFamilySettingsEditor.insets = new Insets(0, 0, 5, 0);
					gbc_uniqueFamilySettingsEditor.fill = GridBagConstraints.BOTH;
					gbc_uniqueFamilySettingsEditor.gridx = 2;
					gbc_uniqueFamilySettingsEditor.gridy = 4;
					panelReporting.add(uniqueFamilySettingsEditor, gbc_uniqueFamilySettingsEditor);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				chckbxOpenReportOnCompletion = new JCheckBox("Open Report on Completion");
				chckbxOpenReportOnCompletion.setSelected(SearchModulePersistedSettings.getOpenReportOnCompletion());
				buttonPane.add(chckbxOpenReportOnCompletion);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(currentSettingsAreValid()){
							SearchModulePersistedSettings.setOpenReportOnCompletion(chckbxOpenReportOnCompletion.isSelected());
							dialogResult = true;
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				//getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dialogResult = false;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			menuBar = new JMenuBar();
			setJMenuBar(menuBar);
		}
		
		//Populate menu bar with query builder entries
		queryBuilderControl.populateMenuBar(menuBar);
		
		//Obtain reference to file menu
		JMenu fileMenu = MenuHelper.findOrCreateTopLevelMenu(menuBar, "File");
		
		//Add a projects sub menu
		JMenu projectSubMenu = new JMenu("Projects");
		fileMenu.add(projectSubMenu);
		
		//Add a new project menu item
		JMenuItem newProject = new JMenuItem("New Project");
		projectSubMenu.add(newProject);
		newProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new project?  Any unsaved changes will be lost!", "Create New Project?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirmResult == JOptionPane.YES_OPTION){
					loadProject(new SearchModuleProject());
				}
			}
		});
		
		//Add a save project menu item
		JMenuItem saveProject = new JMenuItem("Save Project");
		projectSubMenu.add(saveProject);
		saveProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchModuleProject project = getProject();
				File projectFile = Prompt.saveFile("Search Module 3 Project", "sm3p",SearchModulePersistedSettings.getProjectLastDirectory(),MainDialog.this);
				if(projectFile != null){
					SearchModulePersistedSettings.setProjectLastDirectory(projectFile.getParentFile());
					try {
						project.saveToFile(projectFile);
					} catch (IOException e1) {
						Prompt.showError("Error while saving project:\n\n"+e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
		});
		
		//Add a open project menu item
		JMenuItem openProject = new JMenuItem("Open Project");
		projectSubMenu.add(openProject);
		openProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to open a project?  Any unsaved changes will be lost!", "Open Project?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirmResult == JOptionPane.YES_OPTION){
					File projectFile = Prompt.openFile("Search Module 3 Project", "sm3p",SearchModulePersistedSettings.getProjectLastDirectory(),MainDialog.this);
					if(projectFile != null){
						SearchModulePersistedSettings.setProjectLastDirectory(projectFile.getParentFile());
						try {
							loadProject(SearchModuleProject.loadFromFile(projectFile));
						} catch (IOException e1) {
							Prompt.showError("Error while loading project:\n\n"+e1.getMessage());
							e1.printStackTrace();
					}
					}
				}
			}
		});
		
		//Obtain reference to help menu
		JMenu helpMenu = MenuHelper.findOrCreateTopLevelMenu(menuBar, "Help");
		JMenuItem aboutSubMenu = new JMenuItem("About");
		helpMenu.add(aboutSubMenu);
		aboutSubMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.setVisible(true);
			}
		});
		
		JMenuItem helpFileSubMenu = new JMenuItem("Help File");
		helpMenu.add(helpFileSubMenu);
		helpFileSubMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(MainDialog.this.getRootDirectory(),"Help.html"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JMenuItem placeHolderHelpSubMenu = new JMenuItem("Place Holder Reference");
		helpMenu.add(placeHolderHelpSubMenu);
		placeHolderHelpSubMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(MainDialog.this.getRootDirectory(),"PlaceholderReference.html"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		loadProject(new SearchModuleProject());
	}
	
	public boolean currentSettingsAreValid(){
		//Validate user is reporting at least one thing
		if(getCategorySettingsList().stream().filter(c -> c.getReportCount()).count() < 1){
			String title = "Nothing being reported";
			String message = "Please include at least one category in your report.";
			JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//Check for scope level validation errors
		List<QueryCriteria> criteria = queryBuilderControl.getCriteria();
		for(QueryCriteria criterion : criteria){
			if(criterion instanceof LiteralCriteria){
				LiteralCriteria lc = (LiteralCriteria)criterion;
				if(QueryValidationInfo.hasAnyErrors(QueryValidator.validate(lc.getQuery()))){
					String title = "Query Errors";
					String message = "One or more of your scope literal criteria has errors which need to be fixed before continuing.";
					JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
					tabbedPane.setSelectedIndex(0);
					return false;
				}
			}
		}
		
		//Check for scope level validation warnings
		for(QueryCriteria criterion : criteria){
			if(criterion instanceof LiteralCriteria){
				LiteralCriteria lc = (LiteralCriteria)criterion;
				if(QueryValidationInfo.hasOnlyWarnings(QueryValidator.validate(lc.getQuery()))){
					String title = "Query Warnings";
					String message = "One or more scope criteria has warnings, are you sure you want to proceed?";
					int confirmResult = JOptionPane.showConfirmDialog(null, message, "Ignore Scope Query Warnings?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(confirmResult != JOptionPane.YES_OPTION){
						tabbedPane.setSelectedIndex(0);
						return false;
					}else{
						break;
					}
				}
			}
		}
		
		//Check for error level validation issues
		for(SearchTermInfo info : searchTermModel.getSearchTerms()){
			if(QueryValidationInfo.hasAnyErrors(info.getValidations())){
				String title = "Query Errors";
				String message = "One or more of your terms has errors which need to be fixed before continuing.";
				JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
				tabbedPane.setSelectedIndex(1);
				return false;
			}
		}
		
		//Notify user of warning level validation issues
		for(SearchTermInfo info : searchTermModel.getSearchTerms()){
			if(QueryValidationInfo.hasOnlyWarnings(info.getValidations())){
				String title = "Query Warnings";
				String message = "Some terms have warnings, are you sure you want to proceed?";
				int confirmResult = JOptionPane.showConfirmDialog(null, message, "Ignore Term Warnings?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirmResult != JOptionPane.YES_OPTION){
					tabbedPane.setSelectedIndex(1);
					return false;
				}else{
					break;
				}
			}
		}
		
		//Validate user has supplied at least one term
		if(searchTermModel.getRowCount() < 1){
			String title = "No Terms Provided";
			String message = "You have provided no terms, do you want to run anyways?";
			if(JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION){
				return false;
			}
		}
		
		//Warn on long "short name" values
		if(searchTermModel.getSearchTerms().stream().anyMatch(t -> t.getShortName().length() > 230)){
			String title = "Potential Tag Name Issue";
			String message = "One or more 'short name' values are longer than 230 characters.\n\n"+
					"Nuix tag name limit is 256 characters.  If applying tags using the 'short name' placeholder\n"+
					"and a tag ends up longer than 256 characters, Search Module will truncate the tag name to prevent errors.\n\n"+
					"Proceed?";
			if(JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION){
				return false;
			}
		}
		
		//Ensure at least one field is checked on the terms tab
		if(selectedDefaultFieldCount() < 1){
			String title = "No Fields Selected";
			String message = "You must select at least one default search field on the terms tab before continuing.";
			JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
			tabbedPane.setSelectedIndex(1);
			return false;
		}
		
		//Close all tabs if necessary
		boolean applyingCustomMetadata = false;
		for(CategorySettings cs : getCategorySettingsList()){
			if(cs.getApplyCategoryTags() || cs.getAppendCustomMetadata()){
				applyingCustomMetadata = true;
				break;
			}
		}
		
		if(NuixDataBridge.getWindow() != null && applyingCustomMetadata){
			String title = "Close all workbench tabs?";
			String message = "All workbench tabs must be closed before proceeding.  Close all workbench tabs?";
			if(JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION){
				return false;
			}else {
				NuixDataBridge.getWindow().closeAllTabs();
			}
		}
		
		return true;
	}
	
	protected int countCharacterOccurrences(String input, char c){
		int result = 0;
		for(int i = 0;i<input.length();i++){
			if(input.charAt(i) == c) result++;
		}
		return result;
	}
	
	public void fitToScreen(){
		fitToScreen(150);
	}

	public void fitToScreen(int difference){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth() - difference, screenSize.getHeight() - difference);
		setSize(screenSize);
		setLocationRelativeTo(null);
	}
	
	public void importCsv(String csvFile){
		importCsv(new File(csvFile));
	}
	
	public void importCsv(File csvFile) {
		try(FileReader fr = new FileReader(csvFile, StandardCharsets.UTF_8))
		{
			try(BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
				List<String> terms = new ArrayList<String>();
				CsvReader r = new CsvReader(br);
				String[] headers = r.readLine();
				List<SearchTermInfo> convertedTerms = new ArrayList<SearchTermInfo>();
				while(true){
					String[] values = r.readLine();
					if(values == null) break;
					if(values.length == 1){
						convertedTerms.add(new SearchTermInfo(values[0].trim()));
					}else{
						convertedTerms.add(new SearchTermInfo(values[0].trim(),values[1].trim()));
					}
				}
				searchTermModel.addAllSearchTermInfos(convertedTerms);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean getDialogResult(){
		return dialogResult;
	}
	
	public void resetDialogResult(){
		dialogResult = false;
	}
	
	public QueryGenerator getScopeQueryGenerator(){
		return queryBuilderControl.getQueryGenerator();
	}
	
	public void setScopeQueryGenerator(QueryGenerator scope){
		queryBuilderControl.setQueryGenerator(scope);
	}
	
	public List<CategorySettings> getCategorySettingsList(){
		List<CategorySettings> categorySettings = new ArrayList<CategorySettings>();
		categorySettings.add(hitsSettingsEditor.getSettings());
		categorySettings.add(topLevelSettingsEditor.getSettings());
		categorySettings.add(familySettingsEditor.getSettings());
		categorySettings.add(uniqueHitsSettingsEditor.getSettings());
		categorySettings.add(uniqueTopLevelSettingsEditor.getSettings());
		categorySettings.add(uniqueFamilySettingsEditor.getSettings());
		return categorySettings;
	}
	
	public Map<String,CategorySettings> getCategorySettings(){
		Map<String,CategorySettings> result = new HashMap<String,CategorySettings>();
		List<CategorySettings> categorySettings = getCategorySettingsList();
		
		for(CategorySettings cs : categorySettings){
			result.put(cs.getCategoryKey(), cs);
		}
		
		return result;
	}
	
	public List<SearchTermInfo> getTerms(){
		return searchTermModel.getSearchTerms();
	}
	
	public File getReportFile(){
		return new File(txtfldReportFilePath.getText());
	}
	
	public void setReportFilePath(String path){
		txtfldReportFilePath.setText(path);
	}
	
	public void setReportFilePath(File path){
		txtfldReportFilePath.setText(path.getAbsolutePath());
	}
	
	protected void addTermFromTextField() {
		String term = txtfldAddTermText.getText().trim();
		if(!term.isEmpty()){
			searchTermModel.addSearchTerm(term);
			txtfldAddTermText.setText("");
		}
	}
	
	protected void setAllFields(boolean value){
		if(value == true) {
			defaultSearchFields.setSelectedItem(defaultSearchFieldChoices);
		} else {
			defaultSearchFields.setSelectedItem(new String[] {});
		}
	}
	
	public List<String> getFields(){
		List<String> result = new ArrayList<String>();
		Object[] selectedFields = defaultSearchFields.getSelectedObjects();
		for (int i = 0; i < selectedFields.length; i++) {
			result.add((String)selectedFields[i]);
		}
		return result;
	}
	
	public int selectedDefaultFieldCount() {
		Object[] selectedFields = defaultSearchFields.getSelectedObjects();
		return selectedFields.length;
	}
	
	public void setFields(List<String> fields){
		defaultSearchFields.setSelectedItem(fields.toArray(new String[] {}));
	}
	
	public void loadProject(SearchModuleProject project){
		//Re-validate all terms!
		project.revalidateAllTerms();
		
		//Load scope
		setScopeQueryGenerator(project.getScopeQuery());
		
		//Load fields
		setFields(project.getTermFields());
		
		//Load terms
		searchTermModel.setSearchTerms(project.getSearchTerms());
		
		//Load up category settings
		hitsSettingsEditor.setSettings(project.getHitsCategory());
		topLevelSettingsEditor.setSettings(project.getTopLevelHitsCategory());
		familySettingsEditor.setSettings(project.getFamilyHitsCategory());
		uniqueHitsSettingsEditor.setSettings(project.getUniqueHitsCategory());
		uniqueTopLevelSettingsEditor.setSettings(project.getUniqueTopLevelCategory());
		uniqueFamilySettingsEditor.setSettings(project.getUniqueFamilyCategory());
		
		//Load whether exclusions are handled
		chckbxHandleExcludedItems.setSelected(project.getHandleExclusions());
		
		//Load where a cover sheet is included
		chckbxIncludeCoverSheet.setSelected(project.getIncludeCoverSheet());
		
		//Load where a cover sheet is included
		chckbxIncludeLogo.setSelected(project.getIncludeLogo());
		
		//Load cover sheet info
		txtrCoverSheetInformation.setText(project.getCoverSheetInformation());
		
		//Load whether tags require hits
		chckbxTagsRequireHits.setSelected(project.getTagsRequireHits());
		
		//Load whether a sheet with reviewable by custodian be generated
		chckbxReportReviewableBy.setSelected(project.getReportReviewableByCustodian());
	}
	
	public void setOpenReportOnCompletion(boolean value){
		chckbxOpenReportOnCompletion.setSelected(value);
	}
	
	public boolean getOpenReportOnCompletion(){
		return chckbxOpenReportOnCompletion.isSelected();
	}
	
	public SearchModuleProject getProject(){
		SearchModuleProject project = new SearchModuleProject();
		//Store scope
		project.setScopeQuery(getScopeQueryGenerator());
		
		//Store fields
		project.setTermFields(getFields());
		
		//Store terms
		project.setSearchTerms(getTerms());
		
		//Store category settings
		project.setCategoriesSettings(getCategorySettingsList());
		
		//Store whether exclusions are handled
		project.setHandleExclusions(chckbxHandleExcludedItems.isSelected());
		
		//Whether to include a cover sheet
		project.setIncludeCoverSheet(chckbxIncludeCoverSheet.isSelected());
		
		//Whether to include a logo
		project.setIncludeLogo(chckbxIncludeLogo.isSelected());
		
		//Cover sheet info
		project.setCoverSheetInformation(txtrCoverSheetInformation.getText());
		
		//Are tags created if there are no hits?
		project.setTagsRequireHits(chckbxTagsRequireHits.isSelected());
		
		//Will a sheet with reviewable by custodian be generated?
		project.setReportReviewableByCustodian(chckbxReportReviewableBy.isSelected());
		
		return project;
	}
	public JLabel getLblWarningsCount() {
		return lblWarningsCount;
	}
	public JLabel getLblErrorsCount() {
		return lblErrorsCount;
	}
	
	public boolean getHandleExcludedItems(){
		return chckbxHandleExcludedItems.isSelected();
	}
	
	public void setHandleExcludedItems(boolean value){
		chckbxHandleExcludedItems.setSelected(value);
	}
	
	public String getRootDirectory(){
		return this.rootDirectory;
	}
}
