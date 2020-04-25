package com.aemlibrary.com.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Link Rewriter Transformer Configuration", description = "Provide the regular expression and pattern to shorten the pdf URL")
public @interface LinkRewriterConfig {
	
		
	@AttributeDefinition(name = "Regular Expression", description = "Provide the Regex pattern")
	String regex() default "/content/AEM63App/en||fr";


}
