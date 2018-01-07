package org.carbon.persistent.dialect;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.persistent.exception.DialectResolveException;

/**
 * @author Shota Oda 2017/02/15.
 */
public class ClasspathDialectResolver {
    private ClasspathDialectResolver() {
    }

    public static Dialect resolve() throws DialectResolveException {
        List<Dialect> resolveDialect = Stream.of(Dialect.values())
                .filter(dialect -> existDriver(dialect.getDriverClassName()))
                .collect(Collectors.toList());
        if (resolveDialect.size() > 1) {
            throw multipleDriverFoundException(resolveDialect);
        } else if (resolveDialect.isEmpty()) {
            throw noDriverFoundException();
        } else {
            return resolveDialect.get(0);
        }
    }

    private static boolean existDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private static DialectResolveException multipleDriverFoundException(List<Dialect> dialects) {
        String s = "Dialect Resolve Exception. Multiple Driver found\n";
        String message = dialects.stream().map(Dialect::getDriverClassName).collect(Collectors.joining("\n", s, ""));
        return new DialectResolveException(message);
    }

    private static DialectResolveException noDriverFoundException() {
        return new DialectResolveException("Dialect Resolve Exception. No Driver found");
    }
}
