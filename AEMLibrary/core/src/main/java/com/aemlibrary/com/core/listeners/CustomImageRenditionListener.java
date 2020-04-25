package com.aemlibrary.com.core.listeners;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;

@Component(service=EventListener.class,immediate = true)
public class CustomImageRenditionListener implements EventListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomImageRenditionListener.class);
	
	@Reference
	private SlingRepository repository;
	
	@Reference
	private WorkflowService workflowService;
	private Session adminSession;
	
	
	@Activate
	public void activate() {
		String[] nodeTypes = {"cq:Page","cq:PageContent"};
		LOG.info("Inside listner Activate method");
		try {
			adminSession = repository.loginService("datawrite",null);
			adminSession.getWorkspace().getObservationManager().addEventListener(
																	this, 
																	Event.PROPERTY_ADDED|Event.PROPERTY_CHANGED|Event.NODE_REMOVED,
																	"/content/oxy",
																	true,
																	null,
																	nodeTypes,
																	true);
						
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			LOG.error("Error in the event listener",e);
		}
	}
	
	@Deactivate
	public void deactivate() {
		if(adminSession!=null)
			adminSession.logout();
	}

	@Override
	public void onEvent(EventIterator events) {
		// TODO Auto-generated method stub
		LOG.info("Inside listner On Event method");
		
			try {
				while(events.hasNext()) {
					Event event = events.nextEvent();
					String path = event.getPath();
					int eventType = event.getType();
					Property prop = adminSession.getProperty(path);
					LOG.info("Something has been added with value {} Event Type : {}",prop.getString(),eventType);
					
					if(prop.getName().equalsIgnoreCase("imagepath")) {
						
						String imagePath = prop.getString();
						LOG.info("Image Path : {}",imagePath);
						invokeWFProcess(adminSession,imagePath);
						/*prop.setValue(prop.getString()+"!");
						LOG.info("Updated property {}",prop.getString());
						adminSession.save();*/				
						
					}
					
				LOG.info("Something has been changed for different one {}",path);
				}
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				LOG.error("Error in the event",e);
			}
	
		
		
	}
	
	private void invokeWFProcess(Session adminsession,String imagePath) {
		LOG.info("Inside listner On invokeWFProcess method");
		WorkflowSession wfsession = workflowService.getWorkflowSession(adminsession);
		String workflowModelPath = "/var/workflow/models/custom-image-rendition";
		try {
			WorkflowModel model = wfsession.getModel(workflowModelPath);
			WorkflowData wfData = wfsession.newWorkflowData("JCR_PATH", imagePath);
			LOG.info("Inside listner On invokeWFProcess method, Starting the workflow");
			wfsession.startWorkflow(model, wfData);
		} catch (WorkflowException e) {
			
			LOG.error("Error in the event",e);
		}
		
	}

}
