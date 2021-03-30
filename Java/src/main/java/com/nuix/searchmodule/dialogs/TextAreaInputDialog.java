package com.nuix.searchmodule.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXTextArea;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class TextAreaInputDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private boolean dialogResult = false;
	private JXTextArea txtrInputText;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnTab;

	public TextAreaInputDialog(Dialog owner) {
		super(owner);
		setIconImage(Toolkit.getDefaultToolkit().getImage(TextAreaInputDialog.class.getResource("/com/nuix/nx/NuixIcon.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			contentPanel.add(scrollPane);
			{
				txtrInputText = new JXTextArea();
				scrollPane.setViewportView(txtrInputText);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dialogResult = true;
						dispose();
					}
				});
				{
					JLabel lblDelimiter = new JLabel("Delimiter");
					buttonPane.add(lblDelimiter);
				}
				{
					rdbtnTab = new JRadioButton("Tab");
					buttonGroup.add(rdbtnTab);
					rdbtnTab.setSelected(true);
					buttonPane.add(rdbtnTab);
				}
				{
					JRadioButton rdbtnComma = new JRadioButton("Comma");
					buttonGroup.add(rdbtnComma);
					buttonPane.add(rdbtnComma);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dialogResult = false;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public String getText(){
		return txtrInputText.getText();
	}
	
	public void setText(String value){
		txtrInputText.setText(value);
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}
	
	public boolean isTabDelimited(){
		return rdbtnTab.isSelected();
	}
}
