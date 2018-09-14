package com.nuix.searchmodule.query;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.flexible.standard.parser.FastCharStream;
import org.apache.lucene.queryparser.flexible.standard.parser.StandardSyntaxParser;
import org.apache.lucene.queryparser.flexible.standard.parser.Token;
import org.apache.lucene.queryparser.flexible.standard.parser.TokenMgrError;

import com.nuix.nx.NuixDataBridge;
import com.nuix.nx.NuixVersion;
import com.nuix.searchmodule.query.QueryValidationInfo.ValidationType;

public class QueryValidator {
	private static Logger logger = Logger.getLogger(QueryValidator.class);
	
	private static Pattern stripQuotesPattern = Pattern.compile("(^\")|(\"$)");
	private static Pattern stripRegexSlashesPattern = Pattern.compile("(^/)|(/$)");
	private static Pattern invalidWildcardPattern = Pattern.compile("^[\\*\\?]+$");
	private static Pattern invalidRegexDelimiterPattern = Pattern.compile("(^/[^/]+$)|($[^/]+/$)");
	private static Pattern starPrefixPattern = Pattern.compile("[\\*]+[^\\* ]+");
	private static Pattern proximityPatternW = Pattern.compile("w/([0-9]+)",Pattern.CASE_INSENSITIVE);
	private static Pattern proximityPatternPre = Pattern.compile("pre/([0-9]+)",Pattern.CASE_INSENSITIVE);
	private static Set<String> knownNuixFields = new HashSet<String>();
	
	static {
		knownNuixFields.add("address");
		knownNuixFields.add("audited-size");
		knownNuixFields.add("automatic-classifier");
		knownNuixFields.add("batch-load-guid");
		knownNuixFields.add("bcc");
		knownNuixFields.add("bcc-mail-address");
		knownNuixFields.add("bcc-mail-domain");
		knownNuixFields.add("boolean-custom-metadata");
		knownNuixFields.add("cc");
		knownNuixFields.add("cc-mail-address");
		knownNuixFields.add("cc-mail-domain");
		knownNuixFields.add("characters");
		knownNuixFields.add("cluster");
		knownNuixFields.add("comm-date");
		knownNuixFields.add("comm-guid");
		knownNuixFields.add("comment");
		knownNuixFields.add("contains-text");
		knownNuixFields.add("content");
		knownNuixFields.add("custodian");
		knownNuixFields.add("custom-metadata");
		knownNuixFields.add("date-custom-metadata");
		knownNuixFields.add("date-properties");
		knownNuixFields.add("deleted");
		knownNuixFields.add("digest-input-size");
		knownNuixFields.add("digest-list");
		knownNuixFields.add("document-id");
		knownNuixFields.add("encrypted");
		knownNuixFields.add("evidence-metadata");
		knownNuixFields.add("exclusion");
		knownNuixFields.add("family");
		knownNuixFields.add("family-content");
		knownNuixFields.add("family-names");
		knownNuixFields.add("family-properties");
		knownNuixFields.add("file-extension");
		knownNuixFields.add("flag");
		knownNuixFields.add("float-custom-metadata");
		knownNuixFields.add("float-properties");
		knownNuixFields.add("from");
		knownNuixFields.add("from-mail-address");
		knownNuixFields.add("from-mail-domain");
		knownNuixFields.add("guid");
		knownNuixFields.add("has-binary");
		knownNuixFields.add("has-comment");
		knownNuixFields.add("has-communication");
		knownNuixFields.add("has-custodian");
		knownNuixFields.add("has-embedded-data");
		knownNuixFields.add("has-exclusion");
		knownNuixFields.add("has-image");
		knownNuixFields.add("has-item-set");
		knownNuixFields.add("has-print-preview");
		knownNuixFields.add("has-production-set");
		knownNuixFields.add("has-stored");
		knownNuixFields.add("has-tag");
		knownNuixFields.add("has-text");
		knownNuixFields.add("integer-custom-metadata");
		knownNuixFields.add("integer-properties");
		knownNuixFields.add("is-item-original");
		knownNuixFields.add("item-date");
		knownNuixFields.add("item-id");
		knownNuixFields.add("item-set");
		knownNuixFields.add("item-set-batch");
		knownNuixFields.add("item-set-duplicates");
		knownNuixFields.add("item-set-originals");
		knownNuixFields.add("kind");
		knownNuixFields.add("lang");
		knownNuixFields.add("markup-set");
		knownNuixFields.add("md5");
		knownNuixFields.add("mime-type");
		knownNuixFields.add("modifications");
		knownNuixFields.add("modifications");
		knownNuixFields.add("name");
		knownNuixFields.add("named-entities");
		knownNuixFields.add("parent-guid");
		knownNuixFields.add("path-guid");
		knownNuixFields.add("path-kind");
		knownNuixFields.add("path-mime-type");
		knownNuixFields.add("path-name");
		knownNuixFields.add("previous-version-docid");
		knownNuixFields.add("print-method");
		knownNuixFields.add("print-preview");
		knownNuixFields.add("production-set");
		knownNuixFields.add("production-set-guid");
		knownNuixFields.add("properties");
		knownNuixFields.add("recipient");
		knownNuixFields.add("score-confidence");
		knownNuixFields.add("scored");
		knownNuixFields.add("sha-1");
		knownNuixFields.add("sha-256");
		knownNuixFields.add("shannon-entropy");
		knownNuixFields.add("shingle-list");
		knownNuixFields.add("skintone");
		knownNuixFields.add("tag");
		knownNuixFields.add("text-custom-metadata");
		knownNuixFields.add("to");
		knownNuixFields.add("to-mail-address");
		knownNuixFields.add("to-mail-domain");
		knownNuixFields.add("top-level-item-date");
		knownNuixFields.add("word-list");
	}
	
	/**
	 * Escape syntax that is custom to Nuix and causes issues with stock Lucene.
	 * @param query Query to "sanitize"
	 * @return Sanitized string
	 */
	protected static String escapeCustomSyntax(String query){
		String result = proximityPatternW.matcher(query).replaceAll("w\\\\/$1");
		return proximityPatternPre.matcher(result).replaceAll("pre\\\\/$1");
	}
	
	/**
	 * Tokenizes a query string using Lucene StandardSyntaxParser
	 * @param query Query to get tokens for
	 * @return Tokens as strings
	 */
	protected static List<String> tokenizeQuery(String query){
		Pattern a = Pattern.compile("/");
		Pattern b = Pattern.compile("\\\\/");
		ArrayList<String> result = new ArrayList<String>();
		//String modifiedQuery = escapeCustomSyntax(query);
		//modifiedQuery = a.matcher(modifiedQuery).replaceAll("\\\\/");
		query = a.matcher(query).replaceAll("\\\\/");
		
		StandardSyntaxParser parser = new StandardSyntaxParser(new FastCharStream(new StringReader(query)));
		Token t;
		while(true){
			try{
				t = parser.getNextToken();
			}catch(Exception e){
				logger.error(e);
				return null;
			}catch(TokenMgrError e){
				logger.error(e);
				return null;
			}
			String tokenString = t.toString().trim();
			if(tokenString.isEmpty()) { break; }
			else{
				tokenString = b.matcher(tokenString).replaceAll("/");
				result.add(tokenString);
			}
		}
		return result;
	}
	
	/**
	 * Runs all validations against the provided query string.
	 * @param query The query string to inspect
	 * @return 0 validations if no issues were found, otherwise a collection of errors and/or warnings
	 */
	public static List<QueryValidationInfo> validate(String query){
		if(NuixVersion.getCurrent().isAtLeast("6.2.0")){
			knownNuixFields.add("top-level-item-date");
		}
		
		List<QueryValidationInfo> results = new ArrayList<QueryValidationInfo>();
		
		//Check for some of the easy errors up front
		String queryNormalized = query.toLowerCase().trim();
		if(queryNormalized.equals("and")){
			results.add(new QueryValidationInfo(ValidationType.ERROR,"'AND' alone is not a valid query."));
			return results;
		}
		
		if(queryNormalized.equals("or")){
			results.add(new QueryValidationInfo(ValidationType.ERROR,"'OR' alone is not a valid query."));
			return results;
		}
		
		//Check for errors using tokenized query
		List<String> tokens = tokenizeQuery(query);
		if(tokens == null){
			results.add(new QueryValidationInfo(ValidationType.ERROR,"Query syntax error, couldn't parse query."));
			return results;
		}
		
		QueryValidationInfo info;
		info = validateBalancedParens(tokens);
		if(info != null) results.add(info);
		
		results.addAll(validateRegexDelimiters(tokens));
		results.addAll(validateWildcards(tokens));
		results.addAll(validateFieldNames(tokens));
		results.addAll(validateRegexSyntax(tokens));
		
		//If there are no Error level validations
		if(!results.stream().anyMatch(v -> v.isError())){
			//Perform one final check using Nuix
			Map<String,Object> options = new HashMap<String,Object>();
			options.put("limit", 0);
			try{
				NuixDataBridge.getCurrentCase().search(query, options);
			}catch(Exception exc){
				Throwable cause = exc.getCause();
				String message = exc.getMessage();
				if(cause != null)
					message = message +": "+cause.getMessage();
				results.add(new QueryValidationInfo(ValidationType.ERROR, message));
			}
		}
		
		return results;
	}
	
	/**
	 * Strips quotes from a token
	 * @param token The token that may or may not have quotes around it
	 * @return Token with outer quotes removed
	 */
	protected static String stripQuotes(String token){
		return stripQuotesPattern.matcher(token.trim()).replaceAll("");
	}
	
	protected static String stripRegexSlashses(String token){
		return stripRegexSlashesPattern.matcher(token).replaceAll("");
	}
	
	/**
	 * Counts the number of times a particular token occurs in the provided collection of tokens
	 * @param tokens Tokens to search
	 * @param token The token string you are looking to count
	 * @return The number of times the token occurred
	 */
	protected static int countOccurrences(List<String> tokens, String token){
		return (int)tokens.stream().filter(t -> t.equals(token)).count();
	}
	
	/**
	 * Determines whether a token is a phrase (surrounded by ")
	 * @param token The token to check
	 * @return True if the token appears to be a phrase
	 */
	protected static boolean isPhrase(String token){
		return token.startsWith("\"") && token.endsWith("\"") && token.length() > 1;
	}
	
	/**
	 * Determines whether a token is a Regex expression (surrounded by /)
	 * @param token The token to check
	 * @return True if the token appears to be a Regex
	 */
	protected static boolean isRegex(String token){
		return token.startsWith("/") && token.endsWith("/") && token.length() > 1;
	}
	
	/**
	 * Looks through tokens and determines the unique fields used.  Fields are a token followed by ":" token.
	 * @param tokens Tokens to inspect
	 * @return The distinct fields located
	 */
	protected static List<String> getFields(List<String> tokens){
		Set<String> fields = new HashSet<String>();
		String previousToken = "";
		for(String token : tokens){
			if(token.equals(":")){
				fields.add(previousToken);
			}
			previousToken = token;
		}
		return new ArrayList<String>(fields);
	}
	
	/**
	 * Determines whether the count of left parenthesis '(' matches the count of right parenthesis ')'
	 * @param tokens The tokens to inspect
	 * @return Validation info, null if no issues were detected
	 */
	protected static QueryValidationInfo validateBalancedParens(List<String> tokens){
		int left = countOccurrences(tokens,"(");
		int right = countOccurrences(tokens,")");
		if(left != right){
			return new QueryValidationInfo(ValidationType.ERROR,"Left and right parentheses counts don't match");
		}else{
			return null;
		}
	}
	
	/**
	 * Determines if any Regex tokens only contain a leading or trailing slash /
	 * @param tokens The tokens to inspect
	 * @return 0 or more validations, depending on the issues discovered
	 */
	protected static List<QueryValidationInfo> validateRegexDelimiters(List<String> tokens){
		List<QueryValidationInfo> results = new ArrayList<QueryValidationInfo>();
		for(String token : tokens){
			if(invalidRegexDelimiterPattern.matcher(token).matches()){
				results.add(new QueryValidationInfo(ValidationType.ERROR,"Invalid Regex delimiters: "+token));
			}
		}
		return results;
	}
	
	/**
	 * Validates wild card usage.
	 * Will generate errors for wild cards without prefix or suffix non wild card characters.
	 * Will generate warnings for * wild cards being used as prefix (ex: *at)  
	 * @param tokens The tokens to inspect
	 * @return 0 or more validations depending on the issues discovered.
	 */
	protected static List<QueryValidationInfo> validateWildcards(List<String> tokens){
		List<QueryValidationInfo> results = new ArrayList<QueryValidationInfo>();
		String previousToken = "";
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			String nextToken = (i + 1 < tokens.size()) ? tokens.get(i+1) : "";
			//String nextNextToken = (i + 2 < tokens.size()) ? tokens.get(i+2) : "";
			//System.out.println("====="+i+"=====");
			//System.out.println("token: "+token);
			//System.out.println("nextToken: "+nextToken);
			//System.out.println("nextNextToken: "+nextNextToken);
			
			if(invalidWildcardPattern.matcher(stripQuotes(token)).matches() &&
					!previousToken.equals(":") &&
					!previousToken.equals("[") &&
					!previousToken.equalsIgnoreCase("to") &&
					!nextToken.equals("]") &&
					!nextToken.equals(":")
			){
				results.add(new QueryValidationInfo(ValidationType.WARNING,"Wildcard without prefix or suffix: "+token));
			}else if(starPrefixPattern.matcher(stripQuotes(token)).matches()){
				results.add(new QueryValidationInfo(ValidationType.WARNING,"Star wildcard as prefix: "+token));
			}else if(isPhrase(token) && !nextToken.equals(":")){
				List<String> subTokens = tokenizeQuery(stripQuotes(token));
				if(subTokens != null && subTokens.size() > 0){
					results.addAll(validateWildcards(subTokens));
				}else{
					//results.add(new QueryValidationInfo(ValidationType.ERROR,"Unable to parse phrase: "+token));
				}
			}
			previousToken = token;
		}

		return results;
	}
	
	/**
	 * Compares field names located in query with a list of known Nuix search fields.
	 * @param tokens The tokens to inspect
	 * @return 0 or more validations depending on the issues discovered.
	 */
	protected static List<QueryValidationInfo> validateFieldNames(List<String> tokens){
		List<QueryValidationInfo> results = new ArrayList<QueryValidationInfo>();
		List<String> fields = getFields(tokens);
		for(String field : fields){
			if(field.startsWith("\"") && field.endsWith("\"")){
				continue;
			}
			if(!knownNuixFields.contains(field.toLowerCase())){
				results.add(new QueryValidationInfo(ValidationType.WARNING,"Field not recognized as Nuix search field: "+field));
			}
		}
		return results;
	}
	
	/**
	 * Locates regex expression in tokens and uses Java to compile them, reporting any regex syntax errors thrown.
	 * @param tokens The tokens to inspect
	 * @return 0 or more validations depending on the issues discovered.
	 */
	protected static List<QueryValidationInfo> validateRegexSyntax(List<String> tokens){
		List<QueryValidationInfo> results = new ArrayList<QueryValidationInfo>();
		for(String token : tokens){
			//System.out.println("token: "+token+", isRegex: "+isRegex(token));
			if(isRegex(token)){
				String expression = stripRegexSlashses(token);
				try{
					Pattern.compile(expression);
				}catch(PatternSyntaxException pse){
					results.add(new QueryValidationInfo(ValidationType.ERROR,"Bad regular expression syntax: "+expression));
				}
			}
		}
		return results;
	}
}
