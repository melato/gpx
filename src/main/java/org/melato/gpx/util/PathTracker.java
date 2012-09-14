package org.melato.gpx.util;

import org.melato.gpx.Point;

/**
 * Matches actual track locations to a fixed path.
 * Classes that implement this interface compute the location of a current point on a path (route) of waypoints.
 * The computation is approximate, since the incoming locations may follow a discontinuous crazy path.
 * A practical computation can assume a smooth track, such as that followed by a city bus with frequent stops,
 * or an intercity bus with fewer stops.
 * The computationj must take into consideration:
 * <ul>
 * <li>paths that are circular (the first waypoint is the same as the last waypoint)
 * <li>paths that cross themselves
 * <li>Hairpin paths: paths that go somewhere and return in the reverse direction
 * <li>Paths that have a duplicated subsection (For example a figure-8 path with two stops at the junction)
 * </ul>
 * @author Alex Athanasopoulos
 *
 */
public interface PathTracker {

  void setPath(Path path);

  void clearLocation();
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