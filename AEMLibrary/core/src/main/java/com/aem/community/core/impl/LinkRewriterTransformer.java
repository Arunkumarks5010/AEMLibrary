package com.aem.community.core.impl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML;

import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.aemlibrary.com.core.config.LinkRewriterConfig;
@Component
@Designate( ocd = LinkRewriterConfig.class)
public class LinkRewriterTransformer implements Transformer {
	//editing
	private final Logger LOG = LoggerFactory.getLogger(LinkRewriterTransformer.class);
	
	private ContentHandler contentHandler;
	private String regex;
	private static final String SITE_PDF = "/content/AEM63App/en||fr/pdf" ;
	private Pattern sitePattern;
	
	//Activate Method
	@Activate
	public void activate(LinkRewriterConfig config) {
		LOG.debug("Inside activate method of {} ",getClass());
		LOG.debug("Inside activate method, config Regex {} ",config.regex());
		
		regex = config.regex();
	
		sitePattern = Pattern.compile(SITE_PDF);
		
		
	}
	
	@Deactivate
	public void deactivate() {
		LOG.debug("Inside deactivate method of {} ",getClass());
	}

	@Override
    public void setDocumentLocator(Locator locator) {
        contentHandler.setDocumentLocator(locator);
    }
 
    @Override
    public void startDocument() throws SAXException {
        contentHandler.startDocument();
    }
 
    @Override
    public void endDocument() throws SAXException {
        contentHandler.endDocument();
    }
 
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
    }
 
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
    }
 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    	
    	org.apache.cocoon.xml.sax.AttributesImpl attrs = new org.apache.cocoon.xml.sax.AttributesImpl(atts);
    	
    	String href = attrs.getValue(HTML.Attribute.HREF.toString());
    	
    	if(href!=null) {
    		for(int i=0;i<attrs.getLength();i++) {
    			if(href.equalsIgnoreCase(attrs.getQName(i))) {
    				String attributeValue = attrs.getValue(i);
    				LOG.debug("attributeValue value {} ",attributeValue);
    				Matcher siteMatcher = sitePattern.matcher(attributeValue);
    				if(siteMatcher.find()) {
    					attributeValue = attributeValue.replaceAll(regex, "");
    		    		LOG.debug("attributeValue value {} ",attributeValue);
    		    		
    		    		attrs.setValue(i, attributeValue);
    		    		
    		    	}
    				break;
    			}
    			
    			
    			
    		}
    	}
    	   
    	
    	contentHandler.startElement(uri, localName, qName, attrs);
    	
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        contentHandler.endElement(uri, localName, qName);
    }
 
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        contentHandler.characters(ch, start, length);
    }
 
    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        contentHandler.ignorableWhitespace(ch, start, length);
    }
 
    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        contentHandler.processingInstruction(target, data);
    }
 
    @Override
    public void skippedEntity(String name) throws SAXException {
        contentHandler.skippedEntity(name);
    }
 
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
    	LOG.debug("init");

    }
 
    @Override
    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }
 
 
}
