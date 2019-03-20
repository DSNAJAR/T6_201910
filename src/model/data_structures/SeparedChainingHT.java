package model.data_structures;

import java.util.Iterator;

public class SeparedChainingHT <K extends Comparable<K>, V> implements IHashTable{
	
	
	@Override
	public Iterator<K> iterator() {
		// TODO Auto-generated method stub
		Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
	}

	@Override
	public void put(Comparable key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object delete(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

}
