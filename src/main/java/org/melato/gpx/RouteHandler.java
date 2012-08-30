package org.melato.gpx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;


class RouteHandler extends XMLMappingHandler {
	private XMLStringHandler nameHandler = new XMLStringHandler();
	private	 List<Route> routes = new ArrayList<Route>();
	private List<Waypoint> waypoints = new ArrayList<Waypoint>();
	private WaypointHandler waypointHandler = new WaypointHandler(waypoints);

	public RouteHandler() {
		setHandler( GPX.NAME, nameHandler );
		setHandler( GPX.RTEPT, waypointHandler);
	}
	
	@Override
	public void end() throws SAXException {
		Route route = new Route();
		route.name = nameHandler.getText();
		Waypoint[] array = waypoints.toArray( new Waypoint[0]);
		route.path = new Sequence(Arrays.asList(array));
		waypoints.clear();
		routes.add(route);
		
	}
	public List<Route> getRoutes() {
		return routes;
	}
}
