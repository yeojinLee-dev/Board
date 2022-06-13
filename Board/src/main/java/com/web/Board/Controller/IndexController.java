package com.web.Board.Controller;

import com.web.Board.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PageService pageService;
    private final CommentService commentService;

    @GetMapping("/")
    public String login() {
        return "login";
    }
    @GetMapping("/member/join")
    public String join() {
        return "join";
    }

    @GetMapping("/board")
    public String postList(@RequestParam int page, @RequestParam String query, @RequestParam int category,
                           Model List, Model Login_Id, Model pageBtn, Model Page, Model Query,
                           Model CategoryList, Model Category, HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session.getAttribute("login_id") != null) {
            String login_id = (String) session.getAttribute("login_id");
            Login_Id.addAttribute("login_id", login_id);
        }

        List.addAttribute("list", pageService.setPageList(page, query, category));
        pageBtn.addAttribute("pageBtn", pageService.setPageBtn(page, query, category));
        CategoryList.addAttribute("categoryList", categoryService.findAllCategory());

        Query.addAttribute("query", query);
        Category.addAttribute("category", category);
        Page.addAttribute("page", page);

        return "post-list";
    }

    @GetMapping("/post/new")
    public String createPost(Model member, Model category, Model seq_num, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");
        int post_seq_num = postService.getPostCnt() + 1;

        seq_num.addAttribute("seq_num", post_seq_num);
        category.addAttribute("category", categoryService.findAllCategory());
        member.addAttribute("login_id", login_id);

        //System.out.printf("\nIndexController -> createPost()\nlast_page : %d\n", pageService.getCurrentPage(post_seq_num));
        return "post-create";
    }

    @GetMapping("/member/info")
    public String memberInfo () {
        return "member-info";
    }

    @GetMapping("/post/{post_id}")
    public String readPost(@PathVariable int post_id, @RequestParam int page, @RequestParam String query, @RequestParam int category,
                           Model Post, Model Login_id, Model Page, Model Comment, Model Query, Model Category,
                           HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_id.addAttribute("login_id", login_id);
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        Page.addAttribute("page", page);
        Comment.addAttribute("comment", commentService.findByPost_Id(post_id));
        Query.addAttribute("query", query);
        Category.addAttribute("category", category);

        //System.out.printf("\nIndexController -> readPost()\n comment : %s\n", comments.get(0).getContent());

        return "post-read";
    }

    @GetMapping("/repost/{post_id}")
    public String readPostToUpdate(@PathVariable int post_id, @RequestParam int page, @RequestParam String query,
                                   Model Query, Model Page, Model Post, Model Category) {
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        Category.addAttribute("category", categoryService.findAllCategory());
        Page.addAttribute("page", page);
        Query.addAttribute("query", query);

        return "post-modify";
    }
}
