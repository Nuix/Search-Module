package com.nuix.nx.dialogs;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class provides convenience methods for prompting the user to select
 * the saving or opening of a file.
 * @author JasonWells
 *
 */
public class Prompt {
	/**
	 * Prompt the user to save a file. 
	 * @param typeDescription The description of the file type, ex: "Comma Separated Values"
	 * @param extension The extension filter used (no period), ex: "csv"
	 * @param initialDirectory The initial directory to start in, can be null
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} or null if nothing was selected
	 */
	public static File saveFile(String typeDescription, String extension, File initialDirectory, Component owner){
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(typeDescription + " (*."+extension+")",new String[]{extension}); 
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);
		if(initialDirectory != null){
			chooser.setCurrentDirectory(initialDirectory);
		}
		if(chooser.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION){
			File selectedFile = chooser.getSelectedFile();
			if(!selectedFile.getAbsolutePath().endsWith("."+extension)){
				selectedFile = new File(selectedFile + "."+extension);
			}
			return selectedFile;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Prompt the user to save a file. 
	 * @param typeDescription The description of the file type, ex: "Comma Separated Values"
	 * @param extension The extension filter used (no period), ex: "csv"
	 * @return The selected {@link File} or null if nothing was selected
	 */
	public static File saveFile(String typeDescription, String extension, Component owner){
		return saveFile(typeDescription,extension,null,owner);
	}
	
	/**
	 * Prompt the user to open a file. 
	 * @param typeDescription The description of the file type, ex: "Comma Separated Values"
	 * @param extension The extension filter used (no period), ex: "csv"
	 * @param initialDirectory The initial directory to start in, can be null
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} or null if nothing was selected
	 */
	public static File openFile(String typeDescription, String extension, File initialDirectory, Component owner){
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(typeDescription + " (*."+extension+")",new String[]{extension}); 
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);
		if(initialDirectory != null){
			chooser.setCurrentDirectory(initialDirectory);
		}
		if(chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION){
			File selectedFile = chooser.getSelectedFile();
			if(!selectedFile.getAbsolutePath().endsWith("."+extension)){
				selectedFile = new File(selectedFile + "."+extension);
			}
			return selectedFile;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Prompt the user to open a file. 
	 * @param typeDescription The description of the file type, ex: "Comma Separated Values"
	 * @param extension The extension filter used (no period), ex: "csv"
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} or null if nothing was selected
	 */
	public static File openFile(String typeDescription, String extension, Component owner){
		return openFile(typeDescription,extension,null,owner);
	}
	
	/**
	 * Shortcut method to prompt the user to save a CSV file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File saveCsv(Component owner){ return saveFile("Comma Separated Values","csv",owner); }
	/**
	 * Shortcut method to prompt the user to open a CSV file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File openCsv(Component owner){ return openFile("Comma Separated Values","csv",owner); }
	/**
	 * Shortcut method to prompt the user to save a TXT file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File saveTxt(Component owner){ return saveFile("Text File","txt",owner); }
	/**
	 * Shortcut method to prompt the user to open a TXT file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File openTxt(Component owner){ return openFile("Text File","txt",owner); }
	
	/**
	 * Shortcut method to prompt the user to save a XLSX file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File saveXlsx(Component owner){ return saveFile("Excel XLSX","xlsx",owner); }
	
	/**
	 * Shortcut method to prompt the user to save a XLS file
	 * @param owner Owning component, can be null
	 * @return The selected {@link File} instance or null
	 */
	public static File saveXls(Component owner){ return saveFile("Excel XLS","xls",owner); }
	
	public static void showError(String message){
		JOptionPane.showMessageDialog(null, message);
	}
}
