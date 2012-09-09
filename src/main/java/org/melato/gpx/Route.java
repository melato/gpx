package org.melato.gpx;

import java.util.List;


public class Route implements Cloneable {
	public Sequence path;
	public String name;
  
	
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
	
	public List<Waypoint> getWaypoints() {
	  return path.getWaypoints();
	}

	
  public Sequence getPath() {
    return path;
  }

  public void setPath(Sequence path) {
    this.path = path;
  }

  public void setName(String name) {
    this.name = name;
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
