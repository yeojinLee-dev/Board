package com.web.Board.Controller;

import com.web.Board.Service.PostService;
import com.web.Board.domain.Category.Category;
import com.web.Board.domain.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;

    @ResponseBody
    @PostMapping("/api/post/create")
    public int createPost(@RequestBody Post post, HttpServletRequest request) {
        System.out.println("글 등록");
        HttpSession session = request.getSession();
        String login_id = (String)session.getAttribute("login_id");

        return postService.createPost(post, login_id);
    }
}
