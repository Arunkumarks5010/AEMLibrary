package com.aem.community.core.models;



import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Level2 {


	@Inject
	private String level2Name;

	@Inject
	private String level2Details;

	public String getLevel2Name() {
		return level2Name;
	}

	public String getLevel2Details() {
		return level2Details;
	}

}
