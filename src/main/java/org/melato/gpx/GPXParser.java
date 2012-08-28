package org.melato.gpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.melato.xml.XMLDelegator;
import org.melato.xml.XMLMappingHandler;
import org.xml.sax.SAXException;


/**
 * Read an xml gpx file.
 * @author alex
 *
 */
public class GPXParser {
	public GPX parse( File file ) throws IOException {
		try {
			InputStream input = new FileInputStream( file );
			return parse(input);
		} catch( IOException e ) {
			throw new IOException( "Cannot parse " + file, e );
		} catch( RuntimeException e ) {
			throw new IOException( "Cannot parse " + file, e );
		}
	}
  public GPX parse( URL url ) throws IOException {
    try {
      InputStream input = url.openStream();
      return parse(input);
    } catch( IOException e ) {
      throw new IOException( "Cannot parse " + url, e );
    } catch( RuntimeException e ) {
      throw new IOException( "Cannot parse " + url, e );
    }
  }

	public GPX parse( InputStream input) throws IOException {
		XMLMappingHandler rootHandler = new XMLMappingHandler();
		XMLMappingHandler gpxHandler = new XMLMappingHandler();
		rootHandler.setHandler( GPX.GPX, gpxHandler );
		RouteHandler routeHandler = new RouteHandler();
		WaypointHandler waypointHandler = new WaypointHandler();
		TrackHandler trackHandler = new TrackHandler();
		gpxHandler.setHandler( GPX.RTE, routeHandler );
		gpxHandler.setHandler( GPX.TRK, trackHandler );
		gpxHandler.setHandler( GPX.WPT, waypointHandler );
		try {
			GPX gpx = new GPX();
			XMLDelegator.parse( rootHandler, input );
			gpx.routes = routeHandler.getRoutes();
			gpx.tracks = trackHandler.getTracks();
			gpx.waypoints = waypointHandler.getWaypoints();
			return gpx;
		} catch( SAXException e ) {
			throw new RuntimeException(e);
		} finally {
			input.close();
		}
	}
}
