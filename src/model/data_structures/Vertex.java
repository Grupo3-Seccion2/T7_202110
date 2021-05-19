package model.data_structures;
import model.data_structures.*;

public class Vertex <K extends  Comparable<K>,V extends Comparable<V>> implements IVertex<K, V>, Comparable<Vertex<K, V>> 
{
	private K id;
	private V value;
	private boolean marked;
	private int indegree;
	private ArregloDinamico<Edge<K,V>> arcos;

	public Vertex(K id, V value)
	{
		arcos = new ArregloDinamico<>(10);
		this.id = id;
		this.value = value;
		marked = false;
		indegree = 0;
	}

	public K getId()
	{
		return id;
	}

	public V getInfo()
	{
		return value;
	}

	public boolean getMark()
	{
		return marked;
	}

	public void addEdge(Edge<K, V> edge)
	{
		arcos.addLast(edge);
		indegree++;
	}


	public void mark(Edge<K,V> edgeTo) 
	{
		edgeTo.getDestination().marked = true;
	}

	@Override
	public void unmark()
	{
		marked = false;
	}

	@Override
	public int outdegree() {
		return indegree;
	}

	@Override
	public int indegree() {
		// TODO Auto-generated method stub
		return indegree;
	}

	@Override
	public Edge<K, V> getEdge(K vertex) {
		Edge<K,V> temp = null;
		for (int i = 1; i < arcos.size()  ; i++)
		{
			if(arcos.getElement(i).getDestination().equals(vertex))
			{
				temp = arcos.getElement(i);
				break;
			}
		}
		return temp;
	}

	@Override
	public ILista<Vertex<K, V>> vertices() 
	{
		ArregloDinamico<Vertex<K,V>> lista = new ArregloDinamico<Vertex<K, V>>(10000);
		for (int i = 0; i < arcos.size(); i++) 
		{
			lista.addLast(arcos.getElement(i).getDestination());
		}
		return lista;
	}

	@Override
	public ILista<Edge<K, V>> edges() 
	{
		return arcos;
	}

	@Override
	public int compareTo(Vertex<K, V> o) {
		return value.compareTo((V) o);
	}

	public void dfs(Edge<K,V> edgeTo)
	{
		this.marked = true;
		for(int i = 0; i <= arcos.size(); i++)
		{
			Vertex<K,V> destino = arcos.getElement(i).getDestination();
			if(!destino.marked)
			{
				destino.dfs(arcos.getElement(i));
			}
			
		}
	}
	
	public void bfs()
	{
		ICola<Vertex<K,V>> cola = new ColaEncadenada<Vertex<K,V>>();
		this.marked = true;
		cola.enqueue(this);
		while(cola.peek() != null)
		{
			Vertex<K, V> actual = cola.dequeue();
			for(int i = 0; i < actual.edges().size(); i++)
			{
				Vertex<K, V> dest = actual.arcos.getElement(i).getDestination();
				if(!dest.marked)
				{
					dest.marked = true;
					cola.enqueue(dest);
				}
			}
		}
	}
	

}
