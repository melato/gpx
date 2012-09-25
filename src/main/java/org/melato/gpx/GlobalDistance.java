package org.melato.gpx;

import org.melato.gps.Earth;
import org.melato.gps.Point;


public class GlobalDistance implements Metric {

  @Override
  public float distance(Point p1, Point p2) {
    return Earth.distance(p1, p2);
  }

}
