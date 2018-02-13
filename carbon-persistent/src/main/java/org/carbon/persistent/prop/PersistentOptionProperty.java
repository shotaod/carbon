package org.carbon.persistent.prop;

import java.util.List;

import org.carbon.modular.annotation.Property;

/**
 * @author Shota Oda 2018/01/11.
 */
@Property(key = "persistent.option")
public class PersistentOptionProperty {
    public class Schema {
        private List<String> actions;
        private List<String> src;

        public List<String> getActions() {
            return actions;
        }

        public void setActions(List<String> actions) {
            this.actions = actions;
        }

        public List<String> getSrc() {
            return src;
        }

        public void setSrc(List<String> src) {
            this.src = src;
        }
    }

    private Schema schema;
    private Boolean test;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }
}
