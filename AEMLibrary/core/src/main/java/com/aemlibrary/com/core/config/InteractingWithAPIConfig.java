package com.aemlibrary.com.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Inetracting with Servlet Config")
public @interface InteractingWithAPIConfig {
	
	@AttributeDefinition(name = "End Point URL",description = "End points of the Search/Recepie API")
	String url();
	
	@AttributeDefinition(name = "Message if URL is not known",description = "End User/BA to provide the Message to be displayed")
	String defaultMessage();

}
