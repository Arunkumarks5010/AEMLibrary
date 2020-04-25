package com.aem.community.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Level1 {

	@Inject
	private String level1Name;
	
	@Inject
	private List<Level2> level2Nodes;
	
	

	public String getLevel1Name() {
		return level1Name;
	}

	public List<Level2> getLevel2Nodes() {
		return level2Nodes;
	}
	
	
	
}
