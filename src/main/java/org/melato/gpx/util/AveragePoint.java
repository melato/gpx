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

import java.util.List;

import org.melato.gps.Point;
import org.melato.gpx.GPX;
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

