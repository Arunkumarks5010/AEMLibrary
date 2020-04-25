package com.aemlibrary.com.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service=Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION+ "=This is used to fetch the Tags List based on input",
		"sling.servlet.resourceType="+"/weretail/tags/list",		
})
public class TagListDataSource extends SlingSafeMethodsServlet{

	private static final Logger LOG = LoggerFactory.getLogger(TagListDataSource.class);
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOG.info("Inside do GET method of class {} ", this.getClass().getName());
		
		
		try {
			response.getWriter().print("Hey sending resoonse from servlet "+this.getClass().getName());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
}
