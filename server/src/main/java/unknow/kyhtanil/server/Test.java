package unknow.kyhtanil.server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.lang.model.element.*;
import javax.script.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

import unknow.common.tools.*;

public abstract class Test
	{
	

	public static void main(String[] arg) throws Exception
		{
		ScriptEngine js=new ScriptEngineManager().getEngineByName("javascript");
		
		System.out.println(js.eval("var t=Java.type('unknow.kyhtanil.server.Test'); new t {f:function f() {}}"));
		
		if(true)
			return;

		JavaCompiler cc=ToolProvider.getSystemJavaCompiler();
		FileManager manager=new FileManager(cc.getStandardFileManager(null, null, null));
		CompilationTask task=cc.getTask(null, manager, null, null, null, new Iterable<JavaFileObject>()
			{

				public Iterator<JavaFileObject> iterator()
					{
					return new ArrayIterator<JavaFileObject>(new JavaFileObject[] {new JavaSourceFromString("TestCompile", "public class TestCompile implements Runnable { public void run() {System.out.println(\"this run\");}}")});
					}
			});
		task.call();

		ClassLoaderTest cl=new ClassLoaderTest();
		for(OutputFile o:manager.out)
			{
			System.out.println(o.getName());
			Class<?> c=cl.defineClass(o.getName(), o.out.toByteArray());
			Runnable r=(Runnable)c.newInstance();
			r.run();
			}
		}

	public static class ClassLoaderTest extends ClassLoader
		{
		public Class<?> defineClass(String name, byte[] buf)
			{
			return defineClass(name, buf, 0, buf.length);
			}
		}

	public static class FileManager implements JavaFileManager
		{
		private JavaFileManager manager;
		public List<OutputFile> out=new ArrayList<OutputFile>();

		public FileManager(JavaFileManager manager)
			{
			this.manager=manager;
			}

		public int isSupportedOption(String option)
			{
			return manager.isSupportedOption(option);
			}

		public ClassLoader getClassLoader(Location location)
			{
			return manager.getClassLoader(location);
			}

		public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException
			{
			return manager.list(location, packageName, kinds, recurse);
			}

		public String inferBinaryName(Location location, JavaFileObject file)
			{
			return manager.inferBinaryName(location, file);
			}

		public boolean isSameFile(FileObject a, FileObject b)
			{
			return manager.isSameFile(a, b);
			}

		public boolean handleOption(String current, Iterator<String> remaining)
			{
			return manager.handleOption(current, remaining);
			}

		public boolean hasLocation(Location location)
			{
			return manager.hasLocation(location);
			}

		public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException
			{
			return manager.getJavaFileForInput(location, className, kind);
			}

		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException
			{
			OutputFile o=new OutputFile(className);
			out.add(o);
			return o;
			}

		public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException
			{
			return manager.getFileForInput(location, packageName, relativeName);
			}

		public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException
			{
			return null;
			}

		public void flush() throws IOException
			{
			manager.flush();
			}

		public void close() throws IOException
			{
			manager.close();
			}
		}

	/**
	* A file object used to represent source coming from a string.
	*/
	public static class JavaSourceFromString extends SimpleJavaFileObject
		{
		/**
		 * The source code of this "file".
		 */
		final String code;

		/**
		 * Constructs a new JavaSourceFromString.
		 * @param name the name of the compilation unit represented by this file object
		 * @param code the source code for the compilation unit represented by this file object
		 */
		public JavaSourceFromString(String name, String code)
			{
			super(URI.create("string:///"+name.replace('.', '/')+Kind.SOURCE.extension), Kind.SOURCE);
			this.code=code;
			}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors)
			{
			return code;
			}
		}

	public static class OutputFile implements JavaFileObject
		{
		private String name;
		public ByteArrayOutputStream out=new ByteArrayOutputStream();

		public OutputFile(String name)
			{
			this.name=name;
			}

		public URI toUri()
			{
			System.out.println("toUri");
			return null;
			}

		public String getName()
			{
			return name;
			}

		public InputStream openInputStream() throws IOException
			{
			System.out.println("openInputStream");
			return null;
			}

		public OutputStream openOutputStream() throws IOException
			{
			return out;
			}

		public Reader openReader(boolean ignoreEncodingErrors) throws IOException
			{
			System.out.println("openReader");
			return null;
			}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
			{
			System.out.println("getCharContent");
			return null;
			}

		public Writer openWriter() throws IOException
			{
			System.out.println("openWriter");
			return null;
			}

		public long getLastModified()
			{
			System.out.println("getLastModified");
			return 0;
			}

		public boolean delete()
			{
			System.out.println("delete");
			return false;
			}

		public Kind getKind()
			{
			System.out.println("getKind");
			return null;
			}

		public boolean isNameCompatible(String simpleName, Kind kind)
			{
			System.out.println("isNameCompatible");
			return false;
			}

		public NestingKind getNestingKind()
			{
			System.out.println("getNestingKind");
			return null;
			}

		public Modifier getAccessLevel()
			{
			System.out.println("getAccessLevel");
			return null;
			}
		}
	}
