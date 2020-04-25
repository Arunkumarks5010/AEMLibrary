package com.aemapple.com.core.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;


@Component(service=WorkflowProcess.class,property= {"process.label="+"Adding Company Property to Page"})
public class AddingCompanyNameToPage implements WorkflowProcess {

	private static final Logger LOG = LoggerFactory.getLogger(AddingCompanyNameToPage.class);


	@Reference
	private ResourceResolverFactory resolverFactory ;

	
	/***
	 * aemlibrary --> system user -> used to obtain resource resolver
	 */
	@Override
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap metadata) throws WorkflowException {
		String payload = item.getWorkflowData().getPayload().toString();		
		Session session = null;
		ResourceResolver resolver = null;
		LOG.info("Execute method of class {} ",payload);
		Map<String,Object> params = new HashMap<>();
		params.put(ResourceResolverFactory.SUBSERVICE, "aemlibrary");

		try {		
			resolver = resolverFactory.getServiceResourceResolver(params);
			if(resolver != null) {
				Resource contentResource = resolver.getResource(payload).adaptTo(Page.class).getContentResource();
				if(null !=contentResource) {
					session = resolver.adaptTo(Session.class);
					Node node = contentResource.adaptTo(Node.class);
					node.setProperty("company","Apple Inc");
					session.save();				
				}
			}
		}catch (Exception e) {
			LOG.error("Error stack for AddingCompanyNameToPage Class",e.getStackTrace());

		}finally {
			session.logout();
		}


	}

}
