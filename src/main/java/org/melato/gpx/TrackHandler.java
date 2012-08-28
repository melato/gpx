package org.melato.gpx;

import java.util.ArrayList;
import java.util.List;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;


class TrackHandler extends XMLMappingHandler {
	private XMLStringHandler nameHandler = new XMLStringHandler();
	private	List<Track> tracks = new ArrayList<Track>();
	SegmentHandler segHandler = new SegmentHandler();

	public TrackHandler() {
		setHandler( GPX.NAME, nameHandler );
		setHandler( GPX.TRKSEG, segHandler );
	}
	
	class SegmentHandler extends XMLMappingHandler {
		WaypointHandler waypointHandler = new WaypointHandler();
		List<Sequence> paths;
		public SegmentHandler() {
			setHandler( GPX.TRKPT, waypointHandler);
			clear();
		}
		void clear() {
			paths = new ArrayList<Sequence>();
		}
		@Override
		public void end() throws SAXException {
			paths.add( waypointHandler.getPath() );
			waypointHandler.clear();
		}		
	}
	
	@Override
	public void end() throws SAXException {
		Track track = new Track();
		track.name = nameHandler.getText();
		track.segments = segHandler.paths;
		segHandler.clear();
		tracks.add(track);
		
	}
	public List<Track> getTracks() {
		return tracks;
	}
}
