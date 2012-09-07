package org.melato.gpx;

import java.util.Collections;
import java.util.List;

/** A GPX waypoint:  geographical point with annotations. */
public class Waypoint extends Point {
	public String	name;
	public String	desc;
	public String	type;
  public String sym;
	public List<String> links = emptyLinks;

	private static List<String> emptyLinks = Collections.emptyList();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getLinks() {
		return links;
	}
	public String getLink() {
		return links.isEmpty() ? null : links.get(0);
	}
	public void setLinks(List<String> links) {
		this.links = links;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getSym() {
    return sym;
  }
  public void setSym(String sym) {
    this.sym = sym;
  }
  public Waypoint(){}
	public Waypoint(float lat, float lon) {
		super(lat, lon);
	}
  public Waypoint(Point p) {
    this(p.lat, p.lon );
    altitude = p.altitude;
    time = p.time;
    speed = p.speed;
  }
	public Waypoint(Waypoint p) {
		this(p.lat, p.lon );
		altitude = p.altitude;
		time = p.time;
		sym = p.sym;
		name = p.name;
		desc = p.desc;
		type = p.type;
	}
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append( lat );
		buf.append( ';' );
		buf.append( lon );
		if ( type != null ) {
			buf.append( " type=" + type );
		}
    if ( sym != null ) {
      buf.append( " sym=" + sym );
    }
		if ( name != null ) {
			buf.append( " name=" + name );
		}
		if ( desc != null ) {
			buf.append( " desc=" + desc );
		}
		return buf.toString();
	}
}
