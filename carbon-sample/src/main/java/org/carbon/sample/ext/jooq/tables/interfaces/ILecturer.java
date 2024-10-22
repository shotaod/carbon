/**
 * This class is generated by jOOQ
 */
package org.carbon.sample.ext.jooq.tables.interfaces;


import java.io.Serializable;
import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.8.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public interface ILecturer extends Serializable {

    /**
     * Setter for <code>carbondb.lecturer.id</code>.
     */
    public void setId(Long value);

    /**
     * Getter for <code>carbondb.lecturer.id</code>.
     */
    public Long getId();

    /**
     * Setter for <code>carbondb.lecturer.identityClass</code>.
     */
    public void setUsername(String value);

    /**
     * Getter for <code>carbondb.lecturer.identityClass</code>.
     */
    public String getUsername();

    /**
     * Setter for <code>carbondb.lecturer.email</code>.
     */
    public void setEmail(String value);

    /**
     * Getter for <code>carbondb.lecturer.email</code>.
     */
    public String getEmail();

    /**
     * Setter for <code>carbondb.lecturer.thumbnail_id</code>.
     */
    public void setThumbnailId(Long value);

    /**
     * Getter for <code>carbondb.lecturer.thumbnail_id</code>.
     */
    public Long getThumbnailId();

    /**
     * Setter for <code>carbondb.lecturer.pr_text</code>.
     */
    public void setPrText(String value);

    /**
     * Getter for <code>carbondb.lecturer.pr_text</code>.
     */
    public String getPrText();

    /**
     * Setter for <code>carbondb.lecturer.password</code>.
     */
    public void setPassword(String value);

    /**
     * Getter for <code>carbondb.lecturer.password</code>.
     */
    public String getPassword();

    /**
     * Setter for <code>carbondb.lecturer.pr_text_short</code>.
     */
    public void setPrTextShort(String value);

    /**
     * Getter for <code>carbondb.lecturer.pr_text_short</code>.
     */
    public String getPrTextShort();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface ILecturer
     */
    public void from(org.carbon.sample.ext.jooq.tables.interfaces.ILecturer from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface ILecturer
     */
    public <E extends org.carbon.sample.ext.jooq.tables.interfaces.ILecturer> E into(E into);
}
