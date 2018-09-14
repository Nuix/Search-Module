package com.nuix.searchmodule;

import com.google.common.base.Joiner;

public class CategorySettings {
	private String categoryKey;
	private String categoryTitle;
	private boolean reportCount;
	private boolean reportAuditedSize;
	private boolean reportFileSize;
	
	private boolean applyCategoryTags;
	private String categoryTagTemplate;
	
	private boolean applyTermTags;
	private String termTagTemplate;
	
	private boolean applyCustomMetadata;
	private boolean appendCustomMetadata;
	private String customMetadataTemplate;
	private String customMetadataField;
	
	public CategorySettings(){
		this("category placeholder","Placeholder Category");
	}
	public CategorySettings(String key, String title){
		this(key,title,true);
	}
	
	public CategorySettings(String key, String title, boolean report){
		categoryKey = key;
		categoryTitle = title;
		reportCount = report;
		reportAuditedSize = false;
		reportFileSize = false;
		
		applyCategoryTags = false;
		categoryTagTemplate = "{datetime}|"+categoryTitle;
		applyTermTags = false;
		termTagTemplate = "{datetime}|"+categoryTitle+"|Term{sequence}";
		
		applyCustomMetadata = false;
		appendCustomMetadata = true;
		customMetadataTemplate = "{term}";
		customMetadataField = "SearchModule";
	}
	
	protected String fixTagWhitespace(String tag){
		String[] segments = tag.split("\\|");
		for(int i=0;i<segments.length;i++){
			segments[i] = segments[i].trim();
		}
		return Joiner.on("|").join(segments);
	}
	
	public String getCategoryKey() {
		return categoryKey;
	}
	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	public boolean getReportCount() {
		return reportCount;
	}
	public void setReportCount(boolean countReported) {
		this.reportCount = countReported;
	}
	public boolean getReportAudited() {
		return reportAuditedSize;
	}
	public void setReportAudited(boolean auditedSizeReported) {
		this.reportAuditedSize = auditedSizeReported;
	}
	public boolean getReportFileSize() {
		return reportFileSize;
	}
	public void setReportFileSize(boolean reportFileSize) {
		this.reportFileSize = reportFileSize;
	}
	public boolean getApplyCategoryTags() {
		return applyCategoryTags;
	}
	public void setApplyCategoryTags(boolean tagsApplied) {
		this.applyCategoryTags = tagsApplied;
	}
	
	public String getCategoryTagTemplate() {
		return fixTagWhitespace(categoryTagTemplate);
	}
	public void setCategoryTagTemplate(String tagTemplate) {
		this.categoryTagTemplate = fixTagWhitespace(tagTemplate);
	}
	
	public boolean getApplyTermTags() {
		return applyTermTags;
	}
	public void setApplyTermTags(boolean applyTermTags) {
		this.applyTermTags = applyTermTags;
	}
	
	public String getTermTagTemplate() {
		return fixTagWhitespace(termTagTemplate);
	}
	public void setTermTagTemplate(String termTagTemplate) {
		this.termTagTemplate = fixTagWhitespace(termTagTemplate);
	}
	
	public boolean getApplyCustomMetadata() {
		return applyCustomMetadata;
	}
	public void setApplyCustomMetadata(boolean applyCustomMetadata) {
		this.applyCustomMetadata = applyCustomMetadata;
	}
	
	public boolean getAppendCustomMetadata() {
		return appendCustomMetadata;
	}
	public void setAppendCustomMetadata(boolean appendCustomMetadata) {
		this.appendCustomMetadata = appendCustomMetadata;
	}
	
	public String getCustomMetadataTemplate() {
		return customMetadataTemplate;
	}
	public void setCustomMetadataTemplate(String customMetadataTemplate) {
		this.customMetadataTemplate = customMetadataTemplate;
	}
	public String getCustomMetadataField() {
		return customMetadataField;
	}
	public void setCustomMetadataField(String customMetadataField) {
		this.customMetadataField = customMetadataField;
	}
	
}
