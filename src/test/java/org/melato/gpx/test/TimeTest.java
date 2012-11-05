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
package org.melato.gpx.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.melato.gpx.GPX;

public class TimeTest {

	public @Test void parseDate() {
		String datestring = "2011-09-25T13:24:35Z";
		Date date = GPX.parseDate(datestring);
		DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Assert.assertEquals( "2011-09-25T13:24:35", format.format(date));
	}
	
	public @Test void parseDate1() {
		String datestring = "2011-10-1T10:20:30Z";
		Date date = GPX.parseDate(datestring);
		DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Assert.assertEquals( "2011-10-01T10:20:30", format.format(date));
	}
	
}
