package com.aemlibrary.com.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Create Page Configs")
public @interface CreatePageServiceConfig {

	@AttributeDefinition(name = "Page Path",description = "Provide the path under which it has to be created", type = AttributeType.STRING)
	String pagePath() default "/content/we-retail/language-masters/taglist";
	
	@AttributeDefinition(name = "Template Path", description = "Using which Template Page has to be created", type = AttributeType.STRING)
	String templatePath() default "/conf/we-retail/settings/wcm/templates/hero-page";
}
