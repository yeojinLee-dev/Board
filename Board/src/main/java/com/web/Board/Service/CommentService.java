package com.web.Board.Service;

import com.web.Board.Domain.Comment.Comment;
import com.web.Board.Domain.Comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> findByPost_Id(int post_id) {
        return commentRepository.findByPost_Id(post_id);
    }

    public int saveComment(Comment comment, String login_id) {
        //System.out.printf("service -> saveComment() : comment login_id = %s\n", login_id);
        return commentRepository.saveComment(comment, login_id);
    }
}
