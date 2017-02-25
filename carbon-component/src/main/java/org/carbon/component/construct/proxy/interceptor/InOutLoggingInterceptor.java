package org.carbon.component.construct.proxy.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.carbon.component.construct.proxy.annotation.InOutLogging;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/02.
 */
public class InOutLoggingInterceptor implements MethodInterceptor{

    private Logger logger = LoggerFactory.getLogger(InOutLoggingInterceptor.class);
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        boolean shouldProxy = method.isAnnotationPresent(InOutLogging.class);

        Object result = methodProxy.invokeSuper(obj, args);

        if (shouldProxy) {
            log(obj, method, args, result);
        }

        return result;
    }

    private void log(Object obj, Method method, Object[] args, Object result) {
        String argStr = Arrays.stream(args).map(arg -> {
            String type = arg.getClass().getSimpleName();
            String value = arg.toString();
            return String.format("#%s (value = '%s')#", type, value);
        }).collect(Collectors.joining(", "));
        List<SimpleKeyValue<String, ?>> list = Arrays.asList(
            new SimpleKeyValue<>("InOut Interceptor", "↓↓↓Intercept Result Is Below↓↓↓↓↓↓"),
            new SimpleKeyValue<>("Class Fqn", obj.getClass().getName()),
            new SimpleKeyValue<>("Method Type", method.getReturnType().getName()),
            new SimpleKeyValue<>("Method Name", method.getName()),
            new SimpleKeyValue<>("Arguments", argStr),
            new SimpleKeyValue<>("Out Type", result.getClass().getName()),
            new SimpleKeyValue<>("Out Value", result),
            new SimpleKeyValue<>("InOut Interceptor", "↑↑↑End Of Intercept Result↑↑↑↑↑↑")
        );
        logger.debug("\n" + BoxedTitleMessage.produceRight(list));
    }
}
