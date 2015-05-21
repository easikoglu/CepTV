package com.eatech.ceptv.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParseMediaUrl extends DefaultHandler {
	
	private String mediaUrl = "";

	public String parse(String uri) throws Exception {
		
		DefaultHandler handler = new ParseMediaUrl();

		SAXParserFactory factory = SAXParserFactory.newInstance();

		factory.setValidating(false);

		SAXParser parser = factory.newSAXParser();

		parser.parse(uri, handler);
		
		return ((ParseMediaUrl) handler).getMediaUrl();

	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, 
	        Attributes atts) throws SAXException {
		
	    if (localName.equals("media")) {
	    	mediaUrl = atts.getValue("url");	    	
	    }   
	}
	
	

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	} 
	
	
}
