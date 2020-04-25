package com.aemapple.com.core.models;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;


@Model(adaptables=SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadCrumb {

	private static final Logger LOG = LoggerFactory.getLogger(BreadCrumb.class);
	
	@Inject
	private Page currentPage;
	
	private List<String> navList;
	
	
	@PostConstruct
	public void init() {	
		
		if(currentPage!=null) {

			LOG.info("Init Method of {} and current Page Path {}", this.getClass().toString(),currentPage.getPath());		 
			navList = Arrays.asList(currentPage.getPath().substring(1,currentPage.getPath().length()).split("/"));
			LOG.info("Size of the List is {} ", navList.size());
			
			
		}
	}
	
	public List<String> getNavList(){		
		return navList;		
	}
	
	
}
