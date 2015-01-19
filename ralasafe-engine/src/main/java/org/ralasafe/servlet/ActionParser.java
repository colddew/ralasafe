package org.ralasafe.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ActionParser {
	private static final String URL_PATTERN="url-pattern";
	private static final String ACTION_NAME="action-name";
	private static final String ACTION_MAPPING="action-mapping";

	public Map parse( String path ) {		
		DocumentBuilderFactory fcty = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			DocumentBuilder builder = fcty.newDocumentBuilder();
			document = builder.parse( path );
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Element rootEmt = document.getDocumentElement();
		
		return parse( rootEmt );
	}

	private Map parse( Element rootEmt ) {
		Map result=new HashMap();
		
		NodeList nodeList=rootEmt.getElementsByTagName( ACTION_MAPPING );
		for( int i=0,size=nodeList.getLength(); i<size; i++ ) {
			Element item=(Element) nodeList.item( i );
			
			String actionName=item.getElementsByTagName( ACTION_NAME ).item( 0 ).getTextContent();
			String url=item.getElementsByTagName( URL_PATTERN ).item( 0 ).getTextContent();
			
			result.put( url, actionName );
		}		

		return result;
	}
}
