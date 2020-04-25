package com.aemlibrary.com.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Approver Config", description = "Reads the approver Configurations")
public @interface ApproverConfig {

	@Property(label = "Approver Group Name", description = "Project Specific Group Name")
	String approverGroup();
	
	@Property(label = "Default Approver Name")
	String defaultGroup() default "admin";
	
}
