package com.nuix.searchmodule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SearchModulePersistedSettings {
	private static File settingsFile;
	private static Properties properties = new Properties();
	
	private static final String PROJECT_LAST_DIRECTORY_KEY = "project.lastDirectory";
	private static final String REPORT_OPEN_ON_COMPLETION = "report.openOnCompletion";
	
	public static void setSettingsFile(File file){
		settingsFile = file;
		loadSettings();
	}
	
	public static void setSettingsFile(String file){
		setSettingsFile(new File(file));
		loadSettings();
	}
	
	protected static void loadSettings(){
		if(settingsFile.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(settingsFile));
				try {
					properties.load(reader);
				} catch (IOException e) {
					System.err.println("Error while loading settings file: "+settingsFile.getAbsolutePath());
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				System.err.println("Error while loading settings file: "+settingsFile.getAbsolutePath());
				e1.printStackTrace();
			}
		}else{
			initDefaults();
		}
	}
	
	protected static void initDefaults(){
		set(PROJECT_LAST_DIRECTORY_KEY,"C:\\");
		set(REPORT_OPEN_ON_COMPLETION,"true");
	}
	
	protected static void saveSettings(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
			properties.store(writer, "Persisted Settings");
		} catch (IOException e) {
			System.err.println("Error while saving to settings file: "+settingsFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	public static void set(String key, String value){
		properties.setProperty(key, value);
		saveSettings();
	}
	
	public static String get(String key,String defaultValue){
		String value = properties.getProperty(key);
		if(value == null || value.isEmpty()){
			value = defaultValue;
		}
		return value;
	}
	
	public static File getProjectLastDirectory(){
		return new File(get(PROJECT_LAST_DIRECTORY_KEY,"C:\\"));
	}
	
	public static void setProjectLastDirectory(File directory){
		setProjectLastDirectory(directory.getAbsolutePath());
	}
	
	public static void setProjectLastDirectory(String directory){
		set(PROJECT_LAST_DIRECTORY_KEY,directory);
	}
	
	public static boolean getOpenReportOnCompletion(){
		return get(REPORT_OPEN_ON_COMPLETION,"true").toLowerCase().trim().equals("true");
	}
	
	public static void setOpenReportOnCompletion(boolean value){
		set(REPORT_OPEN_ON_COMPLETION,Boolean.toString(value));
	}
}
