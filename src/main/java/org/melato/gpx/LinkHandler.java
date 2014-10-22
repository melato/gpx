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

import java.util.Collection;

import org.melato.gpx.Waypoint.Link;
import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.melato.xml.XMLTag;
import org.xml.sax.SAXException;

class LinkHandler extends XMLMappingHandler {
	Collection<Link> links;
	XMLStringHandler typeHandler = new XMLStringHandler();
	XMLStringHandler textHandler = new XMLStringHandler();
	String href;
	
	public LinkHandler(Collection<Link> links) {
		this.links = links;
		setHandler(GPX.TYPE, typeHandler);
		setHandler(GPX.TEXT, textHandler);
	}

	@Override
	public void start(XMLTag tag) throws SAXException {
	  href = tag.getAttribute(GPX.HREF);
      typeHandler.clear();
      textHandler.clear();
	}

	@Override
	public void end() throws SAXException {
	  Link link = new Link();
	  link.href = href;
	  link.type = typeHandler.getText();
      link.text = textHandler.getText();
      links.add(link);
	}
}
