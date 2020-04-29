package com.aemlibrary.com.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemlibrary.com.core.config.PageCreationSchedulerConfig;
import com.aemlibrary.com.core.service.CreatePageService;

@Component(immediate = true, service = SchedulerForPageCreation.class)
@Designate(ocd=PageCreationSchedulerConfig.class)
public class SchedulerForPageCreation implements Runnable{

	public static final Logger LOG = LoggerFactory.getLogger(SchedulerForPageCreation.class);
	
	@Reference
	private Scheduler scheduler;
	@Reference
	private CreatePageService createPageService;
	
	private String schedulerName;
	private String schedulerExpression;
	private boolean schedulerConcurrency;
	private boolean schedulerEnabled;
	private int schedulerID;
	
	@Activate
	protected void activate(PageCreationSchedulerConfig config) {
		LOG.info("Inside Activate Method");
		schedulerName = config.schedulerName();
		schedulerExpression = config.schedulerExpressions();
		schedulerConcurrency = config.schedulerConcurrent();
		schedulerEnabled = config.serviceEnable();
		schedulerID = schedulerName.hashCode();
		
		startPageCreation();
	}
	
	@Modified
	protected void modified(PageCreationSchedulerConfig config) {	
		LOG.info("Inside Modify Method");
		removerScheduler();
		schedulerName = config.schedulerName();
		schedulerExpression = config.schedulerExpressions();
		schedulerConcurrency = config.schedulerConcurrent();
		schedulerEnabled = config.serviceEnable();
		schedulerID = schedulerName.hashCode();
		startPageCreation();
	}
	
	@Deactivate
	protected void deactivate(PageCreationSchedulerConfig config) {
		LOG.info("Inside deactivate Method");
		removerScheduler();
	}
	
	private void removerScheduler() {
		LOG.info("Inside removeScheduler Method");
		scheduler.unschedule(String.valueOf(schedulerID));
	}
			
	
	private void startPageCreation() {
		LOG.info("Start Page creation method Enabling Status {} and Expression {}", schedulerEnabled, schedulerExpression);
		
		if(schedulerEnabled) {
			ScheduleOptions options = scheduler.EXPR(schedulerExpression);
			options.name(schedulerName);
			options.canRunConcurrently(schedulerConcurrency);
			scheduler.schedule(this,options);		
			
		}
	}

	@Override
	public void run() {
		LOG.info("Calling the Create Page Service for page creation");
		createPageService.createPage();
		LOG.info("Created Page successfully with name {} and Title {}",createPageService.getPageName(),createPageService.getPageTitle());
	}
	
}
