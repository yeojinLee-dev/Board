package com.web.Board.Domain.Comment;

import com.web.Board.Domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public CommentRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Comment> findByPost_Id(int post_id) {
        return jdbcTemplate.query("select m.name, c.content, c.created_date "
                + "from board.COMMENT c "
                + "join board.MEMBER m on c.member_id = m.member_id "
                + "where c.post_id = ?;", CommentRowMapper(), post_id);
    }

    public int saveComment(Comment comment, String login_id) {
        LocalDateTime commentSaveTime = LocalDateTime.now();

        jdbcTemplate.update("insert into board.COMMENT (POST_ID, MEMBER_ID, CREATED_DATE, CONTENT) values (?, (select MEMBER_ID from board.MEMBER where LOGIN_ID = ? limit 1), ?, ?);",
                comment.getPost_id(), login_id, commentSaveTime, comment.getContent());

        return 1;
    }

    private RowMapper<Comment> CommentRowMapper() {
        return (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setContent(rs.getString("CONTENT"));
            comment.setCreated_date(rs.getTimestamp("CREATED_DATE").toLocalDateTime());

            Member member = new Member();
            member.setName(rs.getString("NAME"));

            comment.setMember(member);
            return comment;
        };
    };

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
