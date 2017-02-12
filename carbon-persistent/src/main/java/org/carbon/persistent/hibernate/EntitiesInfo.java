package org.carbon.persistent.hibernate;

import java.util.List;

/**
 * @author Shota Oda 2017/02/10.
 */
public class EntitiesInfo {
    private List<String> entityFqns;

    public EntitiesInfo(List<String> entityFqns) {
        this.entityFqns = entityFqns;
    }

    public List<String> getEntityFqns() {
        return entityFqns;
    }
}
