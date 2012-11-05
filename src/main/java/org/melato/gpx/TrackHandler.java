/*-------------------------------------------------------------------------
 * Copyright (c) 2012, Alex Athanasopoulos.  All Rights Reserved.
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


class TrackHandler extends XMLMappingHandler {
	private XMLStringHandler nameHandler = new XMLStringHandler();
  private XMLStringHandler descHandler = new XMLStringHandler();
	private List<Track> tracks = new ArrayList<Track>();
	private List<Waypoint> waypoints = new ArrayList<Waypoint>();
	SegmentHandler segHandler = new SegmentHandler();

	public TrackHandler() {
		setHandler( GPX.NAME, nameHandler );
    setHandler( GPX.DESC, descHandler );
		setHandler( GPX.TRKSEG, segHandler );
	}
	
	class SegmentHandler extends XMLMappingHandler {
		WaypointHandler waypointHandler = new WaypointHandler(waypoints);
		List<Sequence> paths;
		public SegmentHandler() {
			setHandler( GPX.TRKPT, waypointHandler);
			clear();
		}
		void clear() {
			paths = new ArrayList<Sequence>();
		}
		@Override
		public void end() throws SAXException {
	    Waypoint[] array = waypoints.toArray( new Waypoint[0]);
			paths.add( new Sequence(Arrays.asList(array)));
			waypoints.clear();
		}		
	}
	
	@Override
	public void end() throws SAXException {
		Track track = new Track();
		track.setName(nameHandler.getText());
    track.setDesc(descHandler.getText());
		track.segments = segHandler.paths;
		segHandler.clear();
		tracks.add(track);
		
	}
	public List<Track> getTracks() {
		return tracks;
	}
}
