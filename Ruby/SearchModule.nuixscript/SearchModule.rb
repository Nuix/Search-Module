# Menu Title: Search Module 3
# Needs Case: true

def loadlocal(filename)
	path = File.join(File.dirname(__FILE__),filename)
	begin
		load path
	rescue Exception => exc
		puts "Error loading dependency: #{path}"
		puts exc.message
		puts exc.backtrace
	end
end

def requirelocal(filename)
	path = File.join(File.dirname(__FILE__),filename)
	begin
		require path
	rescue Exception => exc
		puts "Error loading dependency: #{path}"
		puts exc.message
		puts exc.backtrace
	end
end

require "java"
loadlocal("Xlsx.rb")
loadlocal("SimpleTemplate.rb")
requirelocal("SearchModule3GUI.jar")
loadlocal("BootStrap.rb")

if Nx::NuixVersion.getCurrent.isLessThan("7.0")
	puts "This version of search module is not compatible with versions of Nuix prior to 7.0"
	exit 1
end

#This allows script to run through workbench, or via the console.  If running
#via the console and no value for "-Dscript.casepath" is provided, a prompt
#dialog is shown
if $current_case.nil?
	if ENV_JAVA["script.casepath"].nil?
		java_import javax.swing.JFileChooser
		fc = JFileChooser.new
		fc.setDialogTitle("Choose case directory")
		fc.setCurrentDirectory(java.io.File.new("C:\\"))
		fc.setFileSelectionMode(JFileChooser::DIRECTORIES_ONLY)
		file = nil
		if fc.showOpenDialog(nil) == JFileChooser::APPROVE_OPTION
			file = fc.getSelectedFile
		end
		if file.nil?
			puts "User did not select a case to work with, exiting"
			exit 1
		else
			puts "Opening Case:	#{file.getAbsolutePath}"
			$current_case = $utilities.getCaseFactory.open(file,{:migrate => true})
		end
	else
		puts "Opening Case: #{ENV_JAVA["script.casepath"]}"
		$current_case = $utilities.getCaseFactory.open(ENV_JAVA["script.casepath"])
	end
	#Hook into JVM exit to close case if we opened here
	java.lang.Runtime.getRuntime.addShutdownHook(Class.new(Java::java.lang.Thread) {
		def run
			$current_case.close
		end
	}.new)
end

module SM
	include_package "com.nuix.searchmodule"
	module Dialogs
		include_package "com.nuix.searchmodule.dialogs"
	end
end

SM::SearchModulePersistedSettings.setSettingsFile("#{File.dirname(__FILE__)}\\SearchModule.properties")
dialog = SM::Dialogs::MainDialog.new(File.dirname(__FILE__))
dialog.fitToScreen

headers_style = {
	:font_size => 11,
	:font_bold => true
}

category_methods = {
	:hits => :getHits,
	:top_level_hits => :getTopLevelHits,
	:family_hits => :getFamilyHits,
	:unique_hits => :getUniqueHits,
	:unique_top_level => :getUniqueTopLevel,
	:unique_family => :getUniqueFamily,
}
category_symbols = category_methods.keys

while true #Loop until user cancels.  Loop will be show dialog, run, show dialog, run, ...
	file_timestamp = Time.now.strftime("%Y%m%d_%H%M%S")
	default_report_file = "#{$current_case.getLocation.getAbsolutePath}\\Reports\\SearchModule3_#{file_timestamp}.xlsx"

	dialog.setReportFilePath(default_report_file)
	dialog.setVisible(true)
	if dialog.getDialogResult == false
		break
	end
	dialog.resetDialogResult

	category_settings_hash = dialog.getCategorySettings
	category_settings_list = dialog.getCategorySettingsList
	report_file = dialog.getReportFile
	report_file.getParentFile.mkdirs #Attempt to make sure report file directory exists
	terms = dialog.getTerms
	handle_excluded = dialog.getHandleExcludedItems
	include_cover_sheet = dialog.getProject.getIncludeCoverSheet
	cover_sheet_information = dialog.getProject.getCoverSheetInformation
	tags_require_hits = dialog.getProject.getTagsRequireHits
	include_logo = dialog.getProject.getIncludeLogo
	report_reviewable_by_custodian = dialog.getProject.getReportReviewableByCustodian

	#Auto save these settings
	dialog.getProject.saveToFile("#{$current_case.getLocation.getAbsolutePath}\\Reports\\SearchModule3Project_#{file_timestamp}.sm3p")
	dialog.getProject.saveToFile("#{File.dirname(__FILE__)}\\LastRunProject.sm3p")

	start_time = Time.now

	scope_query_generator = dialog.getScopeQueryGenerator
	if terms.size < 1
		#If user provided no terms we convert scope to term then blank out scope
		terms << com.nuix.searchmodule.SearchTermInfo.new(scope_query_generator.toQuery)
		scope_query_generator = com.nuix.nx.query.QueryGenerator.new
	end
	fields = dialog.getFields

	fixed_placeholders = {
		"date" => Time.now.strftime("%Y%m%d"),
		"time" => Time.now.strftime("%H:%M:%S"),
		"datetime" => Time.now.strftime("%Y%m%d_%H%M%S"),
	}

	report_start_time = nil
	progress_dialog = nil
	Nx::Dialogs::ProgressDialog.forBlock do |pd|
		progress_dialog = pd
		#======================================#
		# Bulk Searching & Unique Calculations #
		#======================================#
		pd.setTitle("Search Module 3")
		pd.setMainProgress(0,terms.size)
		pd.setSubProgressVisible(false)
		pd.setAbortButtonVisible(false)
		bulk_searcher = SM::BulkSearchAnnotater.new
		bulk_searcher.setSettings(category_settings_list)
		bulk_searcher.setRemoveExcluded(handle_excluded)
		bulk_searcher.setTagsRequireHits(tags_require_hits)
		bulk_searcher.whenProgressUpdated do |main_status,sub_status,current,total|
			pd.setMainStatus(main_status)
			pd.setSubStatus(sub_status)
			pd.setMainProgress(current)
		end
		pd.logMessage "\nPerforming Searching..."
		results = bulk_searcher.performSearching($current_case,terms,scope_query_generator,fields)

		pd.logMessage bulk_searcher.getTimingsDump

		items_by_category = {}
		category_symbols.each do |category|
			next if !category_settings_hash[category.to_s].getReportCount
			items_by_category[category] = SM::ItemUtilityX.unionMany(results.reject{|r|r.getHadError}.map{|r|r.send(category_methods[category])})
		end

		#===================#
		# Report Generation #
		#===================#

		#Used for getting dimensions of logo files
		java_import java.awt.image.BufferedImage
		java_import javax.imageio.ImageIO

		logo_lg_w = 650
		logo_lg_h = 339
		sub_logo_h = 105

		#Logo
		logo_file = File.join("#{File.dirname(__FILE__)}","logo.png")
		logo_file_exists = java.io.File.new(logo_file).exists
		if !logo_file_exists
			pd.logMessage "Logo image file could not be located at #{logo_file}"
			pd.logMessage "No logo will be inserted into report."
		else
			img = ImageIO.read(java.io.File.new(logo_file))
			logo_lg_w = img.getWidth
			logo_lg_h = img.getHeight
			pd.logMessage "Logo Dimensions: #{logo_lg_w}x#{logo_lg_h}"
		end

		#Sub logo
		sub_logo_file = File.join("#{File.dirname(__FILE__)}","sub_logo.png")
		sub_logo_file_exists = java.io.File.new(sub_logo_file).exists
		if !sub_logo_file_exists
			pd.logMessage "Sub Logo image file could not be located at #{sub_logo_file}"
			pd.logMessage "No sub logo will be inserted into report."
		else
			img = ImageIO.read(java.io.File.new(sub_logo_file))
			sub_logo_h = img.getHeight
			pd.logMessage "Sub Logo Height: #{sub_logo_h}"
		end

		#Small logo 1/5th original size
		logo_sm_w = logo_lg_w / 5
		logo_sm_h = logo_lg_h / 5

		report_start_time = Time.now
		errored_search_results = results.select{|r|r.getHadError}
		pd.logMessage "Errored Searches: #{errored_search_results.size}"

		xlsx = Xlsx.new

		if include_cover_sheet
			#COVER SHEET
			sheet = xlsx.get_sheet("Cover")

			#We set col width where logo would be regardless so that
			#if they include a cover sheet with information but no logo
			#it looks consistent
			sheet.set_col_width(1,logo_lg_w,:pixels)

			#Insert normal logo
			if logo_file_exists && include_logo
				sheet.set_row_height(2,logo_lg_h,:pixels)
				picture_index = sheet.aspose_worksheet.getPictures.add(2,1,3,2,logo_file)
			end

			#Insert sub logo
			if sub_logo_file_exists && include_logo
				sheet.set_row_height(3,sub_logo_h,:pixels)
				picture_index = sheet.aspose_worksheet.getPictures.add(3,1,4,2,sub_logo_file)
			end

			cover_sheet_template = SimpleTemplate.new(cover_sheet_information,fixed_placeholders)
			cover_sheet_placeholders = {
				"casename" => $current_case.getName,
				"caselocation" => $current_case.getLocation.getPath,
			}
			sheet << [""] #Blank row 0
			sheet << ["",cover_sheet_template.resolve_placeholders(cover_sheet_placeholders)]
			sheet << [""] #Blank row 2
			sheet << [""] #Blank row 3

			#Align information cell contents
			sheet.style_cells(1,1,{
				:v_align => :top,
				:text_wrapped => true,
			})

			#Make info row taller
			sheet.set_row_height(1,1,:inches)
			sheet.set_landscape
		end

		#SEARCHES SHEET
		sheet = xlsx.get_sheet("Searches")
		if logo_file_exists && include_logo
			sheet << [""] #Blank row for logo
		end

		headers = ["Term"]
		if errored_search_results.size > 0
			headers << "Error Message"
		end
		category_symbols.each do |category|
			current_category_settings = category_settings_hash[category.to_s]
			friendly_name = category_settings_hash[category.to_s].getCategoryTitle
			if current_category_settings.getReportCount
				headers << friendly_name
				headers << "#{friendly_name} Audited GB" if current_category_settings.getReportAudited
				headers << "#{friendly_name} File Size GB" if current_category_settings.getReportFileSize
				headers << "#{friendly_name} Tag" if current_category_settings.getApplyTermTags
			end
		end
		headers << "Query"
		sheet << headers

		pd.setMainStatus("Generating Report")
		pd.setSubStatus("")
		pd.setMainProgress(0,results.size)

		results.each_with_index do |result,result_index|
			pd.setMainProgress(result_index+1)
			row = [result.getTerm]
			if errored_search_results.size > 0
				row << result.getErrorMessage
				if result.getHadError
					sheet << row
					next
				end
			end
			category_symbols.each do |category|
				current_category_settings = category_settings_hash[category.to_s]
				if current_category_settings.getReportCount
					category_items = result.send(category_methods[category])
					row << category_items.size
					row << SM::ItemUtilityX.getTotalAuditedSizeGB(category_items) if current_category_settings.getReportAudited
					row << SM::ItemUtilityX.getTotalFileSizeGB(category_items) if current_category_settings.getReportFileSize
					row << result.get("#{category}_term_tag") if current_category_settings.getApplyTermTags
				end
			end
			row << result.getQuery
			sheet << row
		end

		if logo_file_exists && include_logo
			sheet.style_cells(1,nil,headers_style)
		else
			sheet.style_cells(0,nil,headers_style)
		end
		sheet.auto_fit_columns
		sheet.set_landscape
		sheet.set_print_gridlines(true)

		if logo_file_exists && include_logo
			#Insert small logo
			current_col_width = sheet.aspose_worksheet.getCells.getColumnWidth(0)
			if current_col_width < logo_sm_w
				sheet.set_col_width(0,logo_sm_w,:pixels)
			end
			sheet.set_row_height(0,logo_sm_h,:pixels)
			picture_index = sheet.aspose_worksheet.getPictures.add(0,0,1,1,logo_file)
		end

		# REVIEWABLE COUNTS BY CUSTODIAN SHEET
		reviewable_category_settings = category_settings_hash["family_hits"]
		if reviewable_category_settings.getReportCount && report_reviewable_by_custodian
			sheet = xlsx.get_sheet("Reviewable by Custodian")
			if logo_file_exists && include_logo
				sheet << [""] #Blank row for logo
			end
			reviewable_items = items_by_category[:family_hits]
			counts_by_custodian = Hash.new{|h,k|h[k]=0}
			reviewable_items.each{|item|counts_by_custodian[item.getCustodian || ""] += 1}
			sheet << ["Custodian","Reviewable Item Count"]
			counts_by_custodian.sort_by{|custodian,count|custodian}.each do |custodian,count|
				if custodian.strip.empty?
					custodian = "No Custodian"
				end
				sheet << [custodian,count]
			end
			if logo_file_exists && include_logo
				sheet.style_cells(1,nil,headers_style)
			else
				sheet.style_cells(0,nil,headers_style)
			end
			sheet.auto_fit_columns
			sheet.set_landscape
			sheet.set_print_gridlines(true)

			if logo_file_exists && include_logo
				#Insert small logo
				current_col_width = sheet.aspose_worksheet.getCells.getColumnWidth(0)
				if current_col_width < logo_sm_w
					sheet.set_col_width(0,logo_sm_w,:pixels)
				end
				sheet.set_row_height(0,logo_sm_h,:pixels)
				picture_index = sheet.aspose_worksheet.getPictures.add(0,0,1,1,logo_file)
			end
		end

		#SUMMARY SHEET
		sheet = xlsx.get_sheet("Summary")
		sheet << ["Scope Query",""] #We will populate this after column sizing so it can overflow
		sheet << ["Default Fields",fields.join(", ")]
		sheet << ["Terms",terms.size]

		category_symbols.each do |category|
			current_category_settings = category_settings_hash[category.to_s]
			next if !current_category_settings.getReportCount
			friendly_name = category_settings_hash[category.to_s].getCategoryTitle
			sheet << ["Total #{friendly_name}",items_by_category[category].size]
			if current_category_settings.getReportAudited
				sheet << ["Total #{friendly_name} Audited GB",SM::ItemUtilityX.getTotalAuditedSizeGB(items_by_category[category])]
			end

			if current_category_settings.getApplyCategoryTags
				template = SimpleTemplate.new(current_category_settings.getCategoryTagTemplate,fixed_placeholders)
				tag = template.resolve_placeholders({"category" => friendly_name})
				sheet << ["#{friendly_name} Tag",tag]
			end
		end

		if errored_search_results.size > 0
			sheet << ["Errored Searches",errored_search_results.size]
		end

		bulk_searcher.getTimings.each do |name,time|
			sheet << ["#{name} Total Time",time]
		end

		sheet << ["Nuix Version",NUIX_VERSION]
		sheet << ["Search Module Version",com.nuix.nx.SearchModuleVersion.getVersion]

		sheet.style_cells(nil,0,headers_style)
		sheet.auto_fit_columns
		sheet[0,1] = scope_query_generator.toQuery #Write scope query after other data is autosized to
		sheet.set_landscape
		sheet.set_print_gridlines(true)

		xlsx.save(report_file)

		report_finish_time = Time.now
		finish_time = Time.now

		puts "Total Reporting Time: #{Time.at(report_finish_time-report_start_time).gmtime.strftime("%H:%M:%S")}"
		puts "Total Time: #{Time.at(finish_time-start_time).gmtime.strftime("%H:%M:%S")}"

		if SM::SearchModulePersistedSettings.getOpenReportOnCompletion
			java.awt.Desktop.getDesktop.open(report_file)
		end
	end

	progress_dialog.setMainStatus("Report Generated")

	while !progress_dialog.nil? && progress_dialog.isShowing
		sleep(0.5)
	end
end

if !$window.nil? && dialog.getCategorySettingsList.any?{|cs|cs.getApplyCategoryTags || cs.getAppendCustomMetadata}
	$window.openTab("workbench",{"search"=>""})
end

dialog.dispose
