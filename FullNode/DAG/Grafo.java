package DAG;

import java.util.*;

 /**
  * La classe Grafo rappresenta un grafo mediante liste di adiacenza.
  * In particolare si e' voluto dare un'implementazione che utilizzasse classi
  * standard di java.util.
  * Di conseguenza:
  * 1. la lista dei nodi e' rappresentata da una HashMap per poter accedere al
  * nodo node1 in tempo costante
  * 2. la lista dei nodi adiacenti e' rappresentata da un HashSet di archi, in
  * modo tale da poter verificare/accedere al nodo adiacente in tempo costante.
  * Anziche' rappresentare il nodo adiacente e il peso dell'arco si e' preferito
  * rappresentare l'arco completo.
  *
  */
public class Grafo {
  HashMap<Object,Set<Arco>> nodi;
  int nArchi;

  /**
   * Costruttore senza argomenti
   */
  public Grafo() {
    nodi = new HashMap<Object,Set<Arco>>();
    nArchi = 0;
  }

  /**
   * restituisce il numero di nodi 
   * 
   * @return il numero di nodi
   */
  public int nodesNumber() {
    return nodi.size();
  }

  /**
   * Restituisce il numero di archi
   * 
   * @return il numero di archi
   */
  public int edgesNumber() {
    return nArchi;
  }

  
  /**
   * aggiunge un nodo al grafo con valore x se x non e' presente nel grafo, nulla altrimenti
   * L'aggiunta di un nodo significa aggiungere la coppia (x, lista) nella HashMap
   * dove lista e' una HashSet vuota.
   * 
   * @param x il nodo da aggiungere
   */
  public void add(Object x) {
    if (!nodi.containsKey(x)) {
      Set<Arco> lista = new HashSet<Arco>();
      nodi.put(x,lista);
    }
  }

  /**
   * rimuove il nodo x dal grafo se x e' presente nel grafo, 
   * altrimenti non modifica il grafo.
   * 
   * @param x il nodo da rimuovere
   */
  public void remove(Object x) {
    if (nodi.containsKey(x)) {
      Iterator arcoIncidenteI = ( (HashSet) nodi.get(x) ).iterator();
      Arco a;
      Object y;
      while (arcoIncidenteI.hasNext()) {
        a = (Arco) arcoIncidenteI.next();
        y = ( a.getNode1().equals(x) ) ? a.getNode2() : a.getNode1();
        if ( ((HashSet) nodi.get(y)).remove(a) )
          nArchi--;
      }
      nodi.remove(x);
    }
  }


  /**
   * aggiunge un arco tra i nodi x e y se tale arco non e' gia' presente e restituisce true, 
   * altrimenti non modifica il grafo e restituisce false. 
   * 
   * @param x primo nodo dell'arco
   * @param y secondo nodo dell'arvo
   * @param value valore dell'arco
   * @return vero se l'arco e' stato rimosso false altrimenti
   */
  public boolean add(Object x, Object y, Object value) {
    boolean flag = false, flag1 = false;
    if (!nodi.containsKey(x))
      add(x);
    if (!nodi.containsKey(y))
      add(y);
    Arco a = new Arco(x,y,value);
    flag = (nodi.get(x) ).add(a);
    flag1 =(nodi.get(y) ).add(a);
    flag = flag && flag1;
    if (flag)
      nArchi++;
    return flag;
  }

  /**
   * Aggiunge l'arco a al grafo se l'arco non e' presente e restituisce true,
   * altrimenti non modifica il grafo e restituisce false
   * 
   * @param a l'arco da aggiungere
   * @return true se l'arco e' stato aggiunto, false altrimenti
   */
  public boolean add(Arco a) {
    return add(a.getNode1(),a.getNode2(),a.getValue());
  }

  /**
   * Rimuove l'arco tra i nodi x e y se tale arco e' presente e restituisce true, 
   * altrimenti non modifica il grafo e restituisce false. 
   * 
   * @param x primo nodo dell'arco
   * @param y secondo nodo dell'arvo
   * @param value valore dell'arco
   * @return vero se l'arco e' stato rimosso false altrimenti
   */
  public boolean remove(Object x, Object y, Object value) {
    Arco a = new Arco(x,y,value);
    return remove(a);
  }

  /**
   * Rimuove l'arco a dal grafo se l'arco e' presente e restituisce true,
   * altrimenti non modifica il grafo e restituisce false
   * 
   * @param a l'arco da aggiungere
   * @return true se l'arco e' stato aggiunto, false altrimenti
   */
  public boolean remove(Arco a) {
    boolean flag = false,  flag1 = false;
    if (nodi.containsKey(a.getNode1()) && nodi.containsKey(a.getNode2())) {
      flag = ( (HashSet) nodi.get(a.getNode1()) ).remove(a);
      flag1 = ( (HashSet) nodi.get(a.getNode2()) ).remove(a);
    }
    return flag || flag1;
  }

  /**
   * Restituisce l'insieme degli archi presenti nel grafo
   * 
   * @return l'insieme di archi presenti nel grafo
   */
  public Set<Arco> getEdgeSet() {
    Set<Arco> setArchi = new HashSet<Arco>();
    Iterator<Set<Arco>> hashSetI = nodi.values().iterator();
    while (hashSetI.hasNext())
      setArchi.addAll((Set<Arco>) hashSetI.next());

    return setArchi;
  }

  /**
   * Restituisce l'insieme di archi incidenti sul nodo nodo,
   * se nodo e' presente nel grafo, null altrimenti 
   * 
   * @param nodo nodo di cui si vuole conoscere l'insieme di archi incidenti
   * @return l'insieme di archi incidenti sul nodo nodo, 
   * se nodo e' presente nel grafo null altrimenti
   */
  public Set<Arco> getEdgeSet(Object nodo) {
    if (nodi.containsKey(nodo)) //se il nodo e' presente nel grafo
      return nodi.get(nodo);
    else
      return null;
  }

  /**
   * Restituisce l'insieme di nodi del grafo
   * 
   * @return l'insieme di nodi del grafo
   */
  public Set<Object> getNodeSet() {
    return nodi.keySet();
  }

  public String toString() {
    StringBuffer out = new StringBuffer();
    Object nodo;
    Arco a;
    Iterator arcoI;
    Iterator nodoI = nodi.keySet().iterator();
    while (nodoI.hasNext()) {
      arcoI = ((Set) nodi.get( nodo = nodoI.next() )).iterator();
      out.append("Nodo " + nodo.toString() + ": ");
      while (arcoI.hasNext()) {
        a = (Arco)arcoI.next();
        //out.append( ((a.x == nodo ) ? a.y.toString() : a.x.toString()) + "("+a.value.toString()+"), ");
        out.append(a.toString()+", ");
      }
      out.append("\n");
    }
    return out.toString();
  }
  
//  public static void main(String[] args) {
//	    Arco a = new Arco();
//	    Grafo g = new Grafo();
//	    g.add("a","b",new Integer(1));
//	    g.add("a","c",new Integer(1));
//	    g.add("a","e",new Integer(3));
//	    g.add("c","d",new Integer(4));
//	    g.add("c","e",new Integer(2));
//	    g.add("b","d",new Integer(3));

//	    System.out.println("Il grafo G e':\n" + g);
//	    System.out.println("L'insieme di archi e': " + g.getEdgeSet());

//	  }

  
}
