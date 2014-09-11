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
  
  /** Check the longitude accuracy at certain coordinates */ 
  void assertLonAccuracy(float lat, float lon, float error) {
    Point2D p1 = new Point2D(lat, lon);
    float lon2 = nextFloat(lon);
    Point2D p2 = new Point2D(lat, lon2);
    // the minimum distance between two different points at the same latitude, using float coordinates.
    float distance = (float) Earth.distance(p1, p2);
    Assert.assertTrue(distance < error);
    System.out.println("lat=" + lat + " lon=" + lon + " lonDiff=" + distance);
  }
  /** Check the latitude accuracy at certain coordinates */ 
  void assertLatAccuracy(float lat, float error) {
    Point2D p1 = new Point2D(lat, 0);
    float lat2 = nextFloat(lat);
    Point2D p2 = new Point2D(lat2, 0);
    // the minimum distance between two different points at the same longitude, using float coordinates.
    float distance = (float) Earth.distance(p1, p2);
    Assert.assertTrue(distance < error);
    System.out.println("lat=" + lat + " latDiff=" + distance);
  }
  /** Verify that a float has plenty of accuracy for storing latitude. */
  public @Test void floatAccuracy() {
    float e = 2;
    assertLatAccuracy(0, e);
    assertLatAccuracy(38, e);
    assertLatAccuracy(90, e);
    assertLonAccuracy(38, 180, e);
    assertLonAccuracy(38, 24, e);
    assertLonAccuracy(0, 180, e);
  }

}
