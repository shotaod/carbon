package org.carbon.persistent.jooq;

import org.carbon.component.annotation.Component;
import org.jooq.util.JavaGenerator;
import org.jooq.util.JavaWriter;
import org.jooq.util.TableDefinition;

/**
 * @author Shota Oda 2017/08/19.
 */
public class CarbonJooqGenerator extends JavaGenerator {
    @Override
    protected void generateDaoClassJavadoc(TableDefinition table, JavaWriter out) {
        super.generateDaoClassJavadoc(table, out);
        out.ref(Component.class);
        out.println("@Component");
    }
}
