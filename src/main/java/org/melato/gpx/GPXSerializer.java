package org.melato.gpx;

import java.util.AbstractCollection;
import java.util.Iterator;

public abstract class GPXSerializer extends AbstractCollection<Waypoint> {
  @Override
  public Iterator<Waypoint> iterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    throw new UnsupportedOperationException();
  }    
  
  @Override
  public abstract boolean add(Waypoint p);
  
  public abstract void openGpx();

  public abstract void closeGpx();

  public abstract void openRoute(Route route);

  public abstract void closeRoute();

  public abstract void openTrack(Track track);

  public abstract void closeTrack();

  public abstract void openSegment();

  public abstract void closeSegment();

  public void addGPX( GPX gpx) {
    openGpx();
    addAll(gpx.getWaypoints() );
    for( Route route: gpx.getRoutes() ) {
      openRoute(route);
      addAll(route.path.getWaypoints());
      closeRoute();
    }
    for( Track track: gpx.getTracks() ) {
      openTrack(track);
      for( Sequence path: track.getSegments() ) {
        openSegment();
        addAll( path.getWaypoints());
        closeSegment();
      }
      closeTrack();
    }     
    closeGpx();
  }
}