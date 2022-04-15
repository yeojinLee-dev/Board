package com.web.Board.Controller;

import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import com.web.Board.Domain.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;
    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/api/post/create")
    public int createPost(@RequestBody Post post, HttpServletRequest request) {
        //System.out.println("글 등록");
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");

        return postService.createPost(post, login_id);
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
}
