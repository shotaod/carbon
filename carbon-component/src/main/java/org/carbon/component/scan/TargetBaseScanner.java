package org.carbon.component.scan;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Transparent;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.exception.UnsupportedProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/01
 */
public class TargetBaseScanner {

    private Logger logger = LoggerFactory.getLogger(TargetBaseScanner.class);

    private static final String Protocol_File = "file";
    private static final String Protocol_Jar = "jar";
    private static final String Class_Suffix = ".class";
    private static final int Class_Suffix_Length = Class_Suffix.length();

    private static final Set<Class<? extends Annotation>> Annotations_Escape = Stream.of(
            Documented.class,
            Retention.class,
            Target.class
    ).collect(Collectors.toSet());

    // ===================================================================================
    //                                                                       Private Field
    //                                                                       =============
    private ClassLoader classLoader;

    public TargetBaseScanner() {
        this.classLoader = ClassLoader.getSystemClassLoader();
    }

    // ===================================================================================
    //                                                                       Public Method
    //                                                                       =============
    public Set<Class<?>> scan(Class scanBase, Set<Class<? extends Annotation>> scanTargets) throws PackageScanException {
        Package scanBasePackage = scanBase.getPackage();
        logger.debug("Start scan at package [{}]", scanBasePackage);
        logger.debug("Escape annotations are {}", Annotations_Escape);
        return walkPackage(scanBasePackage).stream()
                .flatMap(this::getClassStream)
                .filter(clazz -> isScanTarget(clazz, scanTargets))
                .collect(Collectors.toSet());
    }

    // ===================================================================================
    //                                                                      Private Method
    //                                                                      ==============
    private List<String> walkPackage(Package scanBasePack) throws PackageScanException {
        List<String> classNames = new ArrayList<>();
        String packagePath = getPath(scanBasePack);
        URL url = classLoader.getResource(packagePath);
        String protocol = url != null ? url.getProtocol() : null;
        // Scan 対象のパス
        if (Protocol_File.equals(protocol)) {
            Path scanBasePath = Paths.get(url.getPath());

            // Scanマーカーパッケージの部分パス  package.markerBase -> package/markerBase
            String scanPackPartialPathStr = scanBasePack.getName().replace(".", "/");

            int packageStart = scanBasePath.toString().lastIndexOf(scanPackPartialPathStr);
            String fileRoot = scanBasePath.toString().substring(0, packageStart);
            Path fileRootPath = Paths.get(fileRoot);

            try {
                Files.find(scanBasePath, Integer.MAX_VALUE, (path, attr) -> isClassFile(path))
                        .forEach(path -> {
                            String fqn = getClassFqn(fileRootPath, path);
                            classNames.add(fqn);
                        });
            } catch (IOException e) {
                throw new PackageScanException(protocol, e);
            }
        } else if (Protocol_Jar.equals(protocol)) {
            Enumeration<JarEntry> entries;
            try {
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                entries = jarURLConnection.getJarFile().entries();
            } catch (IOException e) {
                throw new PackageScanException(protocol, e);
            }
            URL[] urls = {url};
            classLoader = URLClassLoader.newInstance(urls);
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();

                if (je.isDirectory() || !je.getName().endsWith(Class_Suffix)) continue;
                String className = je.getName().substring(0, je.getName().length() - Class_Suffix_Length).replace('/', '.');
                classNames.add(className);
            }
        } else {
            throw new UnsupportedProtocolException("protocol:[" + protocol + "] is not supported.");
        }

        return classNames;
    }

    private String getPath(Package pack) {
        return pack.getName().replace(".", "/");
    }

    private boolean isClassFile(Path path) {
        File file = path.toFile();
        return file.isFile() && file.getName().endsWith(Class_Suffix);
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

    private Stream<Class<?>> getClassStream(String className) {
        try {
            Class<?> clazz = classLoader.loadClass(className);
            if (clazz.isAnnotation() || clazz.isInterface()) return Stream.empty();
            return Stream.of(clazz);
        } catch (ClassNotFoundException impossible) {
            logger.error("Not found Class:[%s]", className);
            throw new RuntimeException(impossible);
        }
    }

    private boolean isScanTarget(Class<?> clazz, Set<Class<? extends Annotation>> scanTargets) {
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
        if (clazz.isAnnotationPresent(Transparent.class)) return false;
        if (declaredAnnotations.length == 0) return false;
        if (scanTargets.stream().anyMatch(clazz::isAnnotationPresent)) return true;

        return Arrays.stream(declaredAnnotations)
                .map(Annotation::annotationType)
                .filter(annotation -> !Annotations_Escape.contains(annotation))
                .anyMatch(type -> isScanTarget(type, scanTargets));
    }
}
