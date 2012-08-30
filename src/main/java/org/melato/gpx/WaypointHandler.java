package org.melato.gpx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.melato.xml.XMLTag;
import org.xml.sax.SAXException;

class WaypointHandler extends XMLMappingHandler {
	Collection<Waypoint> waypoints;
	XMLStringHandler nameHandler = new XMLStringHandler();
	XMLStringHandler descHandler = new XMLStringHandler();
  XMLStringHandler symHandler = new XMLStringHandler();
	XMLStringHandler typeHandler = new XMLStringHandler();
	XMLStringHandler eleHandler = new XMLStringHandler();
	XMLStringHandler timeHandler = new XMLStringHandler();
  XMLStringHandler speedHandler = new XMLStringHandler();
	List<String> links = new ArrayList<String>();
	XMLStringHandler linkHandler = new StringListHandler(links);
	Waypoint waypoint;
	
	public WaypointHandler(Collection<Waypoint> waypoints) {
		this.waypoints = waypoints;
		setHandler(GPX.NAME, nameHandler);
		setHandler(GPX.DESC, descHandler);
    setHandler(GPX.SYM, symHandler);
		setHandler(GPX.TYPE, typeHandler);
		setHandler(GPX.LINK, linkHandler);
		setHandler(GPX.ELE, eleHandler);
		setHandler(GPX.TIME, timeHandler);
		XMLMappingHandler extensions = new XMLMappingHandler();
		setHandler(GPX.EXTENSIONS, extensions );
		extensions.setHandler( GPX.SPEED, speedHandler );
	}

	private float getFloat( XMLStringHandler stringHandler ) {
		String s = stringHandler.getText();
		if ( s == null )
			return Float.NaN;
		s = s.trim();
		if ( s.length() == 0 )
			return Float.NaN;
		return Float.parseFloat(s);
	}

	
	@Override
	public void start(XMLTag tag) throws SAXException {
		float lat = getFloat(tag, GPX.LAT);
		float lon = getFloat(tag, GPX.LON);
		waypoint = new Waypoint(lat, lon);
		waypoints.add(waypoint);
	}

	@Override
	public void end() throws SAXException {
		waypoint.name = nameHandler.getText();
		waypoint.desc = descHandler.getText();
    waypoint.sym = symHandler.getText();
		waypoint.type = typeHandler.getText();
		waypoint.elevation = getFloat(eleHandler);
    waypoint.speed= getFloat(speedHandler);
		waypoint.time = GPX.parseDate(timeHandler.getText());
		if ( links.size() != 0 ) {
			waypoint.setLinks(Arrays.asList(links.toArray(new String[links.size()])));
		}
		links.clear();
		nameHandler.clear();
		descHandler.clear();
		typeHandler.clear();
		eleHandler.clear();
		timeHandler.clear();
	}

	static float getFloat(XMLTag tag, String attribute) {
		String s = tag.getAttribute(attribute);
		return Float.parseFloat(s);
	}

	static Waypoint getWaypoint(XMLTag tag) {
		float lat = getFloat(tag, GPX.LAT);
		float lon = getFloat(tag, GPX.LON);
		return new Waypoint(lat, lon);
	}
}
