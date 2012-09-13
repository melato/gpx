package org.melato.gpx;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A track is a sequence of paths.
 *
 */
public class Track implements Cloneable {
	public List<Sequence> segments;
	public String name;
	public String desc;
		
	public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public List<Sequence> getSegments() {
		return segments;
	}
	
	public Date startTime() {
		for( Sequence path: getSegments() ) {
			Date start = path.startTime();
			if ( start != null )
				return start;
		}
		return null;
	}

  @Override
  public Track clone() {
    Track track;
    try {
      track = (Track) super.clone();
      track.segments = new ArrayList<Sequence>(segments.size());
      for( Sequence seq: segments ) {
        track.segments.add( seq.clone());
      }
      return track;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException( e );
    }
  }
}
