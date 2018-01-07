package org.carbon.sample.v2.web.blog;

import java.util.List;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.v2.ext.jooq.tables.pojos.Blog;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/21.
 */
@Controller
public class BlogController {
    @Inject
    private BlogService service;

    @Action(url = "/blog", method = HttpMethod.GET)
    public HtmlResponse blogGet() {
        HtmlResponse htmlResponse = new HtmlResponse("/blog/index");
        List<Blog> blogs = service.findAllBlog();
        htmlResponse.putData("data", blogs);
        return htmlResponse;
    }

    @Action(url = "/blog/entry/{entryId}", method = HttpMethod.GET)
    public HtmlResponse blogEntryGet(@PathVariable("entryId") String entryId) {
        HtmlResponse htmlResponse = new HtmlResponse("/blog/entry");
        Blog blog = service.findById(Long.parseLong(entryId));
        htmlResponse.putData("data", blog);
        return htmlResponse;
    }

    @Action(url ="/blog/post", method = HttpMethod.GET)
    public HtmlResponse blogPostGet() {
        return new HtmlResponse("/blog/post");
    }

    @Action(url = "/blog/post", method = HttpMethod.POST)
    public HttpOperation blogPostPost(@Validate @RequestBody BlogPostForm form) {
        service.insertBlog(form);
        return RedirectOperation.to("/blog");
    }
}
