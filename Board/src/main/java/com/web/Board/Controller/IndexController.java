package com.web.Board.Controller;

import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String postList(Model Post, Model Login_Id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");
        Login_Id.addAttribute("login_id", login_id);
        Post.addAttribute("post", postService.findAllPostList());

        return "post-list";
    }

    @GetMapping("/post/create")
    public String postCreate(Model member, Model category, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String login_id = (String) session.getAttribute("login_id");
        member.addAttribute("login_id", login_id);
        category.addAttribute("category", postService.findAllCategory());

        return "post-create";
    }

    @GetMapping("/member/info")
    public String memberInfo() {
        return "member-info";
    }

}
