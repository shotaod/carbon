/*
 * This file is generated by jOOQ.
*/
package org.carbon.sample.v2.ext.jooq.tables.interfaces;


import java.io.Serializable;
import javax.annotation.Generated;


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
public interface ITask extends Serializable {

    /**
     * Setter for <code>carbon.task.id</code>.
     */
    public void setId(Long value);

    /**
     * Getter for <code>carbon.task.id</code>.
     */
    public Long getId();

    /**
     * Setter for <code>carbon.task.title</code>.
     */
    public void setTitle(String value);

    /**
     * Getter for <code>carbon.task.title</code>.
     */
    public String getTitle();

    /**
     * Setter for <code>carbon.task.description</code>.
     */
    public void setDescription(String value);

    /**
     * Getter for <code>carbon.task.description</code>.
     */
    public String getDescription();

    /**
     * Setter for <code>carbon.task.user_id</code>.
     */
    public void setUserId(Long value);

    /**
     * Getter for <code>carbon.task.user_id</code>.
     */
    public Long getUserId();

    /**
     * Setter for <code>carbon.task.available</code>.
     */
    public void setAvailable(Boolean value);

    /**
     * Getter for <code>carbon.task.available</code>.
     */
    public Boolean getAvailable();

    /**
     * Setter for <code>carbon.task.finished</code>.
     */
    public void setFinished(Boolean value);

    /**
     * Getter for <code>carbon.task.finished</code>.
     */
    public Boolean getFinished();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface ITask
     */
    public void from(org.carbon.sample.v2.ext.jooq.tables.interfaces.ITask from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface ITask
     */
    public <E extends org.carbon.sample.v2.ext.jooq.tables.interfaces.ITask> E into(E into);
}