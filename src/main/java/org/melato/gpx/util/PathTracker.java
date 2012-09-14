package org.melato.gpx.util;

import org.melato.gpx.Point;

public interface PathTracker {

  void setPath(Path path);

  /**
   * Add a new location to the track, which becomes the current location.
   * The sequence of calls to setLocation() matters because it provides a series of past locations
   * that the path may use in its algorithm.
   * @param p
   */
  void setLocation(Point p);
  
  /**
   * Return the index of the waypoint nearest the current location, according to path distance.
   * @return
   */
  int getNearestIndex();

  /**
   * Return the path distance of the current location from the beginning of the path.
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
  float getPosition();

}