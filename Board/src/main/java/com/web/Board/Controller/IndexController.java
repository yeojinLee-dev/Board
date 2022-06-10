package com.web.Board.Controller;

import com.web.Board.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CategoryService categoryService;
    private final PageService pageService;
    private final CommentService commentService;

    @GetMapping("/")
    public String login() {
        //model.addAttribute("post", postService.findAll());

        return "login";
    }
    @GetMapping("/join")
    public String join() {

        return "join";
    }

    @GetMapping("/Board/postList/page={pageNum}&category={category_id}&query={searchKeyword}")
    public String postList(@PathVariable int pageNum, @PathVariable String searchKeyword, @PathVariable int category_id, Model postList,
                           Model Login_Id, Model pageBtn, Model currentPage, Model search_keyword,
                           Model category, HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session.getAttribute("login_id") != null) {
            String login_id = (String) session.getAttribute("login_id");
            Login_Id.addAttribute("login_id", login_id);
        }

        postList.addAttribute("postList", pageService.setPageList(pageNum, searchKeyword, category_id));
        //System.out.printf("IndexController.postList() : postList");

        pageBtn.addAttribute("pageBtn", pageService.setPageBtn(pageNum, searchKeyword, category_id));
        //System.out.printf("IndexController.postList() : pageBtn");

        currentPage.addAttribute("currentPage", pageNum);
        //System.out.printf("IndexController.postList() : currentPage : %d\n", pageNumParam);

        category.addAttribute("category", categoryService.findAllCategory());

        search_keyword.addAttribute("searchKeyword", searchKeyword);

        return "post-list";
    }

    @GetMapping("/Board/post/create")
    public String createPost(Model member, Model category, Model last_page, Model seq_num, HttpServletRequest request) {
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

    @GetMapping("/Board/post/read/{post_id}&page={currentPage}&category={category_id}&query={searchKeyword}")
    public String readPost(@PathVariable int post_id, @PathVariable int currentPage, @PathVariable String searchKeyword,
                           Model Post, Model Login_id, Model CurrentPage, Model Comment, Model SearchKeyword,
                           HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_id.addAttribute("login_id", login_id);
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        CurrentPage.addAttribute("currentPage", currentPage);
        Comment.addAttribute("comment", commentService.findByPost_Id(post_id));
        SearchKeyword.addAttribute("searchKeyword", searchKeyword);

        //System.out.printf("\nIndexController -> readPost()\n comment : %s\n", comments.get(0).getContent());


        return "post-read";
    }

    @GetMapping("/Board/post/update/{post_id}&page={currentPage}&category={category_id}&query={searchKeyword}")
    public String readPostToUpdate(@PathVariable int post_id, @PathVariable int currentPage, @PathVariable String searchKeyword,
                                   Model SearchKeyword, Model CurrentPage, Model Post, Model Category) {
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        Category.addAttribute("category", categoryService.findAllCategory());
        CurrentPage.addAttribute("currentPage", currentPage);
        SearchKeyword.addAttribute("searchKeyword", searchKeyword);
        return "post-modify";
    }
}
