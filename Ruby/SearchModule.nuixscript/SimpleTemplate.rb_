class SimpleTemplate
	attr_accessor :fixed_placeholders
	attr_accessor :template

	def initialize(template,fixed_placeholders={})
		@template = template
		@deferred_tagging_cache = Hash.new{|hash,key| hash[key] = []}
		@fixed_placeholders = fixed_placeholders || {}
	end

	def add_fixed_placeholders(hash)
		@fixed_placeholders = @fixed_placeholders.merge(hash)
	end

	def resolve_placeholders(dynamic_placeholders=nil)
		result = @template
		@fixed_placeholders.each do |key,value|
			result = result.gsub(/\{#{key}\}/,value)
		end

		if !dynamic_placeholders.nil?
			dynamic_placeholders.each do |key,value|
				result = result.gsub(/\{#{key}\}/,value)
			end
		end

		return result
	end
end