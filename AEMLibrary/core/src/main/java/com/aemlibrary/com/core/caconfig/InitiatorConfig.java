package com.aemlibrary.com.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Initiator Configuration", description = "Reads the initiator COonfiguration")
public @interface InitiatorConfig {
	
	@Property(label = "Initiator Group Name", description = "Project Specific Group Name")
	String initiatorGroup();
	
	@Property(label = "Default Approver Name")
	String defaultGroup() default "admin";

}
