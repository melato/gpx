package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.Earth;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Represents a path consisting of a sequence of waypoints.
 */
public class Path {
  protected Waypoint[] waypoints;
  protected float[]   lengths;
  
  public Path() {
    setWaypoints( new Waypoint[0] );
  }
  
  public Path(List<Waypoint> waypoints) {
    setWaypoints(waypoints);
  }
  
  public Path(Waypoint[] waypoints) {
    setWaypoints(waypoints);
  }
  
  public void setPath(Path path) {
    this.waypoints = path.waypoints;
    this.lengths = path.lengths;
  }
  public void setWaypoints(Waypoint[] waypoints) {
    this.waypoints = waypoints;
    double distance = 0;
    lengths = new float[waypoints.length];
    if ( lengths.length > 0 ) {
      Waypoint p0 = waypoints[0];
      for( int i = 1; i < lengths.length; i++ ) {
        Waypoint p = waypoints[i];
        distance += Earth.distance(p0, p);
        lengths[i] = (float) distance;
        p0 = p;
      }    
    }
  }
  

  public void setWaypoints(List<Waypoint> waypoints) {
    setWaypoints(waypoints.toArray(new Waypoint[waypoints.size()]));
  }
  
  
  public Waypoint[] getWaypoints() {
    return waypoints;
  }

  public float[] getLengths() {
    return lengths;
  }
  /**
   * Return the length of the sequence path between s[0] and s[index].
   * @param index
   * @return length in meters.
   */
  public float getLength(int index) {
    return lengths[index];
  }
  
  /**
   * Return the length of the sequence.
   * @return length in meters.
   */
  public float getLength() {
    if ( lengths.length == 0 )
      return 0;
    return lengths[lengths.length-1];
  }

  public int findNearestIndex(Point p) {
    float minDistance = 0;
    int minIndex = -1;
    for( int i = 0; i < waypoints.length; i++ ) {
      float d = Earth.distance(p,  waypoints[i]);
      if ( minIndex < 0 || d < minDistance ) {
        minDistance = d;
        minIndex = i;
      }
    }
    return minIndex;
  }
  
}
