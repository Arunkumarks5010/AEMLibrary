package com.aem.community.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class NestedMultiField {
	
	@Inject
	private List<Level1> level1Nodes;

	public List<Level1> getLevel1Nodes() {
		return level1Nodes;
	}
	

}
