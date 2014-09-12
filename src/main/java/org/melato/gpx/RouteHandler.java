/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013, Alex Athanasopoulos.  All Rights Reserved.
 * alex@melato.org
 *-------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *-------------------------------------------------------------------------
 */
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
	private List<KeyValue> extensions = new ArrayList<KeyValue>();
    private KeyValueHandler extensionHandler = new KeyValueHandler(extensions);

	public RouteHandler() {
		setHandler( GPX.NAME, nameHandler );
		setHandler( GPX.RTEPT, waypointHandler);
		setHandler(GPX.EXTENSIONS, extensionHandler);
	}
	
	@Override
	public void end() throws SAXException {
		Route route = new Route();
		route.name = nameHandler.getText();
		Waypoint[] array = waypoints.toArray( new Waypoint[0]);
		route.path = new Sequence(Arrays.asList(array));
		waypoints.clear();
		route.getExtensions().setValues(extensions.toArray(new KeyValue[0]));
		extensions.clear();
		routes.add(route);
		
	}
	public List<Route> getRoutes() {
		return routes;
	}
}
