package model.data_structures;

public class Nodo <T extends Comparable<T>>
{
	public T item;
	
	public Nodo<T> siguiente;
	
	public Nodo(T pItem)
	{
		this.item = pItem;
		
		this.siguiente = null;
	}
}

