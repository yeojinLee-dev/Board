package com.web.Board.domain.Post;

import com.web.Board.domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Repository
public class PostRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PostRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int savePost(Post post) {
        String sql = "insert into board.POST (TITLE, LOGIN_ID, CONTENT) " +
                "values (?, ?, ?)";

        return jdbcTemplate.update(sql, post.getTitle(), post.getMember().getLogin_id(), post.getContent());
    }
}
