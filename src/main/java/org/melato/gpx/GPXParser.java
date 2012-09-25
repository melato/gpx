package org.melato.gpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.melato.xml.XMLDelegator;
import org.melato.xml.XMLMappingHandler;
import org.xml.sax.SAXException;


/**
 * Read an xml gpx file.
 * @author alex
 *
 */
public class GPXParser {
   /**
   * Parse a GPX file and return the parsed GPX object.
   * @param file
   * @return
   * @throws IOException
   */
	public GPX parse( File file ) throws IOException {
		try {
			InputStream input = new FileInputStream( file );
			return parse(input);
		} catch( IOException e ) {
			throw exception( file, e );
		} catch( RuntimeException e ) {
      throw exception( file, e );
		}
	}
	
	private IOException exception( Object file, Exception e ) {
    return new IOException( "Cannot parse " + file + " exception=" + e );
	}
	/**
	 * Parse a file from a URL
	 * @param url
	 * @return
	 * @throws IOException
	 */
  public GPX parse( URL url ) throws IOException {
    try {
      InputStream input = url.openStream();
      return parse(input);
    } catch( IOException e ) {
      throw exception( url, e );
    } catch( RuntimeException e ) {
      throw exception( url, e );
    }
  }

  /**
   * Parse a GPX file (stream) and return the parsed GPX object.
   * @param input
   * @return
   * @throws IOException
   */
	public GPX parse( InputStream input) throws IOException {
		XMLMappingHandler rootHandler = new XMLMappingHandler();
		XMLMappingHandler gpxHandler = new XMLMappingHandler();
		rootHandler.setHandler( GPX.GPX, gpxHandler );
		RouteHandler routeHandler = new RouteHandler();
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		WaypointHandler waypointHandler = new WaypointHandler(waypoints);
		TrackHandler trackHandler = new TrackHandler();
		gpxHandler.setHandler( GPX.RTE, routeHandler );
		gpxHandler.setHandler( GPX.TRK, trackHandler );
		gpxHandler.setHandler( GPX.WPT, waypointHandler );
		try {
			XMLDelegator.parse( rootHandler, input );
      GPX gpx = new GPX();
			gpx.routes = routeHandler.getRoutes();
			gpx.tracks = trackHandler.getTracks();
			gpx.waypoints = waypoints;
			return gpx;
		} catch( SAXException e ) {
			throw new RuntimeException(e);
		} finally {
			input.close();
		}
	}
	
	/**
	 * Parse the waypoints from a GPX file and add them to the given collection.
   * This method can be used for a one-pass parsing of a large GPX file that does not store the result in memory,
   * if the user-provided collection does not store the items added to it.
   * Use to do a linear search from a large set of waypoints stored in a file. 
   * Only the plain waypoints are parsed without being stored in memory.
	 * It parses all three types of waypoints (waypoints, routes, track segments).
	 * @param input The file (stream) to parse
	 * @param result The collection to add the waypoints to.
	 * @throws IOException
	 */
  public void parseWaypoints( InputStream input, Collection<Waypoint> result) throws IOException {
    XMLMappingHandler rootHandler = new XMLMappingHandler();
    XMLMappingHandler gpxHandler = new XMLMappingHandler();
    rootHandler.setHandler( GPX.GPX, gpxHandler );
    RouteHandler routeHandler = new RouteHandler();
    WaypointHandler waypointHandler = new WaypointHandler(result);
    TrackHandler trackHandler = new TrackHandler();
    gpxHandler.setHandler( GPX.RTE, routeHandler );
    gpxHandler.setHandler( GPX.TRK, trackHandler );
    gpxHandler.setHandler( GPX.WPT, waypointHandler );
    try {
      XMLDelegator.parse( rootHandler, input );
      for( Route route: routeHandler.getRoutes() ) {
        for( Waypoint p: route.path.getWaypoints() ) {
          result.add(p);
        }
      }
      for( Track route: trackHandler.getTracks() ) {
        for( Sequence sequence: route.getSegments() ) {
          for( Waypoint p: sequence.getWaypoints() ) {
            result.add(p);
          }
        }
      }
    } catch( SAXException e ) {
      throw new RuntimeException(e);
    } finally {
      input.close();
    }
  }
	
}
