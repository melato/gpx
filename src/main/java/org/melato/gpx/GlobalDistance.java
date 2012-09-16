package org.melato.gpx;


public class GlobalDistance implements Metric {

  private static double square(double x) {
    return x * x;
  }

  /**
   * Haversine formula
   * http://en.wikipedia.org/wiki/Great-circle_distance
   * */
  @Override
  public float distance(Point p1, Point p2) {
    double lat1 = Math.toRadians(p1.lat);
    double lat2 = Math.toRadians(p2.lat);
    double lon1 = Math.toRadians(p1.lon);
    double lon2 = Math.toRadians(p2.lon);
    double d = square(Math.sin((lat1-lat2)/2)) + Math.cos(lat1)*Math.cos(lat2)*square(Math.sin((lon1-lon2)/2));
    d = 2 * Math.asin(Math.sqrt(d));
    /*
    double d = Math.sin(lat1)*Math.sin(lat2) +
        Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2);
    double d = Math.acos(d);
     */
    return (float) (d * Earth.CIRCUMFERENCE / (2 * Math.PI));
  }

}
