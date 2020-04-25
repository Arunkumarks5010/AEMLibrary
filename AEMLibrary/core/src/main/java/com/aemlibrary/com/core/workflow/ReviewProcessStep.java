package com.aemlibrary.com.core.workflow;

import javax.jcr.Session;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aemlibrary.com.commonUtils.AEMConstants;
import com.aemlibrary.com.commonUtils.AEMUtility;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;



@Component(property = {"process.label="+"Publish content to preview step"})
public class ReviewProcessStep implements WorkflowProcess {
	
	@Reference
	private Replicator replicator;
	
	private static final Logger LOG = LoggerFactory.getLogger(ReviewProcessStep.class);

	@Override
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap data) throws WorkflowException {
		
		LOG.info("Inside Execute Step");
		String payload = item.getWorkflowData().getPayload().toString();
		LOG.info("Pay load {}",payload);
		
		Session session = wfsession.adaptTo(Session.class);
		
		ReplicationOptions options = AEMUtility.getReplicationOptions(AEMConstants.PREVIEW_AGENT_ID);
		
		try {
			replicator.replicate(session, ReplicationActionType.ACTIVATE, payload,options);
		} catch (ReplicationException e) {
			LOG.info(e.getMessage());
			
		}
		
		
		
	}

}
