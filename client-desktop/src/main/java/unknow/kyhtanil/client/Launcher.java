package unknow.kyhtanil.client;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import unknow.sync.client.SyncListener;
import unknow.sync.client.SyncRead;

/**
 * Game launcher
 * 
 * @author unknow
 */
public class Launcher {
	/**
	 * entry point
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
		System.setProperty("org.slf4j.simpleLogger.logFile", "log");
		boolean update = true;
		boolean fork = true;
		for (String a : arg) {
			if ("--noupdate".equals(a))
				update = false;
			if ("--nofork".equals(a))
				fork = false;
		}

		File base = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		base = base.getParentFile();
		String baseAbs = base.getAbsolutePath();

		Path client = Paths.get(baseAbs, "client.jar");

		Path tmp = Paths.get(baseAbs, "tmp.jar");

		if (fork && update) {
			Files.copy(client, tmp, StandardCopyOption.REPLACE_EXISTING);
			launch("-jar", tmp.toString(), "--nofork");
			System.exit(0);
		}

		if (update && !A.update())
			System.exit(1);

		File f = new File(client.getParent().toFile(), "lib/");
		File[] libs = f.listFiles();

		Object[] cp = new String[libs.length + 1];
		cp[0] = client.toString();
		for (int i = 0; i < libs.length; i++)
			cp[i + 1] = libs[i].toString();
		launch("-cp", implode(File.pathSeparator, cp), B.class.getName());
	}

	/**
	 * @param sep separator to use
	 * @param o   value to concatenate
	 * @return concatenated string
	 */
	public static String implode(String sep, Object... o) {
		return implode(sep, Arrays.asList(o));
	}

	/**
	 * @param sep  separator to use
	 * @param list value to concatenate
	 * @return concatenated string
	 */
	public static String implode(String sep, List<?> list) {
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			if (sb.length() > 0)
				sb.append(sep);
			sb.append(o.toString());
		}
		return sb.toString();
	}

	/**
	 * launch java
	 * 
	 * @param arg param to java
	 * @throws IOException
	 */
	public static void launch(String... arg) throws IOException {
		String javaHome = System.getProperty("java.home");

		ProcessBuilder pb = new ProcessBuilder(javaHome + "/bin/java");
		List<String> cmd = pb.command();
		for (int i = 0; i < arg.length; i++)
			cmd.add(arg[i]);
		pb.inheritIO();
		pb.start();
		System.out.println("launching: '" + implode(" ", pb.command()) + "'");
	}
}

class A extends JFrame implements SyncListener {
	private static final long serialVersionUID = 1L;
	private JProgressBar total = new JProgressBar();
	private JLabel label = new JLabel("overwall progress ...");

	private Log listener = new Log();

	public A() {
		JPanel root = new JPanel(new GridLayout(2, 1));
		root.add(label);
		root.add(total);
		add(root);
		setSize(330, 64);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static boolean update() throws Exception {
		A a = new A();
		try {
			Properties p = new Properties();
			try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("updater.properties")) {
				p.load(is);
			}
			SyncRead sync = new SyncRead(".", "temp", p.getProperty("host"), Integer.parseInt(p.getProperty("port")));
			sync.setListener(a);
			sync.process(p.getProperty("login"), null);
			a.dispose();
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			a.label.setText(e.toString());
		}
		return false;
	}

	@Override
	public void start() {
		listener.start();
	}

	@Override
	public void update(long done, long total) {
		listener.update(done, total);
		this.total.setValue((int) done);
		this.total.setMaximum((int) total);
	}

	@Override
	public void done(long total) {
		listener.done(total);
	}
}

class B {
	public static void main(String arg[]) {
		Lwjgl3ApplicationConfiguration conf = new Lwjgl3ApplicationConfiguration();
		conf.setTitle("Game");
		conf.setWindowedMode(1024, 768);
		conf.setResizable(true);
		conf.useOpenGL3(true, 3, 2);
		conf.useVsync(true);
		/* Lwjgl3Application a = */new Lwjgl3Application(new Main(), conf);
	}
}
