package org.melato.gpx;

import org.melato.gps.Point;


public interface Metric {
  float distance(Point p1, Point p2);
}
