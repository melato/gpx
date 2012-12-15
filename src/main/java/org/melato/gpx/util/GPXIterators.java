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
package org.melato.gpx.util;

import java.util.Iterator;
import java.util.List;

import org.melato.gps.PointTime;
import org.melato.gpx.GPX;
import org.melato.gpx.Route;
import org.melato.gpx.Sequence;
import org.melato.gpx.Track;
import org.melato.gpx.Waypoint;
import org.melato.util.DelegateIterable;
import org.melato.util.DelegateList;
import org.melato.util.Iterators;

public class GPXIterators {
  public static <T> Iterator<T> emptyIterator() {
    return new EmptyIterator<T>();
  }
  
  public static Iterable<PointTime> trackWaypoints(List<Track> tracks) {
    return new TrackWaypointIterable(tracks);
  }

  public static Iterable<PointTime> trackWaypoints(GPX gpx) {
    return new TrackWaypointIterable(gpx.getTracks());
  }

  public static Iterable<Waypoint> routeWaypoints(GPX gpx) {
    return new RouteWaypointIterable(gpx.getRoutes());
  }

  @SuppressWarnings("unchecked")
  public static Iterable<PointTime> allWaypoints(GPX gpx) {
    return Iterators.concatenate( new DelegateList<Waypoint,PointTime>(gpx.getWaypoints()),
        new DelegateIterable<PointTime>(routeWaypoints(gpx)),
        trackWaypoints(gpx));
  }
  
  private static class EmptyIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public T next() {
      return null;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }    
  }
  public static class TrackWaypointIterable implements Iterable<PointTime> {
    private List<Track> tracks;

    public TrackWaypointIterable(List<Track> tracks) {
      super();
      this.tracks = tracks;
    }

    @Override
    public Iterator<PointTime> iterator() {
      return new TrackWaypointIterator(tracks);
    }
    
  }
  private static class TrackWaypointIterator implements Iterator<PointTime> {
    private Iterator<Track> tracks;
    private Iterator<Sequence> segments = emptyIterator();
    private Iterator<Waypoint> waypoints = emptyIterator();

    public TrackWaypointIterator(List<Track> tracks) {    
      this.tracks = tracks.iterator();
    }
    @Override
    public boolean hasNext() {
      for(;;) {
        if ( waypoints.hasNext() ) {
          return true;
        }
        if ( segments.hasNext() ) {
          waypoints = segments.next().getWaypoints().iterator();
          continue;
        }
        if ( tracks.hasNext() ) {
          segments = tracks.next().getSegments().iterator();
          continue;
        }
        return false;
      }
    }
    @Override
    public PointTime next() {
      return waypoints.next();
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
  }
  public static class RouteWaypointIterable implements Iterable<Waypoint> {
    private List<Route> routes;


    public RouteWaypointIterable(List<Route> routes) {
      super();
      this.routes = routes;
    }

    @Override
    public Iterator<Waypoint> iterator() {
      return new RouteWaypointIterator(routes);
    }
    
  }
  private static class RouteWaypointIterator implements Iterator<Waypoint> {
    private Iterator<Route> routes;
    private Iterator<Waypoint> waypoints = emptyIterator();

    public RouteWaypointIterator(List<Route> routes) {    
      this.routes = routes.iterator();
    }
    @Override
    public boolean hasNext() {
      for(;;) {
        if ( waypoints.hasNext() ) {
          return true;
        }
        if ( routes.hasNext() ) {
          waypoints = routes.next().getWaypoints().iterator();
          continue;
        }
        return false;
      }
    }
    @Override
    public Waypoint next() {
      return waypoints.next();
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
  }
}
