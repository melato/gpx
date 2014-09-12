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

import java.util.ArrayList;
import java.util.Collection;

import org.melato.xml.XMLElementHandler;
import org.melato.xml.XMLNullHandler;
import org.melato.xml.XMLStringHandler;
import org.melato.xml.XMLTag;
import org.xml.sax.SAXException;

class KeyValueHandler extends XMLNullHandler {
  private Collection<KeyValue> list;
  private String key;
  private XMLStringHandler kvHandler = new XMLStringHandler();

  public KeyValueHandler(Collection<KeyValue> list) {
    this.list = list;
  }
  public KeyValueHandler() {
    this.list = new ArrayList<KeyValue>();
  }
  @Override
  public void start(XMLTag tag) throws SAXException {
    key = null;
    list.clear();
  }


  @Override
  public XMLElementHandler getHandler(XMLTag tag) {
    if ( key != null ) {
      list.add(new KeyValue(key, kvHandler.getText()));
    }
    key = tag.getName();
    return kvHandler;
  }
  
  @Override
  public void end() throws SAXException {
    if ( key != null ) {
      list.add(new KeyValue(key, kvHandler.getText()));
      key = null;
    }
  }

}
