package unknow.kyhtanil.client;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import unknow.common.*;
import unknow.common.cli.*;
import unknow.sync.*;

import com.badlogic.gdx.backends.lwjgl3.*;

public class Launcher {
	public static void main(String[] arg) throws Exception {
		boolean update = true;
		boolean fork = true;
		Args args = new Args(arg);
		String a;
		while ((a = args.next()) != null) {
			if ("noupdate".equals(a))
				update = false;
			if ("nofork".equals(a))
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

		if (update)
			A.update();

		File f = new File(client.getParent().toFile(), "lib/");
		File[] libs = f.listFiles();

		Object[] cp = new String[libs.length + 1];
		cp[0] = client.toString();
		for (int i = 0; i < libs.length; i++)
			cp[i + 1] = libs[i].toString();
		launch("-cp", implode(File.pathSeparator, cp), B.class.getName());

		Files.deleteIfExists(tmp);
	}

	public static String implode(String sep, Object... o) {
		return implode(sep, Arrays.asList(o));
	}

	public static String implode(String sep, List<?> list) {
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			if (sb.length() > 0)
				sb.append(sep);
			sb.append(o.toString());
		}
		return sb.toString();
	}

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
		try (SyncClient sync = new SyncClient(Cfg.getSystemString("updater.host"), Cfg.getSystemInt("updater.port"), "./")) {
			sync.setListener(a);

			sync.update(Cfg.getSystemString("updater.login"), Cfg.getSystemString("updater.pass"), Cfg.getSystemString("updater.project"), false, null);
			sync.close();
			return true;
		} finally {
			a.dispose();
		}
	}

	public void startUpdate(String project, int modified, int news, int delete) {
		listener.startUpdate(project, modified, news, delete);
		total.setMaximum(2 * (modified + news));
	}

	public void startFile(String name) {
		listener.startFile(name);
		label.setText("Updating '" + name + "'");
	}

	public void startCheckFile(String name) {
		listener.startCheckFile(name);
		label.setText("Checking '" + name + "'");
	}

	public void doneCheckFile(String name) {
		listener.doneCheckFile(name);
		total.setValue(total.getValue() + 1);
	}

	public void startReconstruct(String name) {
		listener.startReconstruct(name);
		label.setText("Reconstructing '" + name + "'");
	}

	public void updateReconstruct(String name, float rate) {
		listener.updateReconstruct(name, rate);
		// XXX
	}

	public void doneReconstruct(String name, long fileSize, boolean ok) {
		listener.doneReconstruct(name, fileSize, ok);
		total.setValue(total.getValue() + (ok ? 1 : -1));
	}

	public void doneFile(String name, long fileSize) {
		listener.doneFile(name, fileSize);
	}

	public void doneUpdate(String project) {
		listener.doneUpdate(project);
	}
}

class B {
	public static void main(String arg[]) {
		Lwjgl3ApplicationConfiguration conf = new Lwjgl3ApplicationConfiguration();
		conf.setTitle("Game");
		conf.setWindowedMode(560, 368);
		conf.setResizable(true);
		conf.useOpenGL3(true, 3, 2);
		conf.useVsync(true);
		/* Lwjgl3Application a = */new Lwjgl3Application(new Main(), conf);
	}
}
