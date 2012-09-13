package org.melato.gpx;

import java.util.ArrayList;
import java.util.List;

public class GPXBuilder extends GPXSerializer {
  private GPX gpx = new GPX();
  private static enum State { DEFAULT, ROUTE, TRACK, SEGMENT };
  private State state = State.DEFAULT;
  
  private List<Waypoint> waypoints;
  private Route route;
  private Track track;

  private void assertState(State state) {
    if ( state != this.state ) {
      throw new IllegalStateException();
    }
  }
  @Override
  public boolean add(Waypoint p) {
    if ( state == State.TRACK ) {
      throw new IllegalStateException( "Track waypoints must be in segments." );
    }
    waypoints.add(p);
    return true;
  }
  @Override
  public void openGpx() {
    gpx = new GPX();
    waypoints = gpx.getWaypoints();
  }
  @Override
  public void closeGpx() {
    assertState(State.DEFAULT);
    waypoints = null;
  }
  @Override
  public void openRoute(Route template) {
    assertState(State.DEFAULT);
    state = State.ROUTE;
    route = new Route();
    route.name = template.name;
    gpx.getRoutes().add(route);
    waypoints = new ArrayList<Waypoint>();
    route.path = new Sequence(waypoints);
  }
  @Override
  public void closeRoute() {
    assertState(State.ROUTE);
    state = State.DEFAULT;
  }
  @Override
  public void openTrack(Track template) {
    assertState(State.DEFAULT);
    state = State.TRACK;
    track = new Track();
    track.segments = new ArrayList<Sequence>();
    track.name = template.name;
    gpx.getTracks().add(track);
  }
  @Override
  public void closeTrack() {
    assertState(State.TRACK);
    track = null;
    state = State.DEFAULT;
  }
  @Override
  public void openSegment() {
    assertState(State.TRACK);
    Sequence segment = new Sequence();
    track.getSegments().add(segment);
    waypoints = segment.getWaypoints(); 
    state = State.SEGMENT;
  }
  @Override
  public void closeSegment() {
    assertState(State.SEGMENT);
    state = State.TRACK;
  }
  public GPX getGpx() {
    return gpx;
  }  
}
