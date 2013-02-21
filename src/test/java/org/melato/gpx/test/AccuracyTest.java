/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013, Alex Athanasopoulos.  All Rights Reserved.
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
package org.melato.gpx.test;

import junit.framework.Assert;

import org.junit.Test;
import org.melato.gps.Earth;
import org.melato.gps.Point2D;

/** Verify that we're using adequate data representations for accurate computations.
 */
public class AccuracyTest {
	
	/** Verify that a float has plenty of accuracy for storing latitude. */
	public @Test void floatLatLatitude() {
		float lat = 89;  // pick a latitude, near the pole
		float lon = 0f;
		// find the next float
		float d = 1;
		for(int i = 0; i < 100; i++ ) {
			float lat1 = lat + d;
			if ( lat1 == lat )
				break;
			d = d / 2;
		}
		d = d * 2;
		float lat2 = lat + d;
		// lat2 is the smallest float that is still greater than lat.
		Point2D p1 = new Point2D(lat, lon);
		Point2D p2 = new Point2D(lat2, lon);
		float minDistance = (float) Earth.distance(p1, p2);
		// the minimum distance between two different waypoints, using float accuracy.
		System.out.println( "minDistance: " + minDistance );
		// It's actually 0.848643
		Assert.assertTrue( minDistance < 1f );
		// We can compute distances less than one meter, using float latitude.
		// We could also test for longitude, but I'm assuming it would be similar
		// (perhaps 2 millimeters because longitudes can be twice as large (180 degrees).
	}
	
}
