package com.web.Board.Controller;

import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/")
    public String login() {
        //model.addAttribute("post", postService.findAll());
        return "login";

    }

    @GetMapping("/member/join")
    public String join() {
        return "join";
    }

    @GetMapping("/post/list")
    public String postList(Model LoginId, Model PostList) {
        LoginId.addAttribute("member", login_id);
        return "post-list";
    }

    @GetMapping("/post/create")
    public String postCreate() {
        return "post-create";
    }
}
