package com.nuix.nx.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;

import com.nuix.nx.controls.QueryBuilderControl;
import com.nuix.nx.query.QueryGenerator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A {@link QueryBuilderControl} wrapped in a modal dialog.
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class QueryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JMenuBar menuBar;
	private QueryBuilderControl queryBuilderControl;
	private boolean dialogResult = false;

	public QueryDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(1400,900));
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			queryBuilderControl = new QueryBuilderControl();
			contentPanel.add(queryBuilderControl);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialogResult = true;
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
			
			queryBuilderControl.populateMenuBar(menuBar);
		}
	}
	
	/**
	 * The dialog result
	 * @return True if "Ok" was clicked, False if "Cancel" was clicked or the window was closed.
	 */
	public boolean getDialogResult(){ return dialogResult; }
	/**
	 * Get the string query of the edited {@link QueryGenerator}.  Essentially the same as calling:
	 * <code>getQueryGenerator().toQuery()</code>
	 * @return A query string based on the {@link QueryGenerator} edited by this control.
	 */
	public String getQuery(){ return queryBuilderControl.getQuery(); }
	/**
	 * Get the {@link QueryGenerator} instance edited by the underlying control.
	 * @return The edited QueryGenerator
	 */
	public QueryGenerator getQueryGenerator(){ return queryBuilderControl.getQueryGenerator(); }
	/**
	 * Set the {@link QueryGenerator} instance edited by the underlying control.
	 * @param generator
	 */
	public void setQueryGenerator(QueryGenerator generator){ queryBuilderControl.setQueryGenerator(generator); }
}
