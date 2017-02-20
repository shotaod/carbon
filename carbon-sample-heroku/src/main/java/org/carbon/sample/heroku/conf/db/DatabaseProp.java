package org.carbon.sample.heroku.conf.db;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Setter;

/**
 * @author Shota Oda 2017/02/12.
 */
@Setter
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
