package org.melato.gpx;

import java.util.ArrayList;
import java.util.List;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;


class RouteHandler extends XMLMappingHandler {
	private XMLStringHandler nameHandler = new XMLStringHandler();
	private	List<Route> routes = new ArrayList<Route>();
	private WaypointHandler waypointHandler = new WaypointHandler();

	public RouteHandler() {
		setHandler( GPX.NAME, nameHandler );
		setHandler( GPX.RTEPT, waypointHandler);
	}
	
	@Override
	public void end() throws SAXException {
		Route route = new Route();
		route.name = nameHandler.getText();
		route.path = waypointHandler.getPath();
		waypointHandler.clear();
		routes.add(route);
		
	}
	public List<Route> getRoutes() {
		return routes;
	}
}
