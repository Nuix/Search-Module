package com.nuix.nx.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.NameValuePair;
import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.controls.FilterableListControl;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Provides a modal dialog which allows the user to select one or more
 * provided values.
 * @author JasonWells
 *
 */
@SuppressWarnings("serial")
public class MultipleChoiceDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private FilterableListControl filterableListControl;

	public MultipleChoiceDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(350,500));
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			filterableListControl = new FilterableListControl();
			contentPanel.add(filterableListControl);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
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
						filterableListControl.getModel().clear();
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Gets the underlying {@link FilterableListControl}.
	 * @return The underlying {@link FilterableListControl}
	 */
	public FilterableListControl getFilterableListControl() {
		return filterableListControl;
	}
	
	/**
	 * Static method to present a dialog already populated with the tags
	 * in the current case. Tags choices obtained from {@link NuixDataBridge}.
	 * @return All the choices the user has checked when the dialog closes.
	 */
	public static List<NameValuePair> chooseTags(){
		return showChooserForData(NuixDataBridge.getTags());
	}
	
	/**
	 * Shows a dialog allowing a user to select from the supplied data.
	 * @param data The data for the user to choose from.  Name is the displayed value.
	 * @return All the choices the user has checked when the dialog closes.
	 */
	public static List<NameValuePair> showChooserForData(List<NameValuePair> data){
		MultipleChoiceDialog dialog = new MultipleChoiceDialog();
		dialog.filterableListControl.getModel().add(data);
		dialog.setVisible(true);
		List<NameValuePair> result = dialog.filterableListControl.getModel().getCheckedValues();
		dialog.dispose();
		return result;
	}
	
	/**
	 * Shows a dialog allowing a user to select from the supplied data.
	 * @param data The data for the user to choose from.  Name is the displayed value.
	 * @return All the choices the user has checked when the dialog closes.
	 */
	public static List<NameValuePair> showChooserForData(List<NameValuePair> data, String title, boolean showFilter){
		MultipleChoiceDialog dialog = new MultipleChoiceDialog();
		dialog.filterableListControl.getModel().add(data);
		dialog.filterableListControl.setFilteringControlsVisible(showFilter);
		dialog.setVisible(true);
		dialog.setTitle(title);
		List<NameValuePair> result = dialog.filterableListControl.getModel().getCheckedValues();
		dialog.dispose();
		return result;
	}
}
