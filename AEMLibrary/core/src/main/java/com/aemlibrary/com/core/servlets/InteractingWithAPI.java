package com.aemlibrary.com.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemlibrary.com.core.config.InteractingWithAPIConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

//import com.google.gson.JsonParser; -- deprecated code 

@Component(service = Servlet.class,property = {
		Constants.SERVICE_DESCRIPTION + "=Making API Request using GET Method",
		"sling.servlet.methods="+ HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes="+"wcm/foundation/components/page",
		"sling.servlet.extensions=" + "txt",
		"sling.servlet.selectors=" + "recepie"
})

@Designate(ocd = InteractingWithAPIConfig.class)
public class InteractingWithAPI extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(InteractingWithAPI.class);
	private String endPointURL ;


	@Activate
	public void activate(InteractingWithAPIConfig config) {
		endPointURL = config.url();
	}

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) { 

			JsonObject responseOutput = null ;
			String recepieName = request.getParameter("recepieName");
			String recepieID = request.getParameter("recepieID");			
			
			String url = !recepieName.isEmpty() ? newURLBasedOnReceipeOrID(recepieName) : !recepieID.isEmpty() ? newURLBasedOnReceipeOrID(recepieID) : "Please Pass the Parameter" ;
			
			
			
			LOG.info("Endpoint1"+url);
			LOG.info("Endpoint"+endPointURL);
			try {

				URL apiURL = new URL(url);
				HttpURLConnection con = (HttpURLConnection)apiURL.openConnection();
				con.setRequestMethod("GET");
				response.setContentType("application/json");

				if(con.getResponseCode() != 200) {

					responseOutput = buildJsonErrorResponse(con.getResponseCode(),con.getResponseMessage());
					
				}

				else {
					responseOutput = buildJsonResoponse(con);
				}

				LOG.info("Response from the servlet {}:", responseOutput.toString());
				response.getWriter().print(responseOutput);
				con.disconnect();
			} catch (IOException e) {
				LOG.info(e.getMessage());
				e.printStackTrace();
			}
		}


	private String newURLBasedOnReceipeOrID(String param) {

		String url  = endPointURL+param;

		return url;
	}
	
	private JsonObject buildJsonResoponse(HttpURLConnection con) throws IOException {
		String output = "";
		InputStreamReader io = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(io);
				
		StringBuilder jsonString = new StringBuilder();

		if((output = br.readLine()) !=null) {
			jsonString.append(output);
		}
		
		
		JsonObject jsonObject = new Gson().fromJson(jsonString.toString(), JsonObject.class);
		
		br.close();
		
		return jsonObject;
		
	}
	
	private JsonObject buildJsonErrorResponse(int responseCode,String responseMessage) {
		
		JsonObject obj = new JsonObject();
		obj.addProperty("responseCode",responseCode);
		obj.addProperty("Response Message", responseMessage);
		
		return obj;
	}
}