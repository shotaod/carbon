package org.carbon.sample.heroku.web.blog;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ubuntu 2017/02/21.
 */
@Setter
@Getter
public class BlogPostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String summary;
    @NotBlank
    private String content;
}
