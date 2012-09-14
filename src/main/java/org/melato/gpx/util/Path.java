package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.Earth;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Represents a path consisting of a sequence of waypoints.
 * Computes path-distances between waypoints along this path.
 * The path-distance between points i, j is defined as the sum
 * if distances between each two adjacent waypoints from i to j.
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
  public int size() {
    return lengths.length;
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
  
  public Waypoint getWaypoint(int index) {
    return waypoints[index];
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
  
  /** Get the length of the path from point 1 to point 2
   * @param index1
   * @param index2
   * @return The path distance, positive if index1 < index2
   */
  public float getLength(int index1, int index2) {
    return lengths[index2] - lengths[index1];
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

  /**
   * Find the index of the path waypoint that is closest to p.
   * If there are more than one such indexes, return the smallest one.  
   * @param p
   * @return
   */
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
