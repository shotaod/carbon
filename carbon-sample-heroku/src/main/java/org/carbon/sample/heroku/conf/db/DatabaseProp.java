package org.carbon.sample.heroku.conf.db;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Setter;
import org.carbon.web.annotation.Property;

/**
 * @author Shota Oda 2017/02/12.
 */
@Setter
@Property(key = "sample.heroku.db")
public class DatabaseProp {
    private String url;
    public URI getUri() {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI syntax is invalid", e);
        }
    }
}
