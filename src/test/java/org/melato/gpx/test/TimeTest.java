package org.melato.gpx.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.melato.gpx.GPX;

public class TimeTest {

	public @Test void timezones() {
		for( String id: TimeZone.getAvailableIDs() ) {
			//System.out.println( id );
		}
	}	
	
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
