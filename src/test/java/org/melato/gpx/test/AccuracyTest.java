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

import org.junit.Assert;
import org.junit.Test;
import org.melato.gps.Earth;
import org.melato.gps.Point2D;

/** Verify that we're using adequate data representations for accurate computations.
 */
public class AccuracyTest {
	    
  static float nextFloat(float f) {
    // find the next float
    float d = 1;
    for (int i = 0; i < 100; i++) {
      float lat1 = f + d;
      if (lat1 == f)
        break;
      d = d / 2;
    }
    d = d * 2;
    return f + d;
  }
  void checkAccuracyLat(float lat) {
    float lon = 0f;
    float lat2 = nextFloat(lat);
    // lat2 is the smallest float that is still greater than lat.
    Point2D p1 = new Point2D(lat, lon);
    Point2D p2 = new Point2D(lat2, lon);
    float minDistance = (float) Earth.distance(p1, p2);
    // the minimum distance between two different points, using float
    // accuracy.
    System.out.println("lat=" + lat + " lat accuracy=" + minDistance);
    Assert.assertTrue(minDistance < 1f);
    // We can compute distances less than two meters, using float latitude.
    // We could also test for longitude, but I'm assuming it would be similar
    // (perhaps 2 millimeters because longitudes can be twice as large (180
    // degrees).
  }
  void checkAccuracyLon(float lat, float lon) {
    float lon2 = nextFloat(lon);
    Point2D p1 = new Point2D(lat, lon);
    Point2D p2 = new Point2D(lat, lon2);
    float minDistance = (float) Earth.distance(p1, p2);
    // the minimum distance between two different points, using float
    // accuracy.
    System.out.println("lat=" + lat + " lon=" + lon + " accuracy=" + minDistance);
    Assert.assertTrue(minDistance < 2f);
    // We can compute distances less than one meter, using float latitude.
    // We could also test for longitude, but I'm assuming it would be similar
    // (perhaps 2 millimeters because longitudes can be twice as large (180
    // degrees).
  }
  /** Verify that a float has plenty of accuracy for storing latitude. */
  public @Test void floatAccuracy() {
    checkAccuracyLat(89);
    checkAccuracyLat(38);
    checkAccuracyLon(89, 179);
    checkAccuracyLon(38, 179);
    checkAccuracyLon(38, 24);
    checkAccuracyLon(0, 179);
  }

}
