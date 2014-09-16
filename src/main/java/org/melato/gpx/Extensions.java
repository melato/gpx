package org.melato.gpx;

public class Extensions {
  private KeyValue[] values = KeyValue.EMPTY_ARRAY;

  public KeyValue[] getValues() {
    return values;
  }

  public void setValues(KeyValue[] values) {
    this.values = values;
  }
  public String getValue(String key) {
    for(KeyValue kv: values) {
      if ( key.equals(kv.getKey())) {
        return kv.getValue();
      }
    }
    return null;    
  }
  
}
