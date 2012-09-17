package org.melato.gpx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.melato.gpx.GlobalDistance;
import org.melato.gpx.Metric;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Given a sequence of waypoints S and a target proximity distance D,
 * this class finds whether a given query waypoint Q is near the sequence or not.
 * It can also find the points of S that are (locally) closest to Q,
 * always within D.
 * 
 * It uses a form binary search to find local minimums,
 * It is therefore more efficient than linear search.
 * For a single query, the class is actually less efficient than linear search,
 * because it also computes the sequence path length, but
 * the path length is usually needed anyway.
 */
public class ProximityFinder {
  private Metric metric = new GlobalDistance();
  private float target = 0;
  private float[] lengths;
  private Waypoint[] waypoints;
  public void setTargetDistance( float d ) {
    target = d;
  }
  
  
  public Metric getMetric() {
    return metric;
  }


  public void setPath(Path path) {
    waypoints = path.getWaypoints();
    lengths = path.getLengths();
  }
  
  public void setWaypoints(List<Waypoint> waypoints) {
    setPath(new Path(waypoints));
  }

  public Waypoint[] getWaypoints() {
    return waypoints;
  }

  /**
   * Determine if the query waypoint q, is near the subsequence between indexes i1, i2 (inclusive).
   * It uses binary search and recursion to split the susbsequence in two, etc. 
   * @param q
   * @param i1
   * @param i2
   * @return
   */
  private boolean isNear(Waypoint q, int i1, int i2) {
    float d1 = metric.distance(q,  waypoints[i1]);
    if ( d1 <= target )
      return true;
    float d2 = metric.distance(q,  waypoints[i2]);
    if ( d2 <= target )
      return true;
    float diff = Math.abs(lengths[i1] - lengths[i2]);
    if ( d1 - diff > target )
      return false;
    if ( d2 - diff > target )
      return false;
    int di = i2 - i1;
    if ( di <= 1 ) {
      // there are no other points in the segment.
      return false;
    }
    if ( di == 2 ) {
      // there is only one other point in the segment.  Try it.
      return metric.distance(q,  waypoints[i1+1]) <= target;
    }      
    // subdivide the segment in two and try each one.
    int j1 = i1 + di / 2;
    int j2 = j1 + 1;
    return isNear( q, i1, j1) || isNear( q, j2, i2 );
  }
  
  /**
   * Determine if the given waypoint is within the target distance from any point in the sequence.
   * @param q
   * @return true if the waypoint is near the sequence, otherwise false.
   */
  public boolean isNear( Waypoint q ) {
    /**
     * The algorithm:
     * Do a binary search on the sequence points.
     * For each segment s, determine if q can be close to s, or cannot possibly be close to s,
     * based on the distance from q to each end of s (s1, s2) and from the path distance between s1, s2.
     * q can be near s, if distance(q,s1) - path(s1,s2) < target
     * and distance(q,s2) - path(s1,s2) <= target
     * if q can be near s, split s in two pieces, and try each piece again.
     * if q cannot be near s, don't look further inside s.
     * if distance(q,s1) <= target then q is near s1.
     * if distance(q,s2) <= target then q is near s2.
     */
    if ( waypoints.length == 0 )
      return false;
    return isNear( q, 0, waypoints.length - 1 );
  }

  /**
   * Helper class that specifies a subsequence of the sequence.
   * It is used both as an argument and as a return value.
   * */
  private static class Segment {
    /* The first index of the segment. */
    int first;
    /* The last index of the segment. */
    int last;
    /* The index of the closest point. */
    int closest;
        
    /** Construct an empty segment */
    public Segment() {
      this(-1, -2);
    }

    public Segment(int first, int last) {
      this.first = first;
      this.last = last;
      closest = -1;
    }

    boolean contains(int index) {
      return first <= index && index <= last;
    }
    
    boolean isEmpty() {
      return first > last;
    }
    
    int size() {
      if ( first <= last )
        return last - first;
      return 0;
    }
    
    public String toString() {
      String s = "[" + first + "," + last + "]";
      if ( closest >= 0 )
        s += " closest=" + closest;
      return s;
    }
  }
  
  /**
   * Returns a contiguous subsequence of S that has points that are all
   * within the target distance from q, and contains the specified index.
   * Also return the closest point in the result.
   * If the specified index is not within the target distance, return an empty sequence. 
   * @param q
   * @param index
   * @return
   */
  private Segment findNearbySegment(Point q, int index) {
    Segment s = new Segment();
    float minDistance = metric.distance(q, waypoints[index]);
    if ( minDistance > target )
      return s;
    s.first = s.last = s.closest = index;
    // search forward
    for( int i = index + 1; i < waypoints.length; i++ ) {
      float d = metric.distance(q, waypoints[i]);
      if ( d > target ) {
        break;
      }
      s.last = i;
      if ( d < minDistance ) {
        s.closest = i;
        minDistance = d;
      }
    }    
    // search backward
    for( int i = index - 1; i >= 0; i-- ) {
      float d = metric.distance(q, waypoints[i]);
      if ( d > target ) {
        break;
      }
      s.first = i;
      if ( d < minDistance ) {
        s.closest = i;
        minDistance = d;
      }
    }    
    // if ( ! s.isEmpty() ) System.out.println( "nearbySegment: " + s );
    return s;
  }

  /** similar algorithm with isNear(), except find the nearest point within each
   *  set of contiguous points that are near the query.
   *  If a point is found, extend the ends of the segment to contain all contiguous points near the query.
   *  This may return indexes that are beyond the specified segment, because it looks for the local minimum.
   * @param q  The query waypoint.
   * @param segment  Determines the bounds of the segment to search. 
   * @param nearby The output indexes for the local minima found.
   */
  private void findNearby(Point q, Segment segment, Collection<Integer> nearby) {
    //System.out.println( "findNearby " + segment );
    if ( segment.isEmpty() )
      return;

    // first check the whole segment for possible nearness.
    float d1 = metric.distance(q,  waypoints[segment.first]);
    float d2 = metric.distance(q,  waypoints[segment.last]);
    float distance = Math.abs(lengths[segment.last] - lengths[segment.first]);
    // If the whole segment is obviously too far, don't go any further.
    if ( d1 - distance > target )
      return;
    if ( d2 - distance > target )
      return;
    
    // check the endpoints for proximity
    Segment segment1 = findNearbySegment(q, segment.first);
    if ( ! segment1.isEmpty() ) {
      //System.out.println( "segment1=" + segment1 );
      nearby.add(segment1.closest);
      // extend to the left
      segment.first = segment1.first;
      if ( segment1.contains(segment.last)) {
        // The whole segment is nearby
        // extend to the right
        segment.last = segment1.last;
        return;
      }
    }
    
    Segment segment2 = findNearbySegment(q, segment.last);
    if ( ! segment2.isEmpty() ) {
      //System.out.println( "segment2=" + segment2 );
      nearby.add(segment2.closest);
      // extend to the right
      segment.last = segment2.last;
    }
    
    // avoid overlapping segments in order to avoid duplicates
    
    if ( ! segment1.isEmpty() && ! segment2.isEmpty() ) {
      // search the middle
      findNearby(q, new Segment( segment1.last + 1, segment2.first - 1), nearby );
      return;
    }
    
    if ( ! segment1.isEmpty() ) {
      // search to the right
      findNearby(q, new Segment( segment1.last + 1, segment.last - 1), nearby );
      return;
    }

    if ( ! segment2.isEmpty() ) {
      // search to the left
      findNearby(q, new Segment( segment.first + 1, segment2.first - 1), nearby );
      return;
    }
    
    // neither end point is near the query
    int size = segment.size();
    if ( size <= 2 ) {
      // there are no other points in the segment.
      return;
    }
    if ( size == 3 ) {
      // there is only one other point in the segment.  Try it.
      if ( metric.distance(q,  waypoints[segment.first+1]) <= target ) {
        nearby.add(segment.first+1);
        //System.out.println( "found " + segment.first+1 );
        return;
      }
    }      
    // subdivide the segment in two and try each one.
    segment1 = new Segment(segment.first + 1, (segment.first + segment.last) / 2 );
    findNearby(q, segment1, nearby );
    // start from the end of segment1, to make sure segment1 and segment2 do not find duplicates
    segment2 = new Segment( segment1.last + 1, segment.last - 1);
    findNearby(q, segment2, nearby );
    return;
  }

  /**
   * Like findNearbyIndexes, but puts the results into a collection provided by the caller.
   * @param q  The waypoint to query.
   * @param nearby An output collection of indexes.
   */
  /**
   * Find all points of S whose distances from q are within the target distance and are also local minima.
   * @param q  The waypoint to query.
   * @param nearby An output collection for the resulting indexes.
   */
  public void findNearby( Point q, Collection<Integer> nearby ) {
    findNearby(q, new Segment(0, waypoints.length - 1), nearby );
  }
  
  /**
   * Find all points of S whose distances from q are within the target distance and are also local minima.
   * @param q
   * @return A list of the indexes of the found points.
   */
  public List<Integer> findNearbyIndexes( Point q ) {
    List<Integer> nearby = new ArrayList<Integer>();
    findNearby(q, new Segment(0, waypoints.length - 1), nearby );
    return nearby;
  }
  
  /**
   * Find the closest point of S that has distance from q within the target distance.
   * @param q
   * @return The index of such point, or -1
   */
  public int findClosestNearby( Point q ) {
    List<Integer> nearby = findNearbyIndexes(q);
    int count = nearby.size();
    if ( count < 0 )
      return -1;
    float minDistance = 0;
    int minIndex = -1;
    for( int i = 0; i < count; i++ ) {
      int index = nearby.get(i);
      float d = metric.distance(q, waypoints[index]);
      if ( i == 0 || d < minDistance ) {
        minDistance = d;
        minIndex = index;
      }
    }
    return minIndex;
  }    
}
