package com.nuix.searchmodule.dialogs;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.nuix.nx.SearchModuleVersion;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public AboutDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("About");
		setLocationRelativeTo(null);
		setSize(new Dimension(600,400));
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 16, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSearchModule = new JLabel("Search Module");
			lblSearchModule.setFont(new Font("Tahoma", Font.BOLD, 25));
			GridBagConstraints gbc_lblSearchModule = new GridBagConstraints();
			gbc_lblSearchModule.insets = new Insets(0, 0, 5, 5);
			gbc_lblSearchModule.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblSearchModule.gridx = 0;
			gbc_lblSearchModule.gridy = 0;
			contentPanel.add(lblSearchModule, gbc_lblSearchModule);
		}
		{
			JLabel lblVersion = new JLabel("Version "+SearchModuleVersion.getVersion());
			lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 19));
			GridBagConstraints gbc_lblVersion = new GridBagConstraints();
			gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
			gbc_lblVersion.anchor = GridBagConstraints.WEST;
			gbc_lblVersion.gridx = 0;
			gbc_lblVersion.gridy = 1;
			contentPanel.add(lblVersion, gbc_lblVersion);
		}
		{
			JLabel lblWrittenByJason = new JLabel("Written by Jason Wells");
			lblWrittenByJason.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblWrittenByJason = new GridBagConstraints();
			gbc_lblWrittenByJason.insets = new Insets(0, 0, 5, 5);
			gbc_lblWrittenByJason.anchor = GridBagConstraints.EAST;
			gbc_lblWrittenByJason.gridx = 0;
			gbc_lblWrittenByJason.gridy = 3;
			contentPanel.add(lblWrittenByJason, gbc_lblWrittenByJason);
		}
		{
			JLabel lblJasonwellsnuixcom = new JLabel("jason.wells@nuix.com");
			lblJasonwellsnuixcom.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblJasonwellsnuixcom = new GridBagConstraints();
			gbc_lblJasonwellsnuixcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblJasonwellsnuixcom.gridx = 2;
			gbc_lblJasonwellsnuixcom.gridy = 3;
			contentPanel.add(lblJasonwellsnuixcom, gbc_lblJasonwellsnuixcom);
		}
		{
			JLabel lblRequirementsAndTesting = new JLabel("Requirements and Testing by Andy Ward");
			lblRequirementsAndTesting.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblRequirementsAndTesting = new GridBagConstraints();
			gbc_lblRequirementsAndTesting.anchor = GridBagConstraints.EAST;
			gbc_lblRequirementsAndTesting.insets = new Insets(0, 0, 5, 5);
			gbc_lblRequirementsAndTesting.gridx = 0;
			gbc_lblRequirementsAndTesting.gridy = 4;
			contentPanel.add(lblRequirementsAndTesting, gbc_lblRequirementsAndTesting);
		}
		{
			JLabel lblAndywardnuixcom = new JLabel("andy.ward@nuix.com");
			lblAndywardnuixcom.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblAndywardnuixcom = new GridBagConstraints();
			gbc_lblAndywardnuixcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblAndywardnuixcom.gridx = 2;
			gbc_lblAndywardnuixcom.gridy = 4;
			contentPanel.add(lblAndywardnuixcom, gbc_lblAndywardnuixcom);
		}
		{
			JLabel lblRequirementsAndTesting_1 = new JLabel("Requirements and Testing by Philip Glod");
			lblRequirementsAndTesting_1.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblRequirementsAndTesting_1 = new GridBagConstraints();
			gbc_lblRequirementsAndTesting_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblRequirementsAndTesting_1.gridx = 0;
			gbc_lblRequirementsAndTesting_1.gridy = 5;
			contentPanel.add(lblRequirementsAndTesting_1, gbc_lblRequirementsAndTesting_1);
		}
		{
			JLabel lblPhilipglodnuixcom = new JLabel("philip.glod@nuix.com");
			lblPhilipglodnuixcom.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblPhilipglodnuixcom = new GridBagConstraints();
			gbc_lblPhilipglodnuixcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblPhilipglodnuixcom.gridx = 2;
			gbc_lblPhilipglodnuixcom.gridy = 5;
			contentPanel.add(lblPhilipglodnuixcom, gbc_lblPhilipglodnuixcom);
		}
		{
			JButton btnProjectHomePage = new JButton("Project Home Page");
			btnProjectHomePage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().browse(new URI("http://www.github.com/nuix/Search-Module"));
					} catch (Exception exc) {
						// TODO Auto-generated catch block
						exc.printStackTrace();
					}
				}
			});
			GridBagConstraints gbc_btnProjectHomePage = new GridBagConstraints();
			gbc_btnProjectHomePage.gridwidth = 3;
			gbc_btnProjectHomePage.insets = new Insets(0, 0, 0, 5);
			gbc_btnProjectHomePage.gridx = 0;
			gbc_btnProjectHomePage.gridy = 6;
			contentPanel.add(btnProjectHomePage, gbc_btnProjectHomePage);
		}
		pack();
	}

}
