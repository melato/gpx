package org.melato.gpx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A sequence of waypoints.  May be used for track segments, routes, etc.
 */
public class Sequence implements Cloneable {
	List<Waypoint> waypoints;
	
	
	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public Sequence() {
		waypoints = new ArrayList<Waypoint>();
	}
	
	public Sequence(List<Waypoint> waypoints) {
		super();
		this.waypoints = waypoints;
	}
	
	public static float distance( List<Waypoint> waypoints ) {
    double length = 0;
    Waypoint p0 = null;
    for( Waypoint p: waypoints ) {
      if ( p0 != null ) {
        length += Earth.distance(p0,  p );
      }
      p0 = p;
    }
    return (float) length;	  
	}
	
	
	/** Return the length of the sequence, in meters. */
	public float distance() {
	  return distance( waypoints );
	}
	
	/** Return the duration in seconds */
	public static int duration(List<Waypoint> waypoints) {
		if ( waypoints.size() < 2 )
			return 0;
		Waypoint p1 = waypoints.get(0);
		Waypoint p2 = waypoints.get(waypoints.size()-1);
		if ( p1.time == null || p2.time == null )
			return 0;
		return (int) (p2.time.getTime()-p1.time.getTime())/1000;
	}
	
	public Date startTime() {
		if ( waypoints.size() == 0 )
			return null;
		return waypoints.get(0).time;		
	}

	/** Return the duration in seconds */
  public int duration() {
    return duration( waypoints );
  }
	/**
	 * Return true if the route goes away from the center.
	 * A route is outgoing if the distance between the center and the end of the route
	 * is greater than the distance between the center and the start of the route.
	 * @param center
	 * @return
	 */
	public boolean isOutgoing(Point center) {
		int n = waypoints.size();
		if ( n == 0 )
			return false;
		return Earth.distance(center,  waypoints.get(n-1)) > Earth.distance(center,  waypoints.get(0));
	}
	
	public void reverse() {
	  Collections.reverse(waypoints);
	}

  @Override
  public Sequence clone() throws CloneNotSupportedException {
    List<Waypoint> list = new ArrayList<Waypoint>(waypoints.size());
    list.addAll(waypoints);
    return new Sequence(list);
  }
	
	
}
