package org.melato.gpx;

public class Earth {
	public static final float CIRCUMFERENCE = 40044000;

	private static double square(double x) {
		return x * x;
	}

	/**
	 * Compute the distance between two points in meters.  Does not use elevation.
	 * @return
	 */
	public static float distance( Point p1, Point p2 ) {
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
		return (float) (CIRCUMFERENCE * d / (2 * Math.PI));
	}
	
	/**
	 * Compute the speed between two GPS measurements, in metric units (m/s).
	 * @return
	 */
	public static float speed( Point p1, Point p2 ) {
		float time = Point.timeDifference(p1, p2);
		if ( time == 0 )
			return 0;
		return (float) (distance(p1, p2) / time );
	}
	
  /**
   * Compute the  bearing between two points in degrees.  Does not use elevation.
   * @return a number between +180 and -180 degrees.
   */
  public static float bearing( Point p1, Point p2 ) {
    double lat1 = Math.toRadians(p1.lat);
    double lat2 = Math.toRadians(p2.lat);
    double lon1 = Math.toRadians(p1.lon);
    double lon2 = Math.toRadians(p2.lon);
    double t = Math.atan2( Math.sin(lon2-lon1)*Math.cos(lat2),
        Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1));
    return (float) Math.toDegrees(t);
  }
  
	
	
}
