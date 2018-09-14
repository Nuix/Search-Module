package com.nuix.searchmodule.query;

import java.util.List;

public class QueryValidationInfo {
	public enum ValidationType {
		WARNING,
		ERROR,
	}
	private ValidationType validationType = ValidationType.WARNING;
	private String message = "";

	/**
	 * Creates a new instance
	 * @param type The type of validation issue this represents.
	 * @param message A message with details about the issue.
	 */
	public QueryValidationInfo(ValidationType type, String message){
		this.validationType = type;
		this.message = message;
	}
	
	/**
	 * Converts a list of validation info objects into a HTML message for display to user.
	 * @param validations Validations to generate a message for
	 * @return A string with a very basic HTML message containing the validation messages.
	 */
	public static String toMessage(List<QueryValidationInfo> validations){
		StringBuilder message = new StringBuilder();
		message.append("<html>");
		for(QueryValidationInfo info : validations){
			if (info.isError()) {
				message.append("<b>Error:</b> "+info.getMessage()+"<br/>");
			}else if(info.isWarning()){
				message.append("<b>Warning:</b> "+info.getMessage()+"<br/>");
			}
		}
		message.append("</html>");
		return message.toString();
	}
	
	/**
	 * Checks whether the provided list of validation info objects has any errors.
	 * @param validations The validations to check
	 * @return true if any validations are located that are errors.
	 */
	public static boolean hasAnyErrors(List<QueryValidationInfo> validations){
		return validations.stream().anyMatch(i -> i.isError());
	}
	
	/**
	 * Checks whether the provided list of validation info objects only contains warnings
	 * @param validations The validations to check
	 * @return true if the list contains 1 or more validations and they are all not errors
	 */
	public static boolean hasOnlyWarnings(List<QueryValidationInfo> validations){
		return validations.size() > 0 && !validations.stream().anyMatch(i -> i.isError());
	}
	
	/**
	 * @return The message associated with this issue 
	 */
	public String getMessage() { return message; }
	
	/**
	 * @param message The message to associate with this issue.
	 */
	public void setMessage(String message) { this.message = message; }
	
	/**
	 * @return The type of this issue
	 */
	public ValidationType getValidationType() { return validationType; }
	
	/**
	 * @param validationType The type of issue this is.
	 */
	public void setValidationType(ValidationType validationType) { this.validationType = validationType; }
	
	/**
	 * @return True if this is a Validation.ERROR
	 */
	public boolean isError(){ return validationType == ValidationType.ERROR; }
	
	/**
	 * @return True if this is a Validation.WARNING
	 */
	public boolean isWarning(){ return validationType == ValidationType.WARNING; }
}
