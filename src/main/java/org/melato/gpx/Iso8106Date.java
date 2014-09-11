/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013,2014, Alex Athanasopoulos.  All Rights Reserved.
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Iso8106Date {
  static final TimeZone UTC = TimeZone.getTimeZone( "GMT" );
  static final Pattern datePattern = Pattern.compile( "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})T([0-9]{1,2}):?([0-9]{2}):?([0-9\\.]*)([Z+-]?.*)");
  static final Pattern tzPattern = Pattern.compile( "([-+])([0-9]{2}):([0-9]{2})");

  public static Date parseDate(String s) {
    long time = parseTime(s);
    if ( time != 0 )
      return new Date(time);
    return null;
  }
  public static int parseTimeZone(String tz) {
    if ( "Z".equals(tz)) {
      return 0;
    }
    if ( tz.length() == 0 ) {
      return 0;
    }
    Matcher matcher = tzPattern.matcher(tz);
    if ( matcher.matches()) {
      int minutes = 60*Integer.parseInt(matcher.group(2)) + Integer.parseInt(matcher.group(3));
      String sign = matcher.group(1);
      if ( "-".equals(sign)) {
        minutes = - minutes;
      }
      return minutes;
    }
    return 0;    
  }
  /**
   * Parse a GPX date (ISO 8601). 
   * Example:  2011-09-25T10:17:37Z
   * @param s
   */
  public static long parseTime(String s) {
    if ( s == null )
      return 0;
    s = s.trim();
    if ( s.length() == 0 )
      return 0;
    Matcher matcher = datePattern.matcher(s);
    if ( matcher.matches() ) {
      int year = Integer.parseInt( matcher.group(1));
      int month = Integer.parseInt( matcher.group(2));
      int day = Integer.parseInt( matcher.group(3));
      int hour = Integer.parseInt( matcher.group(4));
      int minute = Integer.parseInt( matcher.group(5));
      float second = Float.parseFloat( matcher.group(6));
      int millisecond = Math.round(second*1000);
      boolean isUtc = "Z".equals( matcher.group(7));
      GregorianCalendar calendar = new GregorianCalendar(year,  month - 1, day, hour, minute);      
      if ( isUtc ) {
        calendar.setTimeZone(UTC);
      } else {
        int minutes = parseTimeZone(matcher.group(7));
        if ( minutes != 0 ) {
          calendar.setTimeZone(UTC);
          calendar.add(Calendar.MINUTE, -minutes);
        }
      }
      long time = calendar.getTimeInMillis();
      time += millisecond;
      return time;
    }
    throw new IllegalArgumentException( "Invalid date: " + s );
  }
  

}
