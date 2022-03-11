package com.web.Board.Controller;

import com.web.Board.Domain.Post.PageBtn;
import com.web.Board.Service.PageService;
import com.web.Board.Service.CategoryService;
import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PageService pageService;

    @GetMapping("/")
    public String login() {
        //model.addAttribute("post", postService.findAll());
        return "login";
    }

    @GetMapping("/member/join")
    public String join() {
        return "join";
    }

    @GetMapping("/post/list/{pageNum}")
    public String postList(Model postList, Model Login_Id, Model pageBtn, HttpServletRequest request, @PathVariable int pageNum) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_Id.addAttribute("login_id", login_id);
        postList.addAttribute("postList", pageService.setPageList(pageNum));
        System.out.printf("IndexController.postList() : postList");

        pageBtn.addAttribute("pageBtn", pageService.setPageBtn());
        System.out.printf("IndexController.postList() : pageBtn");

        List<PageBtn> btns = pageService.setPageBtn();
        /*
        for (int i = 0; i < btns.size(); i++)
            System.out.printf("IndexController : pageBtn Model\n-> %d", btns.get(i).getPageNum());
        */
        return "post-list";
    }

    @GetMapping("/post/create")
    public String createPost(Model member, Model category, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String login_id = (String) session.getAttribute("login_id");
        member.addAttribute("login_id", login_id);
        category.addAttribute("category", categoryService.findAllCategory());

        return "post-create";
    }

    @GetMapping("/member/info")
    public String memberInfo () {
        return "member-info";
    }

    @GetMapping("/post/read/{post_id}")
    public String readPost(@PathVariable int post_id, Model Post, Model Login_id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_id.addAttribute("login_id", login_id);
        Post.addAttribute("post", postService.findByPost_Id(post_id));

        // System.out.printf("controller -> postRead() : %s\n%s", postService.findByPost_Id(post_id).getTitle(), postService.findByPost_Id(post_id).getCategory().getName());
        return "post-read";
    }

    @GetMapping("/post/update/{post_id}")
    public String readPostToUpdate(@PathVariable int post_id, Model Post, Model Category, HttpServletRequest request) {
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        Category.addAttribute("category", categoryService.findAllCategory());

        return "post-modify";

    }
}
