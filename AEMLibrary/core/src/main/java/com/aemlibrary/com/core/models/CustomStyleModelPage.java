package com.abbvie.abbviecom.core.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.day.cq.wcm.api.policies.ContentPolicyMapping;


@Model(adaptables = {Resource.class,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomStyleModelPage {
	private static final Logger LOG = LoggerFactory.getLogger(CustomStyleModelPage.class);
	
	@Inject
	private Page currentPage;
	
	@OSGiService
	private QueryBuilder builder;
	
	@Self
	private SlingHttpServletRequest request;
	
	private ResourceResolver resourceResolver;
	
	private String customCssScript;

	public String getCustomCssScript() {
		return customCssScript;
	}
	
	@PostConstruct
	void init() {

		LOG.info("Sling Model  {}","CustomStyleModelPage");
		String pageContentPath =  Optional.ofNullable(currentPage).map(page -> page.getContentResource()).map(Resource::getPath).orElse("");
		SearchResult result = getComponentsInThePage(pageContentPath);
		getComponentPolicies(result);
	}
	
	/**
	 * To List all the components used in the Page
	 * @param path
	 */
	private SearchResult getComponentsInThePage(String path) {
		LOG.info("Sling Model  method name {}","CustomStyleModelPage :: ComponentsInThePage");
		resourceResolver = request.getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);
		Map<String,String> queryMap = new HashMap<>();
		queryMap.put("path",path);
		queryMap.put("type",JcrConstants.NT_UNSTRUCTURED);
		queryMap.put("property",JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
		queryMap.put("property.operation","exists");	
		Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
		SearchResult result = query.getResult();		
		return result;
	}
	
	private void getComponentPolicies(SearchResult result) {
		LOG.info("Sling Model  method name {}","CustomStyleModelPage :: Componentspolicies");
		StringBuilder customStyles = new StringBuilder();
		ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
		result.getResources().forEachRemaining(resource -> {
			LOG.info("Resource Path {}",resource.getPath());
			ContentPolicyMapping policyMapping = policyManager.getPolicyMapping(resource);
			String customStyle = policyMapping.getPolicy().getProperties().get("cq:styleCustomCssScript",String.class);
			if(Objects.nonNull(customStyle)) {
				customStyles.append(customStyle);
			}
		});
		LOG.info("Policy Map policy value {}",customStyles);

	}
	

}
