package org.melato.gpx;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.melato.xml.XMLWriter;

/** Writes gpx data to gpx (XML) files. */
public class GPXWriter {
	DateFormat format;

	public GPXWriter() {
		format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));		
	}
	
	private String formatDate(Date date) {
		return format.format(date) + "Z";
	}
	private void write( XMLWriter xml, String tag, String text ) {
		if ( text == null )
			return;
		xml.tagOpen(tag);
		xml.text(text);
		xml.tagEnd(tag );
		xml.println();
	}
	private void write(XMLWriter xml, String element, Waypoint p ) {
		xml.tagOpen(element, false);
		xml.tagAttribute( "lat", String.valueOf(p.getLat()) );
		xml.tagAttribute( "lon", String.valueOf(p.getLon()) );		
		xml.tagClose();
		if ( ! Float.isNaN( p.elevation ) ) {
			write( xml, GPX.ELE, String.valueOf(p.elevation));
		}
		write( xml, GPX.TYPE, p.getType() );
		write( xml, GPX.NAME, p.getName() );
		write( xml, GPX.DESC, p.getDesc() );
    write( xml, GPX.SYM, p.getSym() );
		if ( p.getTime() != null )
		  write( xml, GPX.TIME, formatDate(p.getTime()));
		xml.tagEnd(element);
		xml.println();
	}
	private void write(XMLWriter xml, String element, List<Waypoint> waypoints ) {
		for( Waypoint waypoint: waypoints ) {
			write( xml, element, waypoint );
		}
	}
	public void write( GPX gpx, File file ) throws IOException {
		XMLWriter xml = new XMLWriter( file );
		try {
			xml.printHeader();
			xml.tagOpen(GPX.GPX, false);
			xml.tagAttribute( "version", "1.1");
			xml.tagAttribute( "creator", "melato.org - http://melato.org");
		    xml.tagAttribute( "xmlns", "http://www.topografix.com/GPX/1/1");
		    xml.tagAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		    xml.tagAttribute( "xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
			xml.tagClose();
			xml.println();
			write( xml, GPX.WPT, gpx.getWaypoints() );
			for( Route route: gpx.getRoutes() ) {
				xml.tagOpen(GPX.RTE);
				write( xml, GPX.RTEPT, route.path.getWaypoints());
				xml.tagEnd(GPX.RTE);
				xml.println();
			}
			for( Track track: gpx.getTracks() ) {
				xml.tagOpen(GPX.TRK);
				for( Sequence path: track.getSegments() ) {
					xml.tagOpen(GPX.TRKSEG);
					write( xml, GPX.TRKPT, path.getWaypoints());
					xml.tagEnd(GPX.TRKSEG);
					xml.println();
				}
				xml.tagEnd(GPX.TRK);
			}			
			xml.tagEnd(GPX.GPX);
		} finally {
			xml.close();
		}
	}

}
