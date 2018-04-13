package org.carbon.web.mapping;

import org.carbon.web.container.ComputedPath;
import org.carbon.web.core.args.ActionArgumentAggregatorFactory;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/10/07.
 */
public abstract class ActionDefinition {

    protected HttpMethod httpMethod;
    protected ComputedPath computed;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
    public ComputedPath getComputed() {
        return computed;
    }

    abstract public Object execute(ActionArgumentAggregatorFactory.ActionArgumentAggregator aggregator) throws Throwable;
    abstract public String mappingResult();

    public ActionDefinition(HttpMethod httpMethod,
                            ComputedPath computed) {
        this.httpMethod = httpMethod;
        this.computed = computed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionDefinition)) return false;

        ActionDefinition that = (ActionDefinition) o;

        //noinspection SimplifiableIfStatement
        if (httpMethod != that.httpMethod) return false;
        return computed.equals(that.computed);
    }

    @Override
    public int hashCode() {
        int result = httpMethod.hashCode();
        result = 31 * result + computed.hashCode();
        return result;
    }
}
