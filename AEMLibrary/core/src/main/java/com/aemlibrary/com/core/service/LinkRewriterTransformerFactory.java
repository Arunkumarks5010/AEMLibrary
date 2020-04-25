package com.aemlibrary.com.core.service;

import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.impl.LinkRewriterTransformer;

@Component(property = {"pipeline.type = {cdnrewriter,linktype}"}, service = {TransformerFactory.class})
public class LinkRewriterTransformerFactory implements TransformerFactory {

	private final Logger LOG = LoggerFactory.getLogger(LinkRewriterTransformerFactory.class);
	@Override
	public Transformer createTransformer() {
		LOG.debug("Creating Transformer Instance");
		return new LinkRewriterTransformer();
	}

}
