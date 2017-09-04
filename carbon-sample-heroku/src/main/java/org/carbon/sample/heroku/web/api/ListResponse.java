package org.carbon.sample.heroku.web.api;

import java.util.List;

import lombok.Getter;

/**
 * @author ubuntu 2017/03/28.
 */
@Getter
public class ListResponse<T> {
    private List<T> data;
    private int size;

    public ListResponse(List<T> data) {
        this.data = data;
        this.size = data.size();
    }
}
