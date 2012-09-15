package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.Earth;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Simplistic stateless path tracking.
 * This is temporary, as it often gives incorrect results.
 * It handles mostly straight paths that don't cross themselves.
 */
public class SimplePathTracker implements TrackingAlgorithm {
  private Path path;
  private int  pathSize;

  private Point location;
  private int   nearestIndex;
  private float pathPosition;
    
  @Override
  public void clearLocation() {
    location = null;
    nearestIndex = -1;
    pathPosition = 0;
  }

  public SimplePathTracker() {
    super();
    setPath(new Path());
  }
  
  public SimplePathTracker(List<Waypoint> waypoints) {
    setPath(new Path(waypoints));
  }

  
  @Override
  public void setPath(Path path) {
    this.path = path;
    pathSize = path.getWaypoints().length;
  }

  @Override
  public int getNearestIndex() {
    return nearestIndex;
  }

  @Override
  public float getPosition() {
    return pathPosition;
  }


  /**
   * Find the two closest consecutive waypoints to a given point.
   * This is public for testing purposes in order to test the algorithm.
   * @param p
   * @return
   */
  public int[] find2Neighbors(Point p) {
    int[] neighbors = new int[2];
    int closest = path.findNearestIndex(p);
    if ( closest == 0 ) {
      neighbors[0] = closest;
      neighbors[1] = closest + 1;
    } else if ( closest == pathSize - 1 ) {
      neighbors[0] = closest - 1;
      neighbors[1] = closest;
    } else {
      // find the closest neighbor to the closest point.
      float d1 = Earth.distance(p,  path.getWaypoints()[closest-1]);
      float d2 = Earth.distance(p,  path.getWaypoints()[closest+1]);
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
  
  @Override
  public void setLocation(Point p) {
    this.location = p;
    nearestIndex = path.findNearestIndex(p);
    pathPosition = findPathLength();
  }

  /**
   * Determine the path length of a given point.
   * Use a simple heuristic that does not keep track of previous points
   * It is accurate on a straight path but has some small discontinuities when
   * there are corners.
   * Other possible algorithms:
   * Keep track of previous points and use the information.
   *   (need to determine when the point is no longer on the path).
   * Take into account 3-4 consecutive waypoints and create some sort of interpolation (cubic spline?)
   * to model the path. 
   * @param p
   * @return
   */
  private float findPathLength() {
    if ( pathSize < 2 )
      return Float.NaN;
    Point p = location;
    int[] neighbor = find2Neighbors(p);
    Waypoint[] waypoints = path.getWaypoints();
    Point s1 = waypoints[neighbor[0]];
    Point s2 = waypoints[neighbor[1]];
    float p1 = path.getLength(neighbor[0]);
    float p2 = path.getLength(neighbor[1]);
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
      if ( d1 < d2 )
        return p1 + s * d1 / (d1 + d2);
      else
        return p2 - s * d2 / (d1 + d2);
    }
    if ( d1 < d2 ) {
      // p is closest to p1.
      return p1 + d1; // or p1 - d1 
    }
    if( d2 < d1 ) {
      // p is closest to p2.
      return p2 - d2; // p2 + d2
    }
    return Float.NaN;
  }
}
