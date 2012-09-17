package org.melato.gpx;

import java.util.Date;

/** A point in GPX spacetime (latitude, longitude, elevation, time) */
public class Point {
	public float	lat;
	public float	lon;
	/** GPX uses the term elevation, rather than altitude. */
	public float elevation = Float.NaN;
	public Date		time;
	public float speed = Float.NaN;
	public float bearing = Float.NaN;
	
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	
	public float getElevation() {
		return elevation;
	}
  public void setElevation(float elevation) {
    this.elevation = elevation;
  }
  
  public void setAltitude(float elevation) {
    setElevation(elevation);
  }
  
  public float getAltitude() {
    return getElevation();
  }
	
	public float getSpeed() {
    return speed;
  }
  public void setSpeed(float speed) {
    this.speed = speed;
  }
    
  public float getBearing() {
    return bearing;
  }
  public void setBearing(float bearing) {
    this.bearing = bearing;
  }
  public Date getTime() {
		return time;
	}

	/** Return the time difference between two points, in seconds. */
	public static float timeDifference(Point p1, Point p2) {
		if ( p1.time == null || p2.time == null)
			return 0;
		return ((float) (p2.time.getTime() - p1.time.getTime())) / 1000;
	}
	
  /** Return the time difference between two points, in milliseconds. */
  public static long timeDifferenceMillis(Point p1, Point p2) {
    if ( p1.time == null || p2.time == null)
      return 0;
    return p2.time.getTime() - p1.time.getTime();
  }
  
  public void setTime(long time) {
    this.time = new Date(time);
  }
	public void setTime(Date time) {
		this.time = time;
	}
	public Point(){}
	public Point(float lat, float lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	public Point(float lat, float lon, float elevation, Date time) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.elevation = elevation;
		this.time = time;				
	}
	
	@Override
	public boolean equals(Object x) {
		if ( x instanceof Point ) {
			Point p = (Point) x;
			return lat == p.lat && lon == p.lon;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new Float(lat).hashCode() + new Float(lon).hashCode();		
	}
	
	@Override
  public String toString() {
    return lat + ";" + lon;
  }
		
}
