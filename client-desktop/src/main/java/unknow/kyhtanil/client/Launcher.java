package unknow.kyhtanil.client;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import javax.swing.*;

import unknow.common.*;
import unknow.common.cli.*;
import unknow.sync.*;

import com.badlogic.gdx.backends.lwjgl3.*;

public class Launcher
	{
	public static void main(String[] arg) throws Exception
		{
		boolean update=true;
		Args args=new Args(arg);
		String a;
		while ((a=args.next())!=null)
			{
			if("noupdate".equals(a))
				update=false;
			}

		File base=new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		base=base.getParentFile();
		String baseAbs=base.getAbsolutePath();

		Path client=Paths.get(baseAbs, "client.jar");

		if(update)
			{
			Path tmp=Paths.get(baseAbs, "tmp.jar");
			Files.copy(client, tmp, StandardCopyOption.REPLACE_EXISTING);
			try (URLClassLoader cl=new URLClassLoader(new URL[] {tmp.toUri().toURL()}, null))
				{
				Class<?> loadClass=cl.loadClass("unknow.kyhtanil.client.A");
				Method u=loadClass.getMethod("update", String.class);
				u.setAccessible(true);
				u.invoke(null, baseAbs);
				}
			Files.delete(tmp);
			}

		File f=new File(base, "lib/");
		File[] libs=f.listFiles();
		URL[] url=new URL[libs.length+1];
		url[0]=client.toUri().toURL();
		for(int i=0; i<libs.length; i++)
			url[i+1]=libs[i].toURI().toURL();
		System.out.println(Arrays.toString(url));

		URLClassLoader cl=new URLClassLoader(url, null);
		Thread.currentThread().setContextClassLoader(cl);
		Class<?> loadClass=cl.loadClass("unknow.kyhtanil.client.B");
		Method launch=loadClass.getMethod("launch");
		launch.setAccessible(true);
		launch.invoke(null);
		}
	}

class A extends JFrame implements SyncListener
	{
	private static final long serialVersionUID=1L;
	private JProgressBar total=new JProgressBar();
	private JLabel label=new JLabel("overwall progress ...");

	private Log listener;

	public A()
		{
		JPanel root=new JPanel(new GridLayout(4, 1));
		root.add(label);
		root.add(total);
		add(root);
		pack();
		setVisible(true);
		}

	public static boolean update(String basePath) throws Exception
		{
		A a=new A();
		SyncClient sync=new SyncClient(Cfg.getSystemString("updater.host"), Cfg.getSystemInt("updater.port"), "./");
		sync.setListener(a);

		sync.update(Cfg.getSystemString("updater.login"), Cfg.getSystemString("updater.pass"), Cfg.getSystemString("updater.project"), false, null);
		sync.close();
		a.dispose();
		return true;
		}

	public void startUpdate(String project, int modified, int news, int delete)
		{
		listener.startUpdate(project, modified, news, delete);
		total.setMaximum(2*(modified+news));
		}

	public void startFile(String name)
		{
		listener.startFile(name);
		label.setText("Updating '"+name+"'");
		}

	public void startCheckFile(String name)
		{
		listener.startCheckFile(name);
		label.setText("Checking '"+name+"'");
		}

	public void doneCheckFile(String name)
		{
		listener.doneCheckFile(name);
		total.setValue(total.getValue()+1);
		}

	public void startReconstruct(String name)
		{
		listener.startReconstruct(name);
		label.setText("Reconstructing '"+name+"'");
		}

	public void updateReconstruct(String name, float rate)
		{
		listener.updateReconstruct(name, rate);
		//XXX
		}

	public void doneReconstruct(String name, long fileSize, boolean ok)
		{
		listener.doneReconstruct(name, fileSize, ok);
		total.setValue(total.getValue()+(ok?1:-1));
		}

	public void doneFile(String name, long fileSize)
		{
		listener.doneFile(name, fileSize);
		}

	public void doneUpdate(String project)
		{
		listener.doneUpdate(project);
		}
	}

class B
	{
	public static void launch()
		{
		Lwjgl3ApplicationConfiguration conf=new Lwjgl3ApplicationConfiguration();
		conf.setTitle("Game");
		conf.setWindowedMode(560, 368);
		conf.setResizable(true);
		new Lwjgl3Application(new Main(), conf);
		}
	}
