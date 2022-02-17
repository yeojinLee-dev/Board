package com.web.Board.domain.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
}
