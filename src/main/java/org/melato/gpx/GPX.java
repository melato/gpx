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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	static TimeZone utc = TimeZone.getTimeZone( "GMT" );
	static Pattern datePattern = Pattern.compile( "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})T([0-9]{1,2}):([0-9]{2}):([0-9\\.]*)(Z?)");

  public static Date parseDate(String s) {
    long time = parseTime(s);
    if ( time != 0 )
      return new Date(time);
    return null;
  }
	/**
	 * Parse a GPX date (ISO 8601). 
	 * Example:  2011-09-25T10:17:37Z
	 * @param s
	 */
	public static long parseTime(String s) {
		if ( s == null )
			return 0;
		s = s.trim();
		if ( s.length() == 0 )
			return 0;
		Matcher matcher = datePattern.matcher(s);
		if ( matcher.matches() ) {
			int year = Integer.parseInt( matcher.group(1));
			int month = Integer.parseInt( matcher.group(2));
			int day = Integer.parseInt( matcher.group(3));
			int hour = Integer.parseInt( matcher.group(4));
			int minute = Integer.parseInt( matcher.group(5));
			float second = Float.parseFloat( matcher.group(6));
			int millisecond = Math.round(second*1000);
			boolean isUtc = "Z".equals( matcher.group(7));
			GregorianCalendar calendar = new GregorianCalendar(year,  month - 1, day, hour, minute);			
			if ( isUtc ) {
				calendar.setTimeZone(utc);
			}
			long time = calendar.getTimeInMillis();
			time += millisecond;
			return time;
		}
		throw new IllegalArgumentException( "Invalid date: " + s );
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
	
}
