package com.nuix.nx.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;

import com.nuix.nx.controls.ScopedTermsControl;
import com.nuix.nx.query.QueryGenerator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A modal dialog containing a {@link ScopedTermsControl}. 
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class ScopedTermsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ScopedTermsControl scopedTermsControl;
	private JMenuBar menuBar;
	private boolean _dialogResult = false;

	public ScopedTermsDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(1400,900));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scopedTermsControl = new ScopedTermsControl();
			contentPanel.add(scopedTermsControl);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						_dialogResult = true;
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_dialogResult = false;
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
		
		
		scopedTermsControl.populateMenuBar(menuBar);
	}
	
	/**
	 * Get the underlying {@link QueryGenerator}
	 * @return The underlying {@link QueryGenerator}
	 */
	public QueryGenerator getQueryGenerator(){
		return scopedTermsControl.getQueryGenerator();
	}
	
	/**
	 * Set the underlying {@link QueryGenerator}
	 * @param generator The value to set
	 */
	public void setQueryGenerator(QueryGenerator generator){
		scopedTermsControl.setQueryGenerator(generator);
	}
	
	/**
	 * Get the list of term fields
	 * @return A list of the selected term fields.
	 */
	public List<String> getTermFields(){
		return scopedTermsControl.getTermFields();
	}
	/**
	 * Set the checked term fields
	 * @param fields Fields to be checked.  A field is ignored if not present as a checkable field.
	 */
	public void setTermFields(List<String> fields){
		scopedTermsControl.setTermFields(fields);
	}
	/**
	 * Get the list of terms
	 * @return The list of terms without: blanks,duplicates,"or","and","not"
	 */
	public List<String> getTerms(){
	    return scopedTermsControl.getTerms();
	}
	/**
	 * Set the list of terms
	 * @param terms The terms to set
	 */
	public void setTerms(List<String> terms){
		scopedTermsControl.setTerms(terms);
	}

	/**
	 * Get the result of this dialog.
	 * @return True if "Ok" was clicked, False if "Cancel" was clicked or the window was closed.
	 */
	public boolean getDialogResult(){
		return _dialogResult;
	}
}
