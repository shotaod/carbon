package org.carbon.sample.v2.web.blog;

import java.util.List;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.ext.jooq.tables.daos.BlogDao;
import org.carbon.sample.v2.ext.jooq.tables.pojos.Blog;

/**
 * @author Shota Oda 2017/02/21.
 */
@Component
public class BlogService {
    @Inject
    private BlogDao dao;

    public List<Blog> findAllBlog() {
        return dao.findAll();
    }

    public Blog findById(Long blogId) {
        return  dao.findById(blogId);
    }


    @Transactional
    public void insertBlog(BlogPostForm form) {
        Blog blog = new Blog(null, form.getTitle(), form.getSummary(), form.getContent());
        dao.insert(blog);
    }
}
