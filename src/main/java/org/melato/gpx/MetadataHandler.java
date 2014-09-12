package org.melato.gpx;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;

public class MetadataHandler extends XMLMappingHandler {
  private GPX gpx;
  private XMLStringHandler nameHandler = new XMLStringHandler();
  private XMLStringHandler descHandler = new XMLStringHandler();

  public MetadataHandler(GPX gpx) {
    super();
    this.gpx = gpx;
    setHandler(GPX.NAME, nameHandler);
    setHandler(GPX.DESC, descHandler);
  }

  @Override
  public void end() throws SAXException {
    super.end();
    gpx.setName(nameHandler.getText());
    gpx.setDesc(descHandler.getText());
    
  }
}
