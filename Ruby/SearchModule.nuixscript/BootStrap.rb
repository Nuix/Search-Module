require 'java'
com.nuix.nx.LookAndFeelHelper::setWindows

require 'json'

module Nx
	include_package "com.nuix.nx"
	java_import "com.nuix.nx.NuixVersion"
	module Query
		include_package "com.nuix.nx.query"
	end

	NuixVersion::setCurrentVersion(NUIX_VERSION)
	puts "Running Search Module #{com.nuix.nx.SearchModuleVersion.getVersion} in Nuix #{NuixVersion.getCurrent}"

	NuixDataBridge::whenCurrentCaseRequested do
		next $current_case
	end

	NuixDataBridge::whenUtilitiesRequested do
		next $utilities
	end

	NuixDataBridge::setWindow($window)

	script_directory = File.dirname(__FILE__)
	query_data_file = File.join(script_directory,"QueryData.json")
	query_data = JSON.parse(File.read(query_data_file))

	NuixDataBridge.setFlags(query_data["flags"])

	date_fields = []
	# These are "native" search fields
	date_fields += query_data["dateFields"].map{|v|NameValuePair.new(v)}
	# These are metadata properties that are likely to contain a date
	date_fields += query_data["dateProperties"].map{|v|NameValuePair.new(v,"date-properties:\"#{v}\"")}
	puts "Loading #{date_fields.size} date field choices..."
	NuixDataBridge.setCommonDateFields(date_fields)

	integer_fields = []
	# These are "native" search fields
	integer_fields += query_data["integerFields"].map{|v|NameValuePair.new(v)}
	# These are metadata properties that are likely to contain an integer
	integer_fields += query_data["integerProperties"].map{|v|NameValuePair.new(v,"integer-properties:\"#{v}\"")}
	puts "Loading #{integer_fields.size} integer field choices..."
	NuixDataBridge.setCommonIntegerFields(integer_fields)

	# These are metadata properties that are likely to contain text
	puts "Loading #{query_data["textFields"].size} text field choices..."
	NuixDataBridge.setCommonTextFields(query_data["textFields"].map{|value|NameValuePair.new(value)})

	# Set list of "known fields".  If the query validator code finds a search field not present in this list it
	# will report it as an unknown field in the users term
	puts "Loading #{query_data["queryValidatorKnownFields"].size} known fields into QueryValidator..."
	com.nuix.searchmodule.query.QueryValidator.setKnownFields(query_data["queryValidatorKnownFields"])
	
	module Dialogs
		include_package "com.nuix.nx.dialogs"
	end

	module ItemOperations
		include_package "com.nuix.nx.itemoperations"
	end
end