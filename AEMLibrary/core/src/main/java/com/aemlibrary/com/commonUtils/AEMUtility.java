package com.aemlibrary.com.commonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationOptions;

public class AEMUtility {
	
	private static final Logger LOG = LoggerFactory.getLogger(AEMUtility.class);

	/**
	 * This Method accepts the Agentid and upon confirmation it returns the ReplicationOptions
	 */
	public static ReplicationOptions getReplicationOptions(String AgentID) {

		// Create leanest replication options for activation
		ReplicationOptions options = new ReplicationOptions();
		// Do not create new versions as this adds to overhead
		options.setSuppressVersions(true);
		// Avoid sling job overhead by forcing synchronous. Note this will result in serial activation.
		options.setSynchronous(true);
		// Do NOT suppress status update of resource (set replication properties accordingly)
		options.setSuppressStatusUpdate(false); 

		//Use a custom Replication Agent 
		
		options.setFilter(new AgentFilter(){
			public boolean isIncluded(final Agent agent) {
				LOG.info("Agent ID {}",agent.getId());
				return AgentID.equals(agent.getId());
			}
		});

		return options;

	}
	


}
