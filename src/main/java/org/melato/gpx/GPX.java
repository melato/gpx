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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A GPX entity has:
 * A list of Routes.
 * A list of Tracks.
 * A list of Waypoints.
 */
public class GPX {
	public static final String GPX = "gpx";

	public static final String NAME = "name";
	public static final String DESC = "desc";
  public static final String SYM = "sym";
	public static final String LINK = "link";
	public static final String TYPE = "type";
  public static final String TEXT = "text";

	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String ELE = "ele";
	public static final String TIME = "time";
  public static final String SPEED = "gpx10:speed";
  public static final String COURSE = "gpx10:course";

	public static final String WPT = "wpt";	
	public static final String RTE = "rte";
	public static final String RTEPT = "rtept";
	public static final String TRK = "trk";
	public static final String TRKSEG = "trkseg";
	public static final String TRKPT = "trkpt";
	public static final String EXTENSIONS = "extensions";
    public static final String METADATA = "metadata";
	
	private String name;
    private String desc;
    private Extensions extensions = new Extensions();
	List<Route> routes = new ArrayList<Route>();
	List<Track> tracks = new ArrayList<Track>();
	List<Waypoint> waypoints = new ArrayList<Waypoint>();

	public List<Route> getRoutes() {
		return routes;
	}	
	
	public List<Track> getTracks() {
		return tracks;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	
	public void setWaypoints(List<Waypoint> waypoints) {
    this.waypoints = waypoints;
  }

  /**
	 * Get an iterator of all paths, including routes and tracks.
	 */
	public Iterable<Sequence> getPaths() {
		List<Sequence> paths = new ArrayList<Sequence>();
		for( Route route: getRoutes() ) {
			paths.add( route.path );			
		}
		for( Track track: getTracks() ) {
			for( Sequence path: track.getSegments() ) {
				paths.add( path );
			}
		}
		return paths;		
	}

  /** Return the total length, in meters. */
	public float length() {
		float length = 0;
		for( Sequence path: getPaths() ) {
			length += path.distance();
		}
		return length;
	}
	
  /** Return the duration in seconds */
	public int duration() {
		int duration = 0;
		for( Sequence path: getPaths() ) {
			duration += path.duration();
		}
		return duration;
	}

	public int maxSpeed() {
		int duration = 0;
		for( Sequence path: getPaths() ) {
			duration += path.duration();
		}
		return duration;
	}
	
	public Date startTime() {
		for( Sequence path: getPaths() ) {
			Date start = path.startTime();
			if ( start != null )
				return start;
		}
		return null;
	}
	
	public Waypoint startPoint() {
    for( Sequence path: getPaths() ) {
      List<Waypoint> points = path.getWaypoints();
      if ( points.size() > 0 )
        return points.get(0);
    }
    return null;
	}
	
  public static GPX read(File file) throws IOException {
    GPXParser parser = new GPXParser();
    GPX gpx = parser.parse(file);
    return gpx;
  }
  
  /** Merge a gpx file into this one. */
  public void merge(GPX gpx) {
    // merge waypoints
    List<Waypoint> waypoints = getWaypoints();
    for( Waypoint p: gpx.getWaypoints() ) {
      waypoints.add(p);
    }
    
    // merge routes
    for( Route route: gpx.getRoutes() ) {
      getRoutes().add(route.clone());      
    }
    
    // merge Tracks
    for( Track track: gpx.getTracks() ) {
      getTracks().add(track.clone());
    }
    
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Extensions getExtensions() {
    return extensions;
  }

  public void setExtensions(Extensions extensions) {
    this.extensions = extensions;
  }
}
