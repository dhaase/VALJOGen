module ExamplesHelper  
  @syntax = { ".java" => "java", ".stg" => "", ".txt" => "" }
  @headings = { ".java" => "Source example", ".stg" => "Custom template example", ".txt" => "Custom header example" }
  
  # get hash of files and their content that exist at path
  def get_example_files(path)
    result = Hash.new 
    
    files = Dir["#{path}/**/**/*.*"]    
    files.each do |file|
      name = File.basename(file)
      contentFile = File.open(file, 'r').read
      content = ""
      contentFile.each_line do |line|
       content << line
      end
      
      # No need to supply copyright as we supply this in the site.
      content=content.sub(/\/*[^C]*Copyright[^*]*\*\//, '')
      
      result[name]=content
    end
    result
  end
  
  # generate markdown containing all examples
  def generate_example_markdown(srcExamplesHash, outputExamplesHash)
    newline="\n"

    # Heading:      
    examples = "# Examples for VALue Java Objects Generator (VALJOGen)"+newline+"<br />"+newline

    # Index:
    examples << "**EXAMPLES INDEX:**"+newline+newline
    orderedSrcHash = srcExamplesHash.sort_by { |name, content| [File.extname(name).downcase, get_example_index(content), name.downcase] }
    orderedSrcHash.each do |name, content|
        extension=File.extname(name).downcase
        raise "Unknown source file extension "+extension if (!@syntax[extension] || !@headings[extension])
        initialJavadocExp = /\/\*\*(\*|\r|\n\s)*([^\.]+\.?)(.|\r|\n)*\*\//.match(content)
            
        examples << "- [<code>"+name+"</code>](#"+get_source_link_id(name)+")"
        if (initialJavadocExp)
          description=initialJavadocExp.captures[1].gsub("\*", " ").strip
          examples << " - " + description
        end
        examples << newline+newline
    end
    
    examples << newline
    examples << "---"
    examples << newline+newline
    
    # Examples:
    orderedSrcHash.each do |name, content|
      extension=File.extname(name).downcase
      
      # Example:
      examples << "## "+@headings[extension]+": <code>"+name+"</code>"
      examples << newline+newline
      examples << "```"+@syntax[extension]+newline
      examples << content+newline
      examples << "```"+newline
      examples << newline
    
      # References:
      references=(srcExamplesHash.select{|refName, refContent| refName!=name && (content.include?('"'+refName+'"') || refContent.include?('"'+name+'"')) }) || {}
      orderedReferences = references.sort_by { |refName, refContent| [File.extname(refName).downcase, refName.downcase] }
    
      links=[]
      orderedReferences.each do |refName, refContent|
        link = get_source_link_id(refName)
        links << "[<code>"+refName+"</code>](#"+link+")"
      end
    
      examples << "**See also:** "+links.join(', ')+newline unless links.empty?
    
      examples << newline
    
      # Output:
      if (extension=='.java')
        clazzName = File.basename( name, ".*" )
        output = outputExamplesHash.find {|outputName, outputContent| Regexp.new('@Generated\(.*\"[^\"]*com.fortyoneconcepts.valjogen.examples.'+clazzName+'(<.+>)?\"\.*\)').match(outputContent) }
        #raise "Could not find output for source file '"+name+"'" if !output

        if (output)
           (outputName, outputContent) = output
           outputExtension=File.extname(outputName).downcase
         
           sourceLink = "[<code>"+name+"</code>](#"+get_source_link_id(name)+")"
         
           dependencyLinks = []
           dependencyLinks << sourceLink
           dependencyLinks.concat(links.clone)
                   
           examples << newline+newline
           examples << "### Generated output: "+(dependencyLinks.length>1 ? "(" : "")+dependencyLinks.join(', ')+(dependencyLinks.length>1 ? ")" : "")+" => <code>"+outputName+"</code> :"+newline+newline
           examples << "```"+@syntax[outputExtension]+newline
           examples << outputContent+newline
           examples << "```"+newline
           examples << newline+newline
        end  
      end
      
      examples << "---"
      examples << newline+newline
    end

    examples
 end
 
 # Get index from source code comment in VALJOGen annotations of format "Example xxx". 
 def get_example_index(src)
   regEx=src.match('@VALJO[^\(]*\([^)]*comment="Example (\d*)"[^)]*\)')
   if (regEx)
     indexStr=regEx.captures[0]
     indexStr.to_i
   else
     9999
   end
 end
 
 # Get ID that markdown has assigned automatically to filename topic.
 def get_source_link_id(filename)
   extension=File.extname(filename).downcase
   (@headings[extension]+" code"+filename+"code").downcase.gsub(" ", "-").gsub(/[^A-Za-z0-9-]/,"")
 end
 
end