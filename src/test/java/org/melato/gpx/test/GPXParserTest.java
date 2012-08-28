package org.melato.gpx.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.melato.gpx.GPX;
import org.melato.gpx.GPXParser;
import org.melato.gpx.Route;
import org.melato.gpx.Track;
import org.melato.gpx.Waypoint;

public class GPXParserTest {
	public @Test void parse() throws IOException {
		InputStream input = getClass().getResourceAsStream("test.gpx");
		GPXParser parser = new GPXParser();
		GPX gpx = parser.parse(input);
		Assert.assertEquals( 2, gpx.getRoutes().size() );
		Route route = gpx.getRoutes().get(0);
		Assert.assertEquals( "Route1", route.name );
		List<Waypoint> waypoints = route.path.getWaypoints();
		Assert.assertEquals( 3, waypoints.size());
		Assert.assertEquals( 10.1f, waypoints.get(0).lat, 0.001f);
		waypoints = gpx.getWaypoints();
		Assert.assertEquals( 1, waypoints.size());
		Waypoint p = waypoints.get(0);
		Assert.assertEquals( 1.1f, p.lat, 0.001f);
		Assert.assertEquals( "P1", p.getName() );
		Assert.assertEquals( "link1", p.getLink() );
		
		List<Track> tracks = gpx.getTracks();
		Assert.assertEquals( 1, tracks.size());
		Track track = tracks.get(0);
		Assert.assertEquals( "Track1", track.name );
		
	}

	 public @Test void parseOpenGpsTracker() throws IOException {
	    InputStream input = getClass().getResourceAsStream("ogs.gpx");
	    GPXParser parser = new GPXParser();
	    GPX gpx = parser.parse(input);
	    Assert.assertEquals( 1, gpx.getTracks().size());
	    Track track = gpx.getTracks().get(0);
	    List<Waypoint> waypoints = track.getSegments().get(0).getWaypoints();
	    Waypoint p = waypoints.get(waypoints.size()-1);
	    Assert.assertEquals( 6.0f, p.getSpeed(), 0.001f);
	  }

}
