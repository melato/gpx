package org.melato.gpx;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.melato.xml.XMLWriter;

/** Writes gpx data to gpx (XML) files. */
public class GPXXmlWriter extends GPXSerializer {
	DateFormat format;
	XMLWriter xml;
	String waypointTag = GPX.WPT;
  protected int waypointCount = 0;


	public GPXXmlWriter(XMLWriter xml) {
		format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.xml = xml;
	}
	
	private String formatDate(Date date) {
		return format.format(date) + "Z";
	}
	
  @Override
  public int size() {
    return waypointCount;
  }    

  
	
	private void write( XMLWriter xml, String tag, String text ) {
		if ( text == null )
			return;
		xml.tagOpen(tag);
		xml.text(text);
		xml.tagEnd(tag );
		xml.println();
	}
	
  @Override
	public boolean add(Waypoint p ) {
	  waypointCount++;
		xml.tagOpen(waypointTag, false);
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
		for( String link: p.getLinks() ) {
		  write( xml, GPX.LINK, link );
		}
		xml.tagEnd(waypointTag);
		xml.println();
		return true;
	}

	@Override
  public void openGpx() {
    waypointCount = 0;
    xml.tagOpen(GPX.GPX, false);
    xml.tagAttribute( "version", "1.1");
    xml.tagAttribute( "creator", "melato.org - http://melato.org");
    xml.tagAttribute( "xmlns", "http://www.topografix.com/GPX/1/1");
    xml.tagAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    xml.tagAttribute( "xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
    xml.tagClose();
    xml.println();
	}

	@Override
  public void closeGpx() {
    xml.tagEnd(GPX.GPX);
    xml.println();
    
    xml.close();    
  }
	
  @Override
  public void openRoute(Route route) {
    xml.tagOpen(GPX.RTE);
    write(xml, GPX.NAME, route.getName()); 
    waypointTag = GPX.RTEPT;
  }
  
  @Override
  public void closeRoute() {
    xml.tagEnd(GPX.RTE);
    waypointTag = GPX.WPT;
  }
  
  @Override
  public void openTrack(Track track) {
    xml.tagOpen(GPX.TRK);
    write(xml, GPX.NAME, track.getName()); 
    write(xml, GPX.DESC, track.getDesc()); 
  }
  
  @Override
  public void closeTrack() {
    xml.tagEnd(GPX.TRK);
    waypointTag = GPX.WPT;
  }
  
  @Override
  public void openSegment() {
    xml.tagOpen(GPX.TRKSEG);
    waypointTag = GPX.TRKPT;
  }
  
  @Override
  public void closeSegment() {
    xml.tagEnd(GPX.TRKSEG);
    xml.println();
    waypointTag = GPX.WPT;
  }
  
  public void write( GPX gpx) throws IOException {
		try {
		  xml.printHeader();
		  super.addGPX(gpx);
		} finally {
			xml.close();
		}
	}

}
