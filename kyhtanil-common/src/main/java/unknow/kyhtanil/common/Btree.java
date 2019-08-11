package unknow.kyhtanil.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.Array;

public class Btree<K extends Comparable<K>, V> implements Map<K,V>
	{
	private int nodeSize;
	private int ht;
	private Node index;

	public Btree(int nodeSize)
		{
		this.nodeSize=nodeSize;
		index=new Node();
		}

	private class Node
		{
		Entry<K>[] data=new Entry[nodeSize];
		int len=0;
		}

	private static class Entry<K extends Comparable<K>> implements Map.Entry<K,Object>, Comparable<K>
		{
		K k;
		Object v;

		@Override
		public K getKey()
			{
			return k;
			}

		@Override
		public Object getValue()
			{
			return v;
			}

		@Override
		public Object setValue(Object value)
			{
			Object v=this.v;
			this.v=value;
			return v;
			}

		@Override
		public int compareTo(K o)
			{
			return k.compareTo(o);
			}
		}

	@Override
	public void clear()
		{
		// TODO Auto-generated method stub
		}

	@Override
	public boolean containsKey(Object arg0)
		{
		// TODO Auto-generated method stub
		return false;
		}

	@Override
	public boolean containsValue(Object arg0)
		{
		// TODO Auto-generated method stub
		return false;
		}

	@Override
	public Set<Map.Entry<K,V>> entrySet()
		{
		// TODO Auto-generated method stub
		return null;
		}

	@Override
	public V get(Object o)
		{
		K key=(K)o;
		Node n=index;
		int ht=this.ht;
		int i;
		while (ht>0)
			{
			i=Arrays.binarySearch(n.data, 0, n.len, key);
			if(i<0)
				i=-i-1;
			n=(Node)n.data[i].v;
			}

		i=Arrays.binarySearch(n.data, 0, n.len, key);
		if(i>0&&i<n.len)
			return (V)n.data[i].v;
		return null;
		}

	@Override
	public boolean isEmpty()
		{
		return index.len==0;
		}

	@Override
	public Set<K> keySet()
		{
		// TODO Auto-generated method stub
		return null;
		}

	@Override
	public V put(K key, V v)
		{
		Node n=index;
		int ht=this.ht;
		int i;
		while (ht>0)
			{
			i=Arrays.binarySearch(n.data, 0, n.len, key);
			if(i<0)
				i=-i-1;
			n=(Node)n.data[i].v;
			}

		if(n.len==nodeSize)
			{
			Node nn=new Node();
			int m=nodeSize/2;
			System.arraycopy(n.data, m, nn.data, 0, nodeSize-m);
			Arrays.fill(n.data, m, nodeSize, null);
			// TODO insert into parent
			}

		i=Arrays.binarySearch(n.data, 0, n.len, key);
		if(i>0&&i<n.len)
			return (V)n.data[i].v;
		return null;
		}

	@Override
	public void putAll(Map<? extends K,? extends V> map)
		{
		for(Map.Entry<? extends K,? extends V> e:map.entrySet())
			put(e.getKey(), e.getValue());
		}

	@Override
	public V remove(Object arg0)
		{
		// TODO Auto-generated method stub
		return null;
		}

	@Override
	public int size()
		{
		// TODO Auto-generated method stub
		return 0;
		}

	@Override
	public Collection<V> values()
		{
		// TODO Auto-generated method stub
		return null;
		}
	}
