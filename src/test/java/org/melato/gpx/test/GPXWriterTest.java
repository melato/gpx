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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.melato.gpx.GPX;
import org.melato.gpx.GPXParser;
import org.melato.gpx.GPXWriter;
import org.melato.gpx.KeyValue;
import org.melato.gpx.Route;
import org.melato.gpx.Sequence;
import org.melato.gpx.Waypoint;

public class GPXWriterTest {
   
   public @Test void write() throws IOException {
     InputStream input = getClass().getResourceAsStream("test.gpx");
     GPXParser parser = new GPXParser();
     GPX gpx = parser.parse(input);
     GPXWriter writer = new GPXWriter();
     OutputStream out = new ByteArrayOutputStream();
     writer.write(gpx, out);
     String s = out.toString();
     System.out.println(s);
   }
   
   public @Test void writeRouteExtensions() throws IOException {
     GPX gpx = new GPX();
     Route route = new Route();
     List<Waypoint> waypoints = Collections.emptyList();
     route.path = new Sequence(waypoints);
     route.setExtensions(new KeyValue[] {
         new KeyValue("a", "1"),
         new KeyValue("b", "2"),
         });
     gpx.getRoutes().add(route);
     ByteArrayOutputStream buf = new ByteArrayOutputStream();
     GPXWriter writer = new GPXWriter();
     writer.write(gpx, buf);
     System.out.println(buf);
     GPXParser parser = new GPXParser();
     gpx = parser.parse(new ByteArrayInputStream(buf.toByteArray()));     
     Assert.assertEquals( 1, gpx.getRoutes().size());
     route = gpx.getRoutes().get(0);
     KeyValue[] extensions = route.getExtensions();
     Assert.assertEquals( 2, extensions.length);
     KeyValue kv = extensions[0];
     Assert.assertEquals("a", kv.getKey());
     Assert.assertEquals("1", kv.getValue());
   }
}
