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
import com.aemlibrary.com.core.caconfig.InitiatorConfig;

@Component(service = ParticipantStepChooser.class, property = {"chooser.label="+ "Reviewer Participant Selector"})
public class ReviewerParticipantStep implements ParticipantStepChooser {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReviewerParticipantStep.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory ;

	@Override
	public String getParticipant(WorkItem item, WorkflowSession session, MetaDataMap metadata) throws WorkflowException {
		ResourceResolver resolver = null;
		String reviewerUser = "";
		
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
						InitiatorConfig config = builder.as(InitiatorConfig.class);
						if(config != null) {
							String participant = config.initiatorGroup();
							if(participant !=null) {
								reviewerUser = participant;
								LOG.info("Using Config {}",reviewerUser);
							}else {
								reviewerUser = config.defaultGroup();
								LOG.info("Using Admin Config {}",reviewerUser);
							}
							
						}
					}
					
							
				}
			}
			
		} catch (LoginException e) {
			
			LOG.error(e.getMessage());
		}	
		LOG.info("Participant Step chooser for review {}",reviewerUser);
		
		return reviewerUser;
	}

}
