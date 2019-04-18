package BCWSN.FullNode; 


/**
  * La classe arco serve per rappresentare un arco del grafo
  */

public class Arco implements Comparable {

 	
  protected Object node1, node2;
  protected Object value;

  /**
   * Costruttore senza argomenti
   */
  public Arco() {
    node1 = node2 = null;
    value = null;
  }

  /**
   * Costruttore con tre argomenti
   * 
   * @param x1 primo nodo
   * @param y1 secondo nodo
   * @param v valore
   */
  public Arco(Object x1, Object y1, Object v) {
    node1 = x1;
    node2 = y1;
    value = v;
  }
 
  /**
   * ritorna il primo nodo
   * 
   * @return il primo nodo
   */
  public Object getNode1() { return node1; }
  
  /**
   * ritorna il secondo nodo
   * 
   * @return il secondo nodo
   */
  public Object getNode2() { return node2; }
  
  /**
   * Ritorna il valore
   * 
   * @return valore dell'arco
   */
  public Object getValue() { return value; }

  
  public boolean equals(Object a) {
	  if (a instanceof Arco) {
		Arco arc = (Arco) a;
		return (node1.equals(arc.node1) && node2.equals(arc.node2) && value.equals(arc.value));		
	  }
	  return false;
  }

  public int hashCode() {
    return node1.hashCode()+node2.hashCode()+value.hashCode();
  }

  public String toString() {
    return "<"+node1.toString()+", "+node2.toString()+"; "+value.toString()+">";
  }

  public int compareTo(Object a) {
    int i = ((Comparable) value).compareTo(((Arco)a).value);
    if (i==0) {
      int j = ((Comparable) node1).compareTo(((Arco)a).node1);
      if (j == 0)
        return ((Comparable) node2).compareTo(((Arco)a).node2);
      else
        return j;
    } else {
      return i;
    }
  }
}
