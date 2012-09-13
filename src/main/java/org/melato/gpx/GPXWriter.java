package org.melato.gpx;

import java.io.File;
import java.io.IOException;

import org.melato.xml.XMLWriter;

/** Writes gpx data to gpx (XML) files. */
public class GPXWriter {
	public void write( GPX gpx, File file ) throws IOException {
		XMLWriter xml = new XMLWriter( file );
		try {
  		xml.printHeader();
      GPXSerializer writer = new GPXXmlWriter(xml);
      writer.addGPX(gpx);
		} finally {
		  xml.close();
		}
	}

}
