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
