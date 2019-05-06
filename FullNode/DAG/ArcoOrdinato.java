package DAG;

/**
 * Rappresenta un arco ordinato, estende la classe Arco
 * Per convenzione node1 e' il nodo da cui parte l'arco (from),
 * node2 e' il nodo in cui l'arco arriva (to)
 *
 */
public class ArcoOrdinato extends Arco{
	
		
	 /**
	   * Costruttore con tre argomenti
	   * 
	   * @param x1 nodo partenza
	   * @param y1 nodo arrivo
	   * @param v valore
	   */
	  public ArcoOrdinato(Object x1, Object y1, Object v) {
	    node1 = x1;
	    node2 = y1;
	    value = v;
	  }
	 
	
	/**
	 * Restituisce il nodo di partenza dell'arco
	 * 
	 * @return nodo da cui parte l'arco orientato
	 */
	public Object getFrom(){
		return node1;
	}
	
	/**
	 * Restituisce il nodo di arrivo dell'arco
	 * 
	 * @return nodo a cui arriva l'arco orientato
	 */
	public Object getTo(){
		return node2;
	}
	
	
	@Override
	public String toString() {
	    return "<"+node1.toString()+" -> "+node2.toString()+"; "+value.toString()+">";		
	}
	
}
