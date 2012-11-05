/*-------------------------------------------------------------------------
 * Copyright (c) 2012, Alex Athanasopoulos.  All Rights Reserved.
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
