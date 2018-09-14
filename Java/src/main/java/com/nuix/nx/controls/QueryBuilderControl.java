package com.nuix.nx.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import nuix.Case;

import com.nuix.nx.MenuHelper;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.dialogs.Prompt;
import com.nuix.nx.query.QueryCriteria;
import com.nuix.nx.query.QueryGenerator;
import com.nuix.nx.query.common.BatchLoadCriteria;
import com.nuix.nx.query.common.CommonQueryCriteria;
import com.nuix.nx.query.common.CustodianCriteria;
import com.nuix.nx.query.common.DigestListCriteria;
import com.nuix.nx.query.common.EvidenceCriteria;
import com.nuix.nx.query.common.ExclusionCriteria;
import com.nuix.nx.query.common.FlagCriteria;
import com.nuix.nx.query.common.ItemSetBatchCriteria;
import com.nuix.nx.query.common.ItemSetCriteria;
import com.nuix.nx.query.common.ItemSetDuplicatesCriteria;
import com.nuix.nx.query.common.ItemSetOriginalsCriteria;
import com.nuix.nx.query.common.KindCriteria;
import com.nuix.nx.query.common.LanguageCriteria;
import com.nuix.nx.query.common.MimeTypeCriteria;
import com.nuix.nx.query.common.OriginalExtensionCriteria;
import com.nuix.nx.query.common.PathKindCriteria;
import com.nuix.nx.query.common.ProductionSetsCriteria;
import com.nuix.nx.query.common.TagCriteria;
import com.nuix.nx.query.logical.CanContainTextCriteria;
import com.nuix.nx.query.logical.ContainsTextCriteria;
import com.nuix.nx.query.logical.HasBinaryCriteria;
import com.nuix.nx.query.logical.HasCommentCriteria;
import com.nuix.nx.query.logical.HasCommunicationCriteria;
import com.nuix.nx.query.logical.HasCustodianCriteria;
import com.nuix.nx.query.logical.HasEmbeddedDataCriteria;
import com.nuix.nx.query.logical.HasExclusionCriteria;
import com.nuix.nx.query.logical.HasImageCriteria;
import com.nuix.nx.query.logical.HasItemSetCriteria;
import com.nuix.nx.query.logical.HasProductionSetCriteria;
import com.nuix.nx.query.logical.HasTagCriteria;
import com.nuix.nx.query.logical.IsDeletedCriteria;
import com.nuix.nx.query.logical.IsEncryptedCriteria;
import com.nuix.nx.query.logical.IsItemSetOriginalCriteria;
import com.nuix.nx.query.logical.LogicalQueryCriteria;
import com.nuix.nx.query.negatable.CorruptedContainerCriteria;
import com.nuix.nx.query.negatable.CorruptedCriteria;
import com.nuix.nx.query.negatable.NegatableQueryCriteria;
import com.nuix.nx.query.negatable.NonSearchablePDFCriteria;
import com.nuix.nx.query.negatable.TextUpdatedCriteria;
import com.nuix.nx.query.negatable.UnrecognisedCriteria;
import com.nuix.nx.query.negatable.UnsupportedContainerCriteria;
import com.nuix.nx.query.negatable.UnsupportedCriteria;
import com.nuix.nx.query.other.DateRangeCriteria;
import com.nuix.nx.query.other.LiteralCriteria;
import com.nuix.nx.query.other.PhraseCriteria;
import com.nuix.nx.query.other.RangeCriteria;

/**
 * This control allows the user to display and edit a {@link QueryGenerator} object.
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class QueryBuilderControl extends JPanel implements ChangeListener {
	private JPanel panelLogical;
	private JPanel panelQueryPreview;
	private JScrollPane scrollCommon;
	private JPanel panelCommonCriteria;
	private JScrollPane scrollLogical;
	private JPanel panelLogicalCriteria;
	
	private boolean isLoadingGenerator = false;
	
	private QueryGenerator _generator = new QueryGenerator();
	private JScrollPane scrollPreview;
	private JTextArea txtrQueryPreview;
	private JLabel lblCriteriaJoinOperator;
	private JComboBox<String> comboCriteriaJoinOperator;

	public QueryBuilderControl() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 100, 0, 125, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblCriteriaJoinOperator = new JLabel("Criteria Join Operator");
		GridBagConstraints gbc_lblCriteriaJoinOperator = new GridBagConstraints();
		gbc_lblCriteriaJoinOperator.anchor = GridBagConstraints.EAST;
		gbc_lblCriteriaJoinOperator.insets = new Insets(0, 0, 5, 5);
		gbc_lblCriteriaJoinOperator.gridx = 0;
		gbc_lblCriteriaJoinOperator.gridy = 0;
		add(lblCriteriaJoinOperator, gbc_lblCriteriaJoinOperator);
		
		comboCriteriaJoinOperator = new JComboBox<String>();
		comboCriteriaJoinOperator.setModel(new DefaultComboBoxModel<String>(new String[] {"AND", "OR"}));
		GridBagConstraints gbc_comboCriteriaJoinOperator = new GridBagConstraints();
		gbc_comboCriteriaJoinOperator.insets = new Insets(0, 0, 5, 5);
		gbc_comboCriteriaJoinOperator.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboCriteriaJoinOperator.gridx = 1;
		gbc_comboCriteriaJoinOperator.gridy = 0;
		add(comboCriteriaJoinOperator, gbc_comboCriteriaJoinOperator);
		comboCriteriaJoinOperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedJoinOperator = comboCriteriaJoinOperator.getModel().getElementAt(comboCriteriaJoinOperator.getSelectedIndex());
				if(selectedJoinOperator.equals("AND")){
					_generator.setJoinWithAnd();
				}else{
					_generator.setJoinWithOr();
				}
				updatePreview();
			}
		});
		
		panelLogical = new JPanel();
		panelLogical.setBorder(new TitledBorder(null, "Logical Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelLogical = new GridBagConstraints();
		gbc_panelLogical.gridwidth = 3;
		gbc_panelLogical.insets = new Insets(0, 0, 5, 0);
		gbc_panelLogical.fill = GridBagConstraints.BOTH;
		gbc_panelLogical.gridx = 0;
		gbc_panelLogical.gridy = 1;
		add(panelLogical, gbc_panelLogical);
		panelLogical.setLayout(new BorderLayout(0, 0));
		
		scrollLogical = new JScrollPane();
		scrollLogical.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLogical.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelLogical.add(scrollLogical);
		
		panelLogicalCriteria = new JPanel();
		panelLogicalCriteria.setBackground(Color.WHITE);
		scrollLogical.setViewportView(panelLogicalCriteria);
		WrapLayout wl_panelLogicalCriteria = new WrapLayout();
		wl_panelLogicalCriteria.setAlignment(FlowLayout.LEFT);
		panelLogicalCriteria.setLayout(wl_panelLogicalCriteria);
		
		JPanel panelCommon = new JPanel();
		panelCommon.setBorder(new TitledBorder(null, "Common Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelCommon = new GridBagConstraints();
		gbc_panelCommon.gridwidth = 3;
		gbc_panelCommon.insets = new Insets(0, 0, 5, 0);
		gbc_panelCommon.fill = GridBagConstraints.BOTH;
		gbc_panelCommon.gridx = 0;
		gbc_panelCommon.gridy = 2;
		add(panelCommon, gbc_panelCommon);
		panelCommon.setLayout(new BorderLayout(0, 0));
		
		scrollCommon = new JScrollPane();
		scrollCommon.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollCommon.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelCommon.add(scrollCommon);
		
		panelCommonCriteria = new JPanel();
		panelCommonCriteria.setBackground(Color.WHITE);
		WrapLayout wl_panelCommonCriteria = new WrapLayout();
		wl_panelCommonCriteria.setAlignment(FlowLayout.LEFT);
		panelCommonCriteria.setLayout(wl_panelCommonCriteria);
		scrollCommon.setViewportView(panelCommonCriteria);
		
		panelQueryPreview = new JPanel();
		panelQueryPreview.setBorder(new TitledBorder(null, "Query Preview", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelQueryPreview = new GridBagConstraints();
		gbc_panelQueryPreview.gridwidth = 3;
		gbc_panelQueryPreview.fill = GridBagConstraints.BOTH;
		gbc_panelQueryPreview.gridx = 0;
		gbc_panelQueryPreview.gridy = 3;
		add(panelQueryPreview, gbc_panelQueryPreview);
		panelQueryPreview.setLayout(new BorderLayout(0, 0));
		
		scrollPreview = new JScrollPane();
		scrollPreview.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPreview.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelQueryPreview.add(scrollPreview);
		
		txtrQueryPreview = new JTextArea();
		txtrQueryPreview.setWrapStyleWord(true);
		txtrQueryPreview.setLineWrap(true);
		txtrQueryPreview.setBackground(Color.WHITE);
		txtrQueryPreview.setEditable(false);
		txtrQueryPreview.setFont(new Font("Courier New", Font.PLAIN, 13));
		scrollPreview.setViewportView(txtrQueryPreview);
		
		setQueryGenerator(_generator);
	}
	
	public QueryGenerator getQueryGenerator(){
		return _generator;
	}
	
	public void setQueryGenerator(QueryGenerator generator){
		try{
			isLoadingGenerator = true;
			_generator = generator;
			panelLogicalCriteria.removeAll();
			panelCommonCriteria.removeAll();
			for(QueryCriteria q : _generator.getCriteria()){
				if(q instanceof NegatableQueryCriteria){
					addCriteria(((NegatableQueryCriteria)q));
				}
				else if(q instanceof LogicalQueryCriteria){
					addCriteria((LogicalQueryCriteria)q);
				}
				else if(q instanceof CommonQueryCriteria){
					addCriteria((CommonQueryCriteria)q);
				}
				else if(q instanceof DateRangeCriteria){
					addCriteria((DateRangeCriteria)q);
				}
				else if(q instanceof RangeCriteria){
					addCriteria((RangeCriteria)q);
				}
				else if(q instanceof PhraseCriteria){
					addCriteria((PhraseCriteria)q);
				}
				else if(q instanceof LiteralCriteria){
					addCriteria((LiteralCriteria)q);
				}
				else if(q instanceof ItemSetBatchCriteria){
					ItemSetBatchCriteria criteria = (ItemSetBatchCriteria)q;
					addCriteria(criteria);
				}
				else if(q instanceof QueryGenerator){
					addCriteria((QueryGenerator)q);
				}
				else{
					System.out.println("WARNING: Unknown criteria type: " + q.getClass().getCanonicalName());
					System.out.println("Please make sure you are using a version of Nx compatible with the query generator you are loading.");
				}
			}
			
			if(_generator.isJoinedWithAnd()){
				comboCriteriaJoinOperator.setSelectedIndex(0);
			}
			else{
				comboCriteriaJoinOperator.setSelectedIndex(1);
			}
			
			isLoadingGenerator = false;
			panelLogicalCriteria.revalidate();
			panelLogicalCriteria.repaint();
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
		catch(Exception e){
			e.printStackTrace();
			//System.out.println(e.toString());
		}
	}
	
	public String getQuery(){
		return _generator.toQuery();
	}
	
	public List<QueryCriteria> getCriteria(){
		return _generator.getCriteria();
	}
	
	private void updatePreview(){
		txtrQueryPreview.setText(_generator.toQuery());
	}

	public void addCriteria(final LogicalQueryCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		LogicalCriteriaControl control = new LogicalCriteriaControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelLogicalCriteria.add(control);
		if(!isLoadingGenerator){
			panelLogicalCriteria.revalidate();
			panelLogicalCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(LogicalQueryCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelLogicalCriteria.getComponents()) {
	        if (c instanceof LogicalCriteriaControl) {
	           if(((LogicalCriteriaControl)c).getCriteria() == criteria){
					panelLogicalCriteria.remove(c);
					panelLogicalCriteria.revalidate();
					panelLogicalCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final CommonQueryCriteria criteria){
		try{
			if(!isLoadingGenerator){
				_generator.addCriteria(criteria);
			}
			CommonCriteriaControl control = new CommonCriteriaControl(criteria);
			control.addChangeListener(this);
			control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					removeCriteria(criteria);
				}
			});
			panelCommonCriteria.add(control);
			if(!isLoadingGenerator){
				panelCommonCriteria.revalidate();
				panelCommonCriteria.repaint();
				updatePreview();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void removeCriteria(CommonQueryCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof CommonCriteriaControl) {
	           if(((CommonCriteriaControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final DateRangeCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		DateRangeCriteriaControl control = new DateRangeCriteriaControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(DateRangeCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof DateRangeCriteriaControl) {
	           if(((DateRangeCriteriaControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final RangeCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		RangeCriteriaControl control = new RangeCriteriaControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(RangeCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof RangeCriteriaControl) {
	           if(((RangeCriteriaControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final PhraseCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		PhraseCriteriaControl control = new PhraseCriteriaControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(PhraseCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof PhraseCriteriaControl) {
	           if(((PhraseCriteriaControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final LiteralCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		LiteralCriteriaControl control = new LiteralCriteriaControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(LiteralCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof LiteralCriteriaControl) {
	           if(((LiteralCriteriaControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final ItemSetBatchCriteria criteria){
		if(!isLoadingGenerator){
			_generator.addCriteria(criteria);
		}
		ItemSetBatchControl control = new ItemSetBatchControl(criteria);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(criteria);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(ItemSetBatchCriteria criteria){
		_generator.removeCriteria(criteria);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof ItemSetBatchControl) {
	           if(((ItemSetBatchControl)c).getCriteria() == criteria){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void addCriteria(final QueryGenerator generator){
		if(!isLoadingGenerator){
			_generator.addCriteria(generator);
		}
		
		SubQueryCriteriaControl control = new SubQueryCriteriaControl(generator);
		control.addChangeListener(this);
		control.getBtnRemoveCriteria().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCriteria(generator);
			}
		});
		panelCommonCriteria.add(control);
		if(!isLoadingGenerator){
			panelCommonCriteria.revalidate();
			panelCommonCriteria.repaint();
			updatePreview();
		}
	}
	
	public void removeCriteria(QueryGenerator generator){
		_generator.removeCriteria(generator);
		for (Component c : panelCommonCriteria.getComponents()) {
	        if (c instanceof SubQueryCriteriaControl) {
	           if(((SubQueryCriteriaControl)c).getGenerator() == generator){
	        	   panelCommonCriteria.remove(c);
	        	   panelCommonCriteria.revalidate();
	        	   panelCommonCriteria.repaint();
					updatePreview();
					break;
	           }
	        }
	    }
	}
	
	public void populateMenuBar(JMenuBar menuBar){
		JMenu addCriteriaMenu = new JMenu("Add Criteria");
		
		//======================
		//Logical Criteria
		//======================
		JMenu addLogicalMenu = new JMenu("Add Logical");
		JMenuItem logicalMenuItem;
		
		final HashMap<String,Class<? extends LogicalQueryCriteria>> logicalEntries =
				new HashMap<String,Class<? extends LogicalQueryCriteria>>();
		
		logicalEntries.put("Can Contain Text", CanContainTextCriteria.class);
		logicalEntries.put("Contains Text", ContainsTextCriteria.class);
		logicalEntries.put("Has Binary", HasBinaryCriteria.class);
		logicalEntries.put("Has Comment", HasCommentCriteria.class);
		logicalEntries.put("Has Communication", HasCommunicationCriteria.class);
		logicalEntries.put("Has Custodian", HasCustodianCriteria.class);
		logicalEntries.put("Has Embedded Data", HasEmbeddedDataCriteria.class);
		logicalEntries.put("Has Exclusion", HasExclusionCriteria.class);
		logicalEntries.put("Has Image", HasImageCriteria.class);
		logicalEntries.put("Has Item Set", HasItemSetCriteria.class);
		logicalEntries.put("Has Production Set", HasProductionSetCriteria.class);
		logicalEntries.put("Has Tag", HasTagCriteria.class);
		logicalEntries.put("Is Encrypted", IsEncryptedCriteria.class);
		logicalEntries.put("Is Deleted", IsDeletedCriteria.class);
		logicalEntries.put("Is Item Set Original", IsItemSetOriginalCriteria.class);
		
		SortedSet<String> logicalKeys = new TreeSet<String>(logicalEntries.keySet());
		for(final String key : logicalKeys){
			logicalMenuItem = new JMenuItem(key);
			Class<? extends LogicalQueryCriteria> classToAdd = logicalEntries.get(key);
			logicalMenuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						addCriteria(classToAdd.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			addLogicalMenu.add(logicalMenuItem);
		}
		
		//======================
		//Negatable Criteria
		//======================
		
		final HashMap<String,Class<? extends NegatableQueryCriteria>> negatableCriteria =
				new HashMap<String,Class<? extends NegatableQueryCriteria>>();
		
		negatableCriteria.put("Is Corrupted Container", CorruptedContainerCriteria.class);
		negatableCriteria.put("Is Corrupted", CorruptedCriteria.class);
		negatableCriteria.put("Is Non-Searchable PDF", NonSearchablePDFCriteria.class);
		negatableCriteria.put("Has Updated Text", TextUpdatedCriteria.class);
		negatableCriteria.put("Is Unrecognised", UnrecognisedCriteria.class);
		negatableCriteria.put("Is Unsupported Container", UnsupportedContainerCriteria.class);
		negatableCriteria.put("Is Unsupported", UnsupportedCriteria.class);
		
		SortedSet<String> negatableKeys = new TreeSet<String>(negatableCriteria.keySet());
		for(final String key : negatableKeys){
			logicalMenuItem = new JMenuItem(key);
			Class<? extends NegatableQueryCriteria> classToAdd = negatableCriteria.get(key);
			logicalMenuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						addCriteria(classToAdd.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			addLogicalMenu.add(logicalMenuItem);
		}
	
		addCriteriaMenu.add(addLogicalMenu);
		
		//======================
		//Common Criteria
		//======================
		JMenu addCommonMenu = new JMenu("Add Common");
		JMenuItem commonMenuItem;
		
		final HashMap<String,Class<? extends CommonQueryCriteria>> commonEntries = new HashMap<String,Class<? extends CommonQueryCriteria>>();
		
		commonEntries.put("Batch Load", BatchLoadCriteria.class);
		commonEntries.put("Custodian", CustodianCriteria.class);
		commonEntries.put("Digest List", DigestListCriteria.class);
		commonEntries.put("Evidence", EvidenceCriteria.class);
		commonEntries.put("Exclusion", ExclusionCriteria.class);
		commonEntries.put("Flags", FlagCriteria.class);
		
		SortedSet<String> commonKeys = new TreeSet<String>(commonEntries.keySet());
		for(String key : commonKeys){
			commonMenuItem = new JMenuItem(key);
			Class<? extends CommonQueryCriteria> classToAdd = commonEntries.get(key);
			commonMenuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						addCriteria(classToAdd.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			addCommonMenu.add(commonMenuItem);
		}
		
		//Add item sets sub menu
		commonEntries.clear();
		JMenu itemSetsSubMenu = new JMenu("Item Set");
		addCommonMenu.add(itemSetsSubMenu);
		
		// Add item set
		commonMenuItem = new JMenuItem("Originals and Duplicates");
		commonMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new ItemSetCriteria());
			}
		});
		itemSetsSubMenu.add(commonMenuItem);
		
		// Add item set originals
		commonMenuItem = new JMenuItem("Originals");
		commonMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new ItemSetOriginalsCriteria());
			}
		});
		itemSetsSubMenu.add(commonMenuItem);
		
		// Add item set originals
		commonMenuItem = new JMenuItem("Duplicates");
		commonMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new ItemSetDuplicatesCriteria());
			}
		});
		itemSetsSubMenu.add(commonMenuItem);
		
		// Add item set batch
		commonMenuItem = new JMenuItem("Batch");
		commonMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new ItemSetBatchCriteria());
			}
		});
		itemSetsSubMenu.add(commonMenuItem);
		
		// Add the rest
		commonEntries.clear();
		commonEntries.put("Kind", KindCriteria.class);
		commonEntries.put("Path Kind", PathKindCriteria.class);
		commonEntries.put("Language", LanguageCriteria.class);
		commonEntries.put("Mime Type", MimeTypeCriteria.class);
		commonEntries.put("Orig Extension", OriginalExtensionCriteria.class);
		commonEntries.put("Production Sets", ProductionSetsCriteria.class);
		commonEntries.put("Tags", TagCriteria.class);
		
		commonKeys = new TreeSet<String>(commonEntries.keySet());
		for(final String key : commonKeys){
			commonMenuItem = new JMenuItem(key);
			commonMenuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						addCriteria(commonEntries.get(key).newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			addCommonMenu.add(commonMenuItem);
		}
		
		
		
		addCriteriaMenu.add(addCommonMenu);
		
		//======================
		//Other Criteria
		//======================
		JMenu addOtherMenu = new JMenu("Add Other");
		
		JMenuItem otherMenuItem = new JMenuItem("Range");
		otherMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new RangeCriteria());
			}
		});
		addOtherMenu.add(otherMenuItem);
		
		otherMenuItem = new JMenuItem("Date Range");
		otherMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new DateRangeCriteria());
			}
		});
		addOtherMenu.add(otherMenuItem);
		
		otherMenuItem = new JMenuItem("Phrase");
		otherMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new PhraseCriteria());
			}
		});
		addOtherMenu.add(otherMenuItem);
		
		otherMenuItem = new JMenuItem("Sub Query");
		otherMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new QueryGenerator());
			}
		});
		addOtherMenu.add(otherMenuItem);
		
		otherMenuItem = new JMenuItem("Literal");
		otherMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCriteria(new LiteralCriteria());
			}
		});
		addOtherMenu.add(otherMenuItem);
		
		addCriteriaMenu.add(addOtherMenu);
		
		//======================
		// File Menu
		//======================
		JMenu fileMenu = MenuHelper.findOrCreateTopLevelMenu(menuBar, "File");
		JMenu querySubMenu = new JMenu("Query");
		
		JMenuItem newQueryMenuItem = new JMenuItem("New Query");
		newQueryMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
		newQueryMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new Query?  Any unsaved changes will be lost.","Are you sure?"
						,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					setQueryGenerator(new QueryGenerator());					
				}
			}
		});
		querySubMenu.add(newQueryMenuItem);
		
		JMenuItem openQueryMenuItem = new JMenuItem("Open Query");
		openQueryMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		openQueryMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File selectedFile = Prompt.openFile("Saved Query Generator", "qgen",QueryBuilderControl.this);
				if(selectedFile != null){
					try {
						setQueryGenerator(QueryGenerator.fromFile(selectedFile.getAbsolutePath()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		querySubMenu.add(openQueryMenuItem);
		
		JMenuItem saveQueryMenuItem = new JMenuItem("Save Query");
		saveQueryMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
		saveQueryMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File selectedFile = Prompt.saveFile("Saved Query Generator", "qgen",QueryBuilderControl.this);
				if(selectedFile != null){
					try {
						_generator.saveToFile(selectedFile.getAbsolutePath());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		querySubMenu.add(saveQueryMenuItem);
		
		JMenuItem copyQueryString = new JMenuItem("Copy Query String");
		copyQueryString.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StringSelection stringSelection = new StringSelection(getQuery());
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard ();
				clpbrd.setContents (stringSelection, null);
			}
		});
		querySubMenu.add(copyQueryString);
		
		JMenuItem testCurrentQuery = new JMenuItem("Test Query");
		testCurrentQuery.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		final Component owner = this;
		testCurrentQuery.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String currentQuery = getQuery();
				Case nuixCase = NuixDataBridge.getCurrentCase();
				if(nuixCase == null){
					JOptionPane.showMessageDialog(owner, "It appears there is currently no case open.", "No Case Open", JOptionPane.ERROR_MESSAGE);
				}else{
					try {
						long hits = nuixCase.count(currentQuery);
						JOptionPane.showMessageDialog(owner, "Hits: "+Long.toString(hits), "Hits", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(owner, "Your query had an error:\n"+e.getMessage(), "Query Error", JOptionPane.ERROR_MESSAGE);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		querySubMenu.add(testCurrentQuery);
		
		fileMenu.add(querySubMenu);
		
		menuBar.add(addCriteriaMenu);
	}

	@Override
	public void changed(Component comp) {
		if(!isLoadingGenerator)
			updatePreview();
	}
}
