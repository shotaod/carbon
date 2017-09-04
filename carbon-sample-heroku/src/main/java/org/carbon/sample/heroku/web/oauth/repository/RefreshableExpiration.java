package org.carbon.sample.heroku.web.oauth.repository;

import java.time.temporal.TemporalUnit;

/**
 * @author Shota Oda 2017/08/11.
 */
public abstract class RefreshableExpiration<REFRESH_TOKEN> extends Expiration {
    protected REFRESH_TOKEN refresh_token;
    public RefreshableExpiration(Integer expire, TemporalUnit unit, REFRESH_TOKEN refresh_token) {
        super(expire, unit);
        this.refresh_token = refresh_token;
    }

    abstract public boolean reValidate(REFRESH_TOKEN refresh_token);

    public REFRESH_TOKEN getRefreshToken() {
        return refresh_token;
    }
}
