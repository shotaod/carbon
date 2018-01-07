/*
 * This file is generated by jOOQ.
*/
package org.carbon.sample.v2.ext.jooq;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;

import org.carbon.sample.v2.ext.jooq.tables.AuthClient;
import org.carbon.sample.v2.ext.jooq.tables.Blog;
import org.carbon.sample.v2.ext.jooq.tables.SchemaVersion;
import org.carbon.sample.v2.ext.jooq.tables.Task;
import org.carbon.sample.v2.ext.jooq.tables.User;
import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Carbon extends SchemaImpl {

    private static final long serialVersionUID = -641895559;

    /**
     * The reference instance of <code>carbon</code>
     */
    public static final Carbon CARBON = new Carbon();

    /**
     * The table <code>carbon.auth_client</code>.
     */
    public final AuthClient AUTH_CLIENT = org.carbon.sample.v2.ext.jooq.tables.AuthClient.AUTH_CLIENT;

    /**
     * The table <code>carbon.blog</code>.
     */
    public final Blog BLOG = org.carbon.sample.v2.ext.jooq.tables.Blog.BLOG;

    /**
     * The table <code>carbon.schema_version</code>.
     */
    public final SchemaVersion SCHEMA_VERSION = org.carbon.sample.v2.ext.jooq.tables.SchemaVersion.SCHEMA_VERSION;

    /**
     * The table <code>carbon.task</code>.
     */
    public final Task TASK = org.carbon.sample.v2.ext.jooq.tables.Task.TASK;

    /**
     * The table <code>carbon.user</code>.
     */
    public final User USER = org.carbon.sample.v2.ext.jooq.tables.User.USER;

    /**
     * No further instances allowed
     */
    private Carbon() {
        super("carbondb", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        List result = new ArrayList();
        result.addAll(getSequences0());
        return result;
    }

    private final List<Sequence<?>> getSequences0() {
        return Arrays.<Sequence<?>>asList(
            Sequences.AUTH_CLIENT_ID_SEQ,
            Sequences.BLOG_ID_SEQ,
            Sequences.TASK_ID_SEQ,
            Sequences.USER_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            AuthClient.AUTH_CLIENT,
            Blog.BLOG,
            SchemaVersion.SCHEMA_VERSION,
            Task.TASK,
            User.USER);
    }
}