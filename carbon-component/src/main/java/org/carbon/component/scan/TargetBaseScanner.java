package org.carbon.component.scan;

import org.carbon.component.exception.UnsupportedProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * @author Shota Oda 2016/10/01
 */
public class TargetBaseScanner {

	private Logger logger = LoggerFactory.getLogger(TargetBaseScanner.class);

	private static final String Protocol_File = "file";
	private static final String Protocol_Jar = "jar";

	// ===================================================================================
	//                                                                          Singleton
	//                                                                          ==========
	public TargetBaseScanner() {
		this.classLoader = ClassLoader.getSystemClassLoader();
	}

	// ===================================================================================
	//                                                                       Private Field
	//                                                                       =============
	private ClassLoader classLoader;
	private List<Class<?>> classes = new ArrayList<>();


	// ===================================================================================
	//                                                                       Public Method
	//                                                                       =============
	public List<Class<?>> scan(Class scanBase) throws IOException {
		this.classes = new ArrayList<>();
		walkPackage(scanBase.getPackage());
		return this.classes;
	}

	public List<Class<?>> getClasses() {
		return classes;
	}

	// ===================================================================================
	//                                                                      Private Method
	//                                                                      ==============
	private void walkPackage(Package scanBasePack) throws IOException {

		String packagePath = getPath(scanBasePack);
		URL url = classLoader.getResource(packagePath);
		String protocol = url.getProtocol();
		// Scan 対象のパス
		if (Protocol_File.equals(protocol)) {
			Path scanBasePath = Paths.get(url.getPath());

			// Scanマーカーパッケージの部分パス  package.markerBase -> package/markerBase
			String scanPackPartialPathStr = scanBasePack.getName().replace(".", "/");

			int packageStart = scanBasePath.toString().lastIndexOf(scanPackPartialPathStr);
			String fileRoot = scanBasePath.toString().substring(0, packageStart);
			Path fileRootPath = Paths.get(fileRoot);

			Files.find(scanBasePath, Integer.MAX_VALUE, (path, attr) -> isClassFile(path))
				.forEach(path -> {
					String fqn = getClassFqn(fileRootPath, path);
					addClass(fqn);
				});
		} else if (Protocol_Jar.equals(protocol)) {
			JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
			Enumeration<JarEntry> entries = jarURLConnection.getJarFile().entries();
			URL[] urls = { url };
			classLoader = URLClassLoader.newInstance(urls);
			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class")) continue;
				String className = je.getName().substring(0,je.getName().length()-6).replace('/', '.');
				addClass(className);
			}
		} else {
			throw new UnsupportedProtocolException("protocol:[" + protocol + "] is not supported.");
		}
	}

	private String getPath(Package pack) {
		return pack.getName().replace(".", "/");
	}

	private boolean isClassFile(Path path) {
		File file = path.toFile();
		return file.isFile() && file.getName().endsWith(".class");
	}

	private String getClassFqn(Path fileRoot, Path classFilePath) {

		// /package/Clazz.class
		String classFQN = classFilePath.toString().replace(fileRoot.toString(), "");

		// remove suffix ".class"
		classFQN = classFQN.substring(0, classFQN.indexOf(".class"));

		// replace "/" to "."
		classFQN = classFQN.replace('/', '.');

		// remove head "."
		if (classFQN.startsWith(".")) {
			classFQN = classFQN.substring(1, classFQN.length());
		}


		return classFQN;
	}

	private void addClass(String className) {
		try {
			Class<?> clazz = classLoader.loadClass(className);
			this.classes.add(clazz);
		} catch (ClassNotFoundException e) {
			logger.error("Not found Class:[%s]", className);
			System.exit(0);
		}
	}
}
