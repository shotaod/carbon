package org.carbon.sample.heroku.web.sample.rest.todo;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

/**
 * @author garden 2017/07/16.
 */
@Getter
@Setter
public class TodoParams {
    private List<String> hoges;

    @Override
    public String toString() {
        return hoges.stream().collect(Collectors.joining("\n"));
    }
}
