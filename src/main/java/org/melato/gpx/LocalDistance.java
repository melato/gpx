package org.melato.gpx;

/** A faster distance computation that is accurate near a particular reference point.
 * It is 6 times faster than GlobalDistance
 * A rough measurement shows it to be accurate within about 10 cm for distances ~ 10 Km for latitude 38.
 * @author Alex Athanasopoulos
 */
public class LocalDistance implements Metric {
  private float latScale;
  private float lonScale;
  
  public LocalDistance(Point reference) {
    latScale = Earth.metersPerDegreeLatitude();
    lonScale = Earth.metersPerDegreeLongitude(reference.getLat());
  }

  @Override
  public float distance(Point p1, Point p2) {
    float x = (p2.getLon() - p1.getLon()) * lonScale;
    float y = (p2.getLat() - p1.getLat()) * latScale;    
    return (float) Math.sqrt(x*x + y*y);
  }

}
