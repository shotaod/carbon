package org.dabuntu.component.scan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ubuntu 2016/10/01
 */
public class TargetBaseScanner {

	// ===================================================================================
	//                                                                          Singleton
	//                                                                          ==========
	public TargetBaseScanner() {
		this.classLoader = Thread.currentThread().getContextClassLoader();
	}

	// ===================================================================================
	//                                                                       Private Field
	//                                                                       =============
	private ClassLoader classLoader;
	private List<Class> classes = new ArrayList<>();


	// ===================================================================================
	//                                                                       Public Method
	//                                                                       =============
	public List<Class> scan(Class scanBase) throws IOException {
		walkPackage(scanBase.getPackage());

		return this.classes;
	}

	public List<Class> getClasses() {
		return classes;
	}

	// ===================================================================================
	//                                                                      Private Method
	//                                                                      ==============
	private void walkPackage(Package scanBasePack) throws IOException {

		URL url = classLoader.getResource(getPath(scanBasePack));

		// Scan 対象のパス
		Path scanBasePath = Paths.get(url.getPath());

		// Scanマーカーパッケージの部分パス  package.markerBase -> package/markerBase
		String scanPackPartialPathStr = scanBasePack.getName().replace(".", "/");

		int packageStart = scanBasePath.toString().lastIndexOf(scanPackPartialPathStr);
		String fileRoot = scanBasePath.toString().substring(0, packageStart);
		Path fileRootPath = Paths.get(fileRoot);

		Files.find(scanBasePath, Integer.MAX_VALUE, (path, attr) -> isClassFile(path))
				.forEach(path -> {
					String fqn = getClassFqn(fileRootPath, path);
					try {
						Class<?> clazz = classLoader.loadClass(fqn);
						this.classes.add(clazz);
					} catch (ClassNotFoundException e) {
						System.out.printf("Not found Class:[%s]", fqn);
						e.printStackTrace();
					}
				});
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

}
