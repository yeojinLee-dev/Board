package com.web.Board.domain.Category;

import com.web.Board.domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public CategoryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int saveCategory(Category category) {
        return jdbcTemplate.update("insert into board.CATEGORY (NAME, IS_DELETED) VALUES (?, ?)",
                category.getName(), category.is_deleted());
    }

    public List<Category> findAllCategory() {
        return jdbcTemplate.query("select * from board.CATEGORY", CategoryRowMapper());
    }

    private RowMapper<Category> CategoryRowMapper() {
        return (rs, rowNum) -> {
            Category category = new Category();
            category.setCategory_id(rs.getInt("CATEGORY_ID"));
            category.setName(rs.getString("NAME"));
            category.set_deleted(rs.getBoolean("IS_DELETED"));
            return category;
        };
    }
}
