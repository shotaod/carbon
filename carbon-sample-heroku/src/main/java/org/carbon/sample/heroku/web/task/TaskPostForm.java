package org.carbon.sample.heroku.web.task;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ubuntu 2017/03/28.
 */
@Setter
@Getter
public class TaskPostForm {
    private String title;
    private String description;
}
