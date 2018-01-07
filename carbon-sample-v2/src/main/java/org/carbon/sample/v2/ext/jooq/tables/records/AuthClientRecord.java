/*
 * This file is generated by jOOQ.
*/
package org.carbon.sample.v2.ext.jooq.tables.records;


import javax.annotation.Generated;

import org.carbon.sample.v2.ext.jooq.tables.AuthClient;
import org.carbon.sample.v2.ext.jooq.tables.interfaces.IAuthClient;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
public class AuthClientRecord extends UpdatableRecordImpl<AuthClientRecord> implements Record3<Long, String, String>, IAuthClient {

    private static final long serialVersionUID = -869357370;

    /**
     * Setter for <code>carbon.auth_client.id</code>.
     */
    @Override
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>carbon.auth_client.id</code>.
     */
    @Override
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>carbon.auth_client.client_host</code>.
     */
    @Override
    public void setClientHost(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>carbon.auth_client.client_host</code>.
     */
    @Override
    public String getClientHost() {
        return (String) get(1);
    }

    /**
     * Setter for <code>carbon.auth_client.client_id</code>.
     */
    @Override
    public void setClientId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>carbon.auth_client.client_id</code>.
     */
    @Override
    public String getClientId() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return AuthClient.AUTH_CLIENT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return AuthClient.AUTH_CLIENT.CLIENT_HOST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return AuthClient.AUTH_CLIENT.CLIENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getClientHost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getClientId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthClientRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthClientRecord value2(String value) {
        setClientHost(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthClientRecord value3(String value) {
        setClientId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthClientRecord values(Long value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void from(IAuthClient from) {
        setId(from.getId());
        setClientHost(from.getClientHost());
        setClientId(from.getClientId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends IAuthClient> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AuthClientRecord
     */
    public AuthClientRecord() {
        super(AuthClient.AUTH_CLIENT);
    }

    /**
     * Create a detached, initialised AuthClientRecord
     */
    public AuthClientRecord(Long id, String clientHost, String clientId) {
        super(AuthClient.AUTH_CLIENT);

        set(0, id);
        set(1, clientHost);
        set(2, clientId);
    }
}