/*-------------------------------------------------------------------------
 * Copyright (c) 2012, Alex Athanasopoulos.  All Rights Reserved.
 * alex@melato.org
 *-------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *-------------------------------------------------------------------------
 */
package org.melato.gpx.util;

import java.util.Arrays;
import java.util.List;

import org.melato.gps.Point;
import org.melato.gpx.GlobalDistance;
import org.melato.gpx.Metric;
import org.melato.gpx.Waypoint;

/**
 * Represents a path consisting of a sequence of waypoints.
 * Computes path-distances between waypoints along this path.
 * The path-distance between points i, j is defined as the sum
 * if distances between each two adjacent waypoints from i to j.
 */
public class Path {
  private Metric metric = new GlobalDistance();
  protected Waypoint[] waypoints;
  protected float[]   lengths;
  
  public Path() {
    setWaypoints( new Waypoint[0] );
  }
  
  public Metric getMetric() {
    return metric;
  }

  public Path(Metric metric) {
    this.metric = metric;
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
        distance += metric.distance(p0, p);
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
      float d = metric.distance(p,  waypoints[i]);
      if ( minIndex < 0 || d < minDistance ) {
        minDistance = d;
        minIndex = i;
      }
    }
    return minIndex;
  }

  /**
   * Find the closest index from the sub-path [index1, index2], inclusive
   */
  public int findNearestIndex(Point point, int index1, int index2) {
    float minDistance = 0;
    int minIndex = -1;
    for( int index = index1; index <= index2; index++ ) {
      if ( 0 <= index && index < size() ) {
        float d = metric.distance(point,  getWaypoint(index));
        if ( d < minDistance ) {
          minIndex = index;
          minDistance = d;
        }
      }
    }
    return minIndex;
  }
  
  
  public int findNearestIndex(float position) {
    if ( lengths.length == 0 )
      return -1;
    if ( position < 0 )
      return 0;
    if ( position > lengths.length )
      return size() - 1;
    int pos = Arrays.binarySearch(lengths, position);
    if ( pos >= 0 ) {
      return pos;
    } else {
      pos = -(pos+1);
      // pos is the index where position would be inserted in the array to keep it sorted
      if ( pos == 0 )
        return pos;
      if ( pos >= lengths.length )
        return lengths.length - 1;
      float d1 = position - lengths[pos-1];
      float d2 = lengths[pos] - position;
      if ( d1 < d2 )
        return pos -1;
      return pos;
    }    
  }
  
}
