package org.carbon.web.container;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ComputedPath {
    public static class Node {
        private String pathName;
        private String varName;

        private Node(String pathName, String varName) {
            this.pathName = pathName;
            this.varName = varName;
        }

        public static Node Static(String name) {
            return new Node(name, null);
        }

        public static Node Variable(String pathName, String varName) {
            return new Node(pathName, varName);
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

    private List<Node> computedPaths;

    public ComputedPath(List<Node> computedPaths) {
        this.computedPaths = computedPaths;
    }

    public List<Node> getComputedPaths() {
        return computedPaths;
    }

    @Override
    public String toString() {
        return computedPaths.stream().map(Node::getPathName).collect(Collectors.joining("/"));
    }
}
