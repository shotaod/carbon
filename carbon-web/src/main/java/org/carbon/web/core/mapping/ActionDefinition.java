package org.carbon.web.core.mapping;

import org.carbon.web.container.ComputedUrl;
import org.carbon.web.core.ActionArgumentAggregator;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/10/07.
 */
public abstract class ActionDefinition {

    protected HttpMethod httpMethod;
    protected ComputedUrl computed;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
    public ComputedUrl getComputed() {
        return computed;
    }

    abstract public Object execute(ActionArgumentAggregator aggregator) throws Exception;
    abstract public String mappingResult();

    public ActionDefinition(HttpMethod httpMethod,
                            ComputedUrl computed) {
        this.httpMethod = httpMethod;
        this.computed = computed;
    }
}
