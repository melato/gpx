package org.melato.gpx.util;

import java.util.AbstractList;
import java.util.List;

import org.melato.gpx.Earth;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Represents a path consisting of a sequence of waypoints, and does relevant computations.
 */
public class Path {
  protected SequencePoint[] points;
  
  
  public Path() {
    super();
  }
  
  public Path(List<Waypoint> waypoints) {
    super();
    setSequence(waypoints);
  }

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

  private int findNearestIndex(Point p) {
    float minDistance = 0;
    int minIndex = -1;
    for( int i = 0; i < points.length; i++ ) {
      float d = Earth.distance(p,  points[i].point);
      if ( minIndex < 0 || d < minDistance ) {
        minDistance = d;
        minIndex = i;
      }
    }
    return minIndex;
  }
  
  private int[] find2Neighbors(Point p) {
    int[] neighbors = new int[2];
    int closest = findNearestIndex(p);
    if ( closest == 0 ) {
      neighbors[0] = closest;
      neighbors[1] = closest + 1;
    } else if ( closest == points.length - 1 ) {
      neighbors[0] = closest - 1;
      neighbors[1] = closest;
    } else {
      // find the closest neighbor to the closest point.
      float d1 = Earth.distance(p,  points[closest-1].point);
      float d2 = Earth.distance(p,  points[closest+1].point);
      if ( d1 <= d2 ) {
        neighbors[0] = closest - 1;
        neighbors[1] = closest;
      } else {
        neighbors[0] = closest;
        neighbors[1] = closest + 1;
      }
    }
    return neighbors;
  }
  
  public float getPathLength(Point p) {
    if ( points.length < 2 )
      return Float.NaN;
    int[] neighbor = find2Neighbors(p);
    Point s1 = points[neighbor[0]].point;
    Point s2 = points[neighbor[1]].point;
    float p1 = getPathLength(neighbor[0]);
    float p2 = getPathLength(neighbor[1]);
    float s = p2 - p1;
    float d1 = Earth.distance(p, s1);
    float d2 = Earth.distance(p, s2);
    //Log.info("s1=" + s1 + " p1=" + p1 + " d1=" + d1);
    //Log.info("s2=" + s2 + " p2=" + p2 + " d2=" + d2);
    /*
    - if d1 < s and d2 < s, q is between S1, S2.  P = p1 + s * d1 / (d1+d2)
    - if s < d1 < d2, q is beyond S2.  P = p2 + d2
    - if s < d2 < d1, q is before S1.  P = p1 - d1
     */
    if ( d1 < s && d2 < s ) {
      return p1 + s * d1 / (d1 + d2);
    }
    if ( d1 < d2 ) {
      return p2 + d2; 
    }
    if( d2 < d1 ) {
      return p1 - d1;
    }
    return Float.NaN;
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
