package com.aemlibrary.com.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.AttributeType;;

@ObjectClassDefinition(name = "Scheduler Configuration to Create Page at certain Interval")
public @interface PageCreationSchedulerConfig {
	
	@AttributeDefinition(name = "Scheduler Name", description = " Provide the Name of the scheduler", type = AttributeType.STRING)
	String schedulerName() default "Page Creation";
	
	@AttributeDefinition(name = "Enable", description = "Enable to activate the scheduler",type = AttributeType.BOOLEAN)
	boolean serviceEnable() default false;

	@AttributeDefinition(name = "Expression", description = "Cron - job Expression", type = AttributeType.STRING)
	String schedulerExpressions() default "0 0 0/1 1/1 * ? *";
	
	@AttributeDefinition(name = "Concurrent", description = "Schedule Task Concurrently", type = AttributeType.BOOLEAN)
	boolean schedulerConcurrent() default false;
}
