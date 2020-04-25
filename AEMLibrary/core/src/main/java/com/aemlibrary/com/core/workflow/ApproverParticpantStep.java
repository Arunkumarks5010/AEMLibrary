package com.aemlibrary.com.core.workflow;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aemlibrary.com.core.caconfig.ApproverConfig;
import com.aemlibrary.com.core.caconfig.InitiatorConfig;



@Component(service = ParticipantStepChooser.class, property = {"chooser.label = Approver Participant Selector"} )
public class ApproverParticpantStep implements ParticipantStepChooser {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApproverParticpantStep.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Override
	public String getParticipant(WorkItem item, WorkflowSession session, MetaDataMap map) throws WorkflowException {
		ResourceResolver resolver = null;
		String approverUser = "";
		
		String payload = item.getWorkflowData().getPayload().toString();
		
		LOG.info("Pay Load Path {}",payload);
		
		Map<String,Object> params = new HashMap<>();
		params.put(ResourceResolverFactory.SUBSERVICE, "aemlibrary");
		try {
			resolver = resolverFactory.getServiceResourceResolver(params);
			if(resolver != null) {
				Resource resource = resolver.getResource(payload);
				if(null !=resource) {
					ConfigurationBuilder builder = resource.adaptTo(ConfigurationBuilder.class);
					if(null != builder) {
						ApproverConfig config = builder.as(ApproverConfig.class);
						if(config != null) {
							String participant = config.approverGroup();
							
							if(participant !=null) {
								approverUser = participant;
								LOG.info("Using Config {}",approverUser);
							}else {
								approverUser = config.defaultGroup();
								LOG.info("Using Admin Config {}",approverUser);
							}
							
						}
					}
					
				}
			}
			
		} catch (LoginException e) {
			
			LOG.error(e.getMessage());
		}	
		LOG.info("Participant Step chooser for review {}",approverUser);
		
		return approverUser;
	}


}
