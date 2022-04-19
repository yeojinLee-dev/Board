package com.web.Board.Domain.Comment;

import com.web.Board.Domain.Member.Member;
import com.web.Board.Domain.Post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private int comment_id;

    private String content;

    private LocalDateTime created_date;

    private Member member;

    private Post post;

    private int member_id;

    private int post_id;

    public Comment (String content, LocalDateTime created_date, int member_id, int post_id) {
        this.content = content;
        this.created_date = created_date;
        this.member_id = member_id;
        this.post_id = post_id;
    }
}
