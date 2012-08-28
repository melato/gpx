package org.melato.gpx;



public class Route implements Cloneable {
	public Sequence path;
	public String name;
  
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
