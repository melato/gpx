package org.melato.gpx;

import java.util.List;

import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;

public class StringListHandler extends XMLStringHandler {
	private List<String> list;

	public StringListHandler(List<String> list) {
		this.list = list;
	}
	
	@Override
	public void end() throws SAXException {
		super.end();
		list.add(getText());
	}
	

}
