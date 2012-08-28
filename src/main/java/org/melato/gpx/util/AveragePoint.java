package org.melato.gpx.util;

import java.util.List;

import org.melato.gpx.Point;
import org.melato.gpx.Sequence;
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
}

