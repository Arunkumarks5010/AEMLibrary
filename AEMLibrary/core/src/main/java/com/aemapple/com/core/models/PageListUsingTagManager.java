package com.aemapple.com.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

@Model(adaptables= {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageListUsingTagManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(PageListUsingTagManager.class);
	
	@Inject
	private String rootPagePath;
	
	@Inject 
	private String[] tags;

	@Inject
	private ResourceResolver resolver;
	
	private Map<String,String> pageDetails ;
	
	@PostConstruct
	public void init() {
		LOG.info("Inside Init medthod of {}",this.getClass());
		if(rootPagePath != null && tags != null) {
		getPageList();
		}
		
	}
	
	private void getPageList() {

	
		TagManager manager = resolver.adaptTo(TagManager.class);
		RangeIterator<Resource> resources = manager.find(rootPagePath, tags);
		if(resources!=null) {			
			List<Resource> pageResources = new ArrayList<Resource>();
			resources.forEachRemaining(pageResources::add);
			if(pageResources.size()>0) {
				pageDetails = new TreeMap<String,String>();
				pageResources.forEach(resource ->{					
					LOG.info("Resource {}",resource.getParent().getName());
					Page page = resource.getParent().adaptTo(Page.class);
					LOG.info("Page Title {} and Path {}",page.getTitle(),page.getPath());
					pageDetails.put(page.getTitle(),page.getPath());
				});
			}
			
		}
		
	}

	public Map<String,String> getPageDetails() {
		return pageDetails;
	}


}
