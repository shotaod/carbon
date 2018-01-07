package org.carbon.sample.v2.web.blog;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/02/21.
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
