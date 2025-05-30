package linhlang.webconfig.repository;

import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.model.CategoryBlog;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryBlogRepository implements CommonRepository<CategoryBlog, String> {
    private final DSLContext dsl;

    @Override
    public void save(CategoryBlog data) {

    }

    @Override
    public CategoryBlog load(String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }
}
