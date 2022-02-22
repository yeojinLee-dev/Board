package com.web.Board.Domain.Post;

import com.web.Board.Domain.Category.Category;
import com.web.Board.Domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        List<Post> posts = jdbcTemplate.query("select * from board.POST", PostRowMapper());
        List<Member> member;
        List<Category> category;

        for (int i = 0; i < posts.size(); i++) {
            member = jdbcTemplate.query("select * from board.MEMBER where MEMBER_ID = ?", MemberRowMapper(), posts.get(i).getMember_id());
            category = jdbcTemplate.query("select * from board.CATEGORY where CATEGORY_ID = ?", CategoryRowMapper(), posts.get(i).getCategory_id());

            posts.get(i).setMember(member.get(0));
            posts.get(i).setCategory(category.get(0));

        }
        return posts;
    }

    private RowMapper<Post> PostRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();

            post.setPost_id(rs.getInt("POST_ID"));
            post.setTitle(rs.getString("TITLE"));
            post.setContent(rs.getString("CONTENT"));
            post.setMember_id(rs.getInt("MEMBER_ID"));
            post.setCategory_id(rs.getInt("CATEGORY_ID"));
            //post.setCreated_date((LocalDateTime) rs.getObject("CREATED_DATE"));
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
