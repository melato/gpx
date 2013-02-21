/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013, Alex Athanasopoulos.  All Rights Reserved.
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

import java.util.List;


public class Route implements Cloneable {
	public Sequence path;
	public String name;
  public String label;
  
	
  public Route() {
    super();
  }

	public Route(Sequence path, String name) {
    super();
    this.path = path;
    this.name = name;
  }
	
  public Route(List<Waypoint> waypoints) {
    super();
    this.path = new Sequence(waypoints);
  }
  
  public String getName() {
    return name;
  }
	
  public void setName(String name) {
    this.name = name;
  }
  
	public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<Waypoint> getWaypoints() {
	  return path.getWaypoints();
	}
	
  public Sequence getPath() {
    return path;
  }

  public void setPath(Sequence path) {
    this.path = path;
  }

  @Override
  public Route clone() {
    Route copy;
    try {
      copy = (Route) super.clone();
      copy.path = (Sequence) path.clone();
      return copy;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException( e );
    }
  }
}
