package com.aemlibrary.com.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemlibrary.com.core.config.CreatePageServiceConfig;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;

@Component(immediate = true, service = CreatePageService.class)
@Designate(ocd= CreatePageServiceConfig.class)
public class CreatePageService {

	public static final Logger LOG = LoggerFactory.getLogger(CreatePageService.class);

	@Reference
	private ResourceResolverFactory resolverFactory;
	
	private ResourceResolver resolver;
	private Session session;


	private String pagePath;
	private String templatePath;
	private String pageTitle;
	private String pageName;


	public String getPageName() {
		return pageName;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	@Activate
	public void activate(CreatePageServiceConfig config) {
		LOG.info("Inside activate method of {}", this.getClass());
		pagePath = config.pagePath();
		templatePath = config.templatePath();

	}

	public void createPage() {
		LOG.info("Inside createPage - 1 of CreatePage service ");
		Map<String,Object> params = new HashMap<>();
		params.put(ResourceResolverFactory.SUBSERVICE, "aemlibrary");
		TreeSet<String> pages = null;
		try {
			resolver = resolverFactory.getServiceResourceResolver(params);
			LOG.info("Inside try block of createPage - 1 of CreatePage service ");
			if(resolver!=null) {
				Page page = resolver.getResource(pagePath).adaptTo(Page.class);	
				LOG.info("Inside if block 1 of createPage - 1 of CreatePage service ");
				if(page!=null) {
					LOG.info("Inside if block 2 of createPage - 1 of CreatePage service {} ",page.getName());
					Iterator<Page> childPage = page.listChildren(new PageFilter());					
					
						pages = new TreeSet<String>();
						while(childPage.hasNext()) {
							pages.add(childPage.next().getName());
						}
						
						if(!pages.isEmpty()) {
							LOG.info("Inside if block 3 of createPage - 1 of CreatePage service ");
							String lastPageName = pages.last();
							pageName = "arun"+nameOrTitleForPage(lastPageName);//arun0016
							pageTitle = "Arun "+nameOrTitleForPage(lastPageName);//Arun 0016
						}
					
					else {
						LOG.info("beginning");
						pageName = "arun0000";
						pageTitle = "Arun 0000";
					}
					
				}
				
				LOG.info("end of  createPage - 1 of CreatePage service ");
				createPage(pageName,pageTitle);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}

	}


	private void createPage(String pageName, String pageTitle) {
		LOG.info("Inside createPage - 2 of CreatePage service page Name {}, page Title {}", pageName,pageTitle);

		try {
			
				PageManager manager = resolver.adaptTo(PageManager.class);
				Page page = manager.create(pagePath, pageName, templatePath, pageTitle,true);				
				if(page!=null) {
					LOG.info("Page Name CreatePage service {} ", page.getTitle());
					Node contentNode = page.getContentResource().adaptTo(Node.class);
					if(contentNode!=null) {
						LOG.info("Content node CreatePage service {}", contentNode.getName());
						session = resolver.adaptTo(Session.class);
						contentNode.setProperty("AddedPropertyUsingPM", "Added Using Page Manager");
						contentNode.setProperty("AddedBy", session.getUserID());
						LOG.info("Added props successfully CreatePage service ");
						session.save();
					}

				}
			
		} catch (Exception e) {
			LOG.info("Exception message {}", e.getMessage());
;
		}finally {
			session.logout();
		}
	}
	
	private String nameOrTitleForPage(String name) {
		//String initialCharacters = name.substring(0, name.length()-4);
		String lastfourCharacter = name.substring(name.length()-4);	//0015
		int updatedNumber = Integer.parseInt(lastfourCharacter) + 1;//16 
        String updatedname = String.format("%04d", updatedNumber);// 0016
		return updatedname;
	}

}
