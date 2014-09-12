package org.melato.gpx;

import java.util.ArrayList;
import java.util.List;

import org.melato.xml.XMLMappingHandler;
import org.melato.xml.XMLStringHandler;
import org.xml.sax.SAXException;

public class MetadataHandler extends XMLMappingHandler {
  private GPX gpx;
  private XMLStringHandler nameHandler = new XMLStringHandler();
  private XMLStringHandler descHandler = new XMLStringHandler();
  private List<KeyValue> extensions = new ArrayList<KeyValue>();
  private KeyValueHandler extensionHandler = new KeyValueHandler(extensions);
  

  public MetadataHandler(GPX gpx) {
    super();
    this.gpx = gpx;
    setHandler(GPX.NAME, nameHandler);
    setHandler(GPX.DESC, descHandler);
    setHandler(GPX.EXTENSIONS, extensionHandler);
}

  @Override
  public void end() throws SAXException {
    super.end();
    gpx.setName(nameHandler.getText());
    gpx.setDesc(descHandler.getText());
    gpx.getExtensions().setValues(extensions.toArray(new KeyValue[0]));
    extensions.clear();    
  }
}
