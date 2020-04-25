package com.aemlibrary.com.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;

@Model(adaptables= {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageListForTag {

	private static final Logger LOG = LoggerFactory.getLogger(PageListForTag.class);

	@Inject
	private QueryBuilder builder;

	@Inject
	private ResourceResolver resolver;

	@Inject
	private String rootPagePath;
	
	@Inject 
	private String[] tags;
	

	
	//private String tagName;
	//private String finalTagName;

//	private static final String  TAGSOURCE = "we-retail:activity/";


	private List<String> pageList = new ArrayList<>();


	@PostConstruct
	public void init() {
		LOG.info("Inside Init medthod of {}",this.getClass());
		LOG.info("Tag Name from the component {}",tags);
		LOG.info("Root Path Name from the component {}",rootPagePath);
		if(tags != null) {
		//	searchKeyWordTag(tagName);
			getPageList(tags);
		}
			
	}
/*
	private String searchKeyWordTag(String tagName) {			
		finalTagName = TAGSOURCE.concat(tagName);
		return finalTagName;
	}*/

/***
 * Using Query Builder API retrive the pages on tags
 * @param tagName
 */
	private void getPageList(String[] tags){

		Session session = resolver.adaptTo(Session.class);
		try {
			Map<String,String> queryParams = new HashMap<String,String>();
			queryParams.put("path",rootPagePath);
			queryParams.put("type","cq:Page");
			queryParams.put("property","jcr:content/@cq:tags");
			for(int i=0 ; i <tags.length ; i++) {
				String property = "property."+i+1+"_value";
				queryParams.put(property,tags[i]);
			}

			PredicateGroup pGroup = PredicateGroup.create(queryParams);
			Query query = builder.createQuery(pGroup, session);
			if(query!=null) {
				SearchResult result = query.getResult();

				if(result!=null) {
					LOG.info("Inside Result {}",result.getExecutionTime());
					LOG.info(""+result.getHits().size());
					List<Hit> hits = result.getHits();
					Iterator<Hit> hitList = hits.iterator();
					while(hitList.hasNext()) {
						String path = hitList.next().getPath();
						fetchingPageTitle(path);
						LOG.info("Path list of the tags {}",path);
					}
				}
			}

		}catch (Exception e) {

			LOG.info(e.getMessage());

		}		

	}
	
	private void fetchingPageTitle(String path) {
		
		Page page = resolver.getResource(path).adaptTo(Page.class);
		pageList.add(page.getPath());
		
	}

	public List<String> getPageDetails() {
		return pageList;
	}

	/*public String getTagName() {
		return finalTagName;
	}*/
}
