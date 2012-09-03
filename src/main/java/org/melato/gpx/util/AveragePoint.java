package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.GPX;
import org.melato.gpx.Point;
import org.melato.gpx.Route;
import org.melato.gpx.Sequence;
import org.melato.gpx.Track;
import org.melato.gpx.Waypoint;

public class AveragePoint {
	private int	pointCount = 0;
	private double latSum = 0;
	private double lonSum = 0;
	public void add(Point p) {
		pointCount++;
		latSum += p.lat;
		lonSum += p.lon;
	}
	public Point getCenter() {
		if ( pointCount == 0 )
			return null;
		float lat = (float) (latSum/pointCount);
		float lon = (float) (lonSum/pointCount);
		return new Point(lat, lon);
	}
  public void add(List<Waypoint> list) {
    for( Point p: list ) {
      add(p);
    }
  }
	public void add(Sequence seq) {
	  add( seq.getWaypoints() );
	}
	
	public static Point getCenter(GPX gpx) {
	  AveragePoint mean = new AveragePoint();
	  mean.add(gpx.getWaypoints());
    for( Route route: gpx.getRoutes() ) {
      mean.add(route.path.getWaypoints());
    }
    for( Track track: gpx.getTracks() ) {
      for( Sequence seq: track.getSegments() ) {
        mean.add( seq.getWaypoints());
      }
    }
    return mean.getCenter();
	}
}

