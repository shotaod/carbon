package org.carbon.web.container;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ComputedUrl {
    public static class Path {
        private String pathName;
        private String varName;

        private Path(String pathName, String varName) {
            this.pathName = pathName;
            this.varName = varName;
        }

        public static Path Static(String name) {
            return new Path(name, null);
        }
        public static Path Variable(String pathName, String varName) {
            return new Path(pathName, varName);
        }

        public String getPathName() {
            return pathName;
        }

        public String getVarName() {
            return varName;
        }

        public boolean isVar() {
            return varName != null;
        }
    }
	private List<Path> computedPaths;

    public ComputedUrl(List<Path> computedPaths) {
		this.computedPaths = computedPaths;
	}

	public List<Path> getComputedPaths() {
		return computedPaths;
	}

    @Override
    public String toString() {
        return computedPaths.stream().map(Path::getPathName).collect(Collectors.joining("/"));
    }
}
