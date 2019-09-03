package unknow.kyhtanil.common.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Data<K, V>
	{
	private String path;
	private Map<K,Long> index;
	private RandomAccessFile d;
	private Kryo kryo=new Kryo();
	private Input in=new In();
	private Output out=new Out();

	@SuppressWarnings("unchecked")
	public Data(String path) throws FileNotFoundException
		{
		this.path=path;
		this.d=new RandomAccessFile(path, "rw");
		try
			{
			Input input=new Input(new FileInputStream(path+".idx"));
			this.index=(Map<K,Long>)kryo.readClassAndObject(input);
			input.close();
			}
		catch (FileNotFoundException e)
			{
			this.index=new HashMap<>();
			}
		}

	@SuppressWarnings("unchecked")
	public V get(K key) throws IOException
		{
		Long off=index.get(key);
		if(off==null)
			return null;
		d.seek(off);
		return (V)kryo.readClassAndObject(in);
		}

	public void put(K key, V value) throws IOException
		{
		d.seek(d.length());
		index.put(key, d.getFilePointer());
		kryo.writeClassAndObject(out, value);
		}

	public void save() throws IOException
		{
		Path tmp=Files.createTempFile("temp", ".tmp");
		Output o=new Output(new FileOutputStream(tmp.toFile())); // put tmp file
		Map<K,Long> ni=new HashMap<>();
		for(K k:index.keySet())
			{
			kryo.writeClassAndObject(o, get(k));
			ni.put(k, o.total());
			}
		o.close();
		Output output=new Output(new FileOutputStream(path+".idx"));
		kryo.writeClassAndObject(output, ni);
		out.close();
		Files.move(tmp, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
		index=ni;
		}

	private class In extends Input
		{
		@Override
		protected int fill(byte[] buffer, int offset, int count) throws KryoException
			{
			try
				{
				return d.read(buffer, offset, count);
				}
			catch (IOException ex)
				{
				throw new KryoException(ex);
				}
			}
		}

	private class Out extends Output
		{
		@Override
		public void flush() throws KryoException
			{
			try
				{
				d.write(buffer, 0, position);
				}
			catch (IOException ex)
				{
				throw new KryoException(ex);
				}
			position=0;
			}
		}
	}
