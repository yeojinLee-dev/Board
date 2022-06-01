package com.web.Board.Controller;

import com.web.Board.Domain.Comment.Comment;
import com.web.Board.Service.CommentService;
import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import com.web.Board.Domain.Post.Post;
import config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static config.BaseResponseStatus.*;

@Controller
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    @ResponseBody
    @PostMapping("/api/post/create")
    public BaseResponse<Integer> createPost(@RequestBody Post post, HttpServletRequest request) {
        //System.out.println("글 등록");
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");

        try {
            if (post.getTitle().length() == 0)
                return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
            if (post.getContent().length() == 0)
                return new BaseResponse<>(POST_POSTS_EMPTY_CONTENTS);
            if (post.getContent().length() > 1000)
                return new BaseResponse<>(POST_POSTS_INVALID_CONTENTS);

            return new BaseResponse<>(postService.createPost(post, login_id));
        } catch (NullPointerException nullPointerException) {
            return new BaseResponse<>(POST_POSTS_EMPTY_CATEGORY);
        }

    }

    @ResponseBody
    @PutMapping("/api/post/update/{post_id}")
    public int updatePost(@RequestBody Post post, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");

        //System.out.printf("controller -> updatePost()\n 수정된 게시물 내용 : %s\n", post.getTitle());
        return postService.updatePost(post, login_id);
    }

    @ResponseBody
    @DeleteMapping("/api/post/delete/{post_id}")
    public int deletePost(@PathVariable int post_id) {
        return postService.deletePost(post_id);
    }

    @ResponseBody
    @PostMapping("/api/post/read/{post_id}")
    public int checkAuthor(@PathVariable int post_id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");
        //System.out.printf("controller -> checkAuthor() : login_id=%s\n", login_id);

        String author = postService.findByPost_Id(post_id).getMember().getLogin_id();
        //System.out.printf("controller -> checkAuthor() : author=%s\n", author);

        int isSameAuthor;

        if (login_id.equals(author))
            isSameAuthor = 1;
        else
            isSameAuthor = 0;

        return isSameAuthor;
    }

    @ResponseBody
    @PostMapping("/api/comment")
    public BaseResponse<Integer> saveComment(@RequestBody Comment comment, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");

        if (comment.getContent().length() == 0)
            return new BaseResponse<>(POST_COMMENT_EMPTY_COMMENTS);
        //System.out.printf("controller -> saveComment() : comment login_id=%s\n", login_id);
        return new BaseResponse<>(commentService.saveComment(comment, login_id));
    }
}
