package com.web.Board.domain.Post;

import com.web.Board.domain.Category.Category;
import com.web.Board.domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PostRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int savePost(Post post) {
        List<Member> member = jdbcTemplate.query( "select * from board.MEMBER where LOGIN_ID = ? ", MemberRowMapper(), post.getMember().getLogin_id());


        String sql = "insert into board.POST (TITLE, MEMBER_ID, CONTENT, CATEGORY_ID) " +
                "values (?, (select MEMBER_ID from board.MEMBER where LOGIN_ID = ? limit 1), ?, ?)";

        System.out.println("savePost() : " + String.format("%s\n%s\n%s\n%d", post.getTitle(),
                post.getMember().getLogin_id(), post.getContent(), post.getCategory_id()));

        jdbcTemplate.update(sql, post.getTitle(), post.getMember().getLogin_id(), post.getContent(), post.getCategory_id());
        return 1;
    }

    public List<Post> findAllPost() {
        return jdbcTemplate.query("select * from board.POST", PostRowMapper());
    }

    private RowMapper<Post> PostRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();

            post.setTitle(rs.getString("TITLE"));
            post.setContent(rs.getString("CONTENT"));
            post.setMember((Member) rs.getObject("MEMBER"));
            post.setCategory_id(rs.getInt("CATEGORY_ID"));
            post.setCreated_date((LocalDateTime) rs.getObject("CREATED_DATE"));
            return post;
        };
    }

    private RowMapper<Member> MemberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setName(rs.getString("NAME"));
            member.setLogin_id(rs.getString("LOGIN_ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setEmail(rs.getString("EMAIL"));
            member.setPhone(rs.getString("PHONE"));
            return member;
        };
    }

}
