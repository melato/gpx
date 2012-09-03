package org.melato.gpx.util;

import java.util.AbstractList;
import java.util.List;

import org.melato.gpx.Earth;
import org.melato.gpx.Waypoint;

/**
 * Represents a path consisting of a sequence of waypoints, and does relevant computations.
 */
public class Path {
  protected SequencePoint[] points;
  /**
   * For each point in the sequence, maintain the path of the sequence up to that point.
   * This way we can easily compute path differences between two points.
   */
  protected static class SequencePoint {
    Waypoint point;
    float   distance;
    public SequencePoint(Waypoint point, float distance) {
      this.point = point;
      this.distance = distance;
    }    
  }

  public void setSequence(List<Waypoint> sequence) {
    points = new SequencePoint[sequence.size()];
    double distance = 0;
    Waypoint p0 = null;
    for( int i = 0; i < points.length; i++ ) {
      Waypoint p = sequence.get(i);
      if ( i > 0 )
        distance += Earth.distance(p0, p);
      points[i] = new SequencePoint(p, (float) distance);
      p0 = p;
    }
  }
  /**
   * Return the length of the sequence path between s[0] and s[index].
   * @param index
   * @return length in meters.
   */
  public float getPathLength(int index) {
    return points[index].distance;
  }
  
  /**
   * Return the length of the sequence.
   * @return length in meters.
   */
  public float getPathLength() {
    if ( points.length == 0 )
      return 0;
    return points[points.length-1].distance;
  }

  /**
   * Return the number of waypoints in the sequence.
   */
  public int size() {
    return points.length;
  }

  public int findWaypointIndex(Waypoint p) {
    for( int i = 0; i < points.length; i++ ) {
      if ( p.equals(points[i].point)) {
        return i;
      }
    }
    return -1;
  }
  
  /**
   * Return the list of waypoints (not the original list, but an equivalent one).
   * @return
   */
  List<Waypoint> waypoints() {
    return new AbstractList<Waypoint>() {
      @Override
      public int size() {
        return points.length;
      }
      
      @Override
      public Waypoint get(int index) {
        return points[index].point;
      }      
    };
  }
}
