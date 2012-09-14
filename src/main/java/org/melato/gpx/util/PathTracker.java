package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

public interface PathTracker {

  void setWaypoints(List<Waypoint> sequence);

  List<Waypoint> getWaypoints();

  /**
   * Return the length of the sequence path between s[0] and s[index].
   * @param index
   * @return length in meters.
   */
  float getSequenceLength(int index);

  /**
   * Return the length of the sequence.
   * @return length in meters.
   */
  float getSequenceLength();

  /////////////////////////////
  /////////////////////////////
  
  /**
   * Set the current location.
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
  float getPathPosition();

}