package com.aemlibrary.com.core.workflow;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(property= {"process.label="+"Custom Rendition Step"})
public class CustomRenditionProcessStep implements WorkflowProcess{
	private static final Logger LOG = LoggerFactory.getLogger(CustomRenditionProcessStep.class);

	@Override
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap data) throws WorkflowException {
		LOG.info("Inside CustomRenditionProcess Step Class");
		String imagePath = item.getWorkflowData().getPayload().toString();
		LOG.info("Inside CustomRenditionProcess Step, ImagePath :{}",imagePath);
		
	}

	
}
